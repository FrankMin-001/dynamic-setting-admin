package com.smalldragon.yml.pools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Configuration
public class SafeThreadPoolConfig {

    @Value("${safe.pool.core-size:5}")
    private int coreSize;

    @Value("${safe.pool.max-size:10}")
    private int maxSize;

    @Value("${safe.pool.keep-alive-time:60}")
    private int keepAliveTime;

    @Value("${safe.pool.queue-capacity:100}")
    private int queueCapacity;

    @Value("${safe.pool.thread-name-prefix:safe-pool-}")
    private String threadNamePrefix;

    /**
     * 符合阿里巴巴规范的安全线程池（全局默认线程池）
     * 1. 不使用Executors创建
     * 2. 使用有界队列
     * 3. 明确拒绝策略
     * 
     * @Primary 注解使其成为默认的全局线程池，可直接注入使用，无需指定 @Qualifier
     */
    @Bean("CommonThreadPool")
    @Primary
    public ThreadPoolExecutor safeThreadPool() {
        // 创建有界队列
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(queueCapacity);

        // 创建线程工厂
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName(threadNamePrefix + threadNumber.getAndIncrement());
                thread.setDaemon(false);
                return thread;
            }
        };

        // 自定义拒绝策略：记录日志并抛出异常
        RejectedExecutionHandler rejectedHandler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                log.warn("Task rejected from thread pool: {}. Pool stats: active={}, queueSize={}, poolSize={}",
                        r.getClass().getSimpleName(),
                        executor.getActiveCount(),
                        executor.getQueue().size(),
                        executor.getPoolSize());
                throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + executor.toString());
            }
        };

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                coreSize,
                maxSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                workQueue,
                threadFactory,
                rejectedHandler
        );

        // 允许核心线程超时
        executor.allowCoreThreadTimeOut(true);

        // 预启动所有核心线程
        executor.prestartAllCoreThreads();

        log.info("SafeThreadPool initialized: coreSize={}, maxSize={}, queueCapacity={}, threadNamePrefix={}",
                coreSize, maxSize, queueCapacity, threadNamePrefix);

        return executor;
    }

}

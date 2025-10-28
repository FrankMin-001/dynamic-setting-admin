package com.smalldragon.yml.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * @author YML
 */
@Data
@Configuration
public class JedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.database}")
    private int database;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxTotal;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private int maxWait;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxWait(Duration.ofMillis(maxWait));
        poolConfig.setMinIdle(minIdle);
        // 建议开启连接检测
        poolConfig.setTestOnBorrow(true);


        if (password != null && !password.isEmpty()) {
            return new JedisPool(poolConfig, host, port, timeout, password,database);
        }
        return new JedisPool(poolConfig, host, port, timeout,null,database);
    }

}

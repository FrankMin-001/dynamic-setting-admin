package com.smalldragon.yml.function.service;

import java.util.concurrent.CompletableFuture;

public interface SignInService {
    /**
     * @Description 签到打卡模拟 (无消息队列分压直接进行业务操作)
     * @Author YML
     * @Date 2025/4/12
     */
    CompletableFuture<Boolean> doingByThreadPool();

    /**
     * @Description 签到打卡模拟 (无消息队列分压直接进行业务操作)
     * @Author YML
     * @Date 2025/4/12
     */
    void doingByQueue();

}

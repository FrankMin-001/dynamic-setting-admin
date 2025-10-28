package com.smalldragon.yml.function.service.Impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.google.protobuf.StringValue;
import com.smalldragon.yml.constants.CommonConstants;
import com.smalldragon.yml.context.UserContext;
import com.smalldragon.yml.function.service.SignInService;
import com.smalldragon.yml.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.smalldragon.yml.constants.CommonConstants.*;

/**
 * @Author YML
 * @Date 2025/4/12 16:54
 **/
@Slf4j
@RequiredArgsConstructor
@Service("SignInServiceImpl")
public class SignInServiceImpl implements SignInService {

    private final RedisUtil redisUtil;

    private final String SIGN_IN_STREAM = "SignInStream::Queue";

    @Override
    @Async("CommonThreadPool")
    public CompletableFuture<Boolean> doingByThreadPool() {
        // 生成唯一id - 模仿用户id
        long uuid = IdUtil.getSnowflake().nextId();
        // 在签到表中进行签到并记录时间戳 (1代表已经签到)
        redisUtil.hset("SignInTable",uuid+"::"+System.currentTimeMillis(),1);
        return CompletableFuture.completedFuture(true);
    }

    @Override
    @Async("CommonThreadPool")
    public void doingByQueue() {

        // 打印当前线程名称（关键检查点）
        System.out.println("当前执行线程: " + Thread.currentThread().getName());

        // 生成唯一id - 模仿用户id
        long uuid = IdUtil.getSnowflake().nextId();
        // 时间戳
        long thisTime = System.currentTimeMillis();
        // 内容体 (任务完成状态)
        Map<String, String> messageMap = new HashMap<>();
        // 设置消息队列完成状态
        messageMap.put(QUEUE_STATUS, QUEUE_UNFINISHED);
        // 记录打卡用户
        messageMap.put(USER_ID, String.valueOf(uuid));
        // 记录打卡时间
        messageMap.put(CREATE_TIME, String.valueOf(thisTime));

        // 存入消息队列中 (根据业务需求只有一个打卡需求,但是后续可能会有其他业务需求,所以消费团暂时只有一个名为打卡逻辑的消费者)
        redisUtil.xAdd(SIGN_IN_STREAM,messageMap);
    }

}

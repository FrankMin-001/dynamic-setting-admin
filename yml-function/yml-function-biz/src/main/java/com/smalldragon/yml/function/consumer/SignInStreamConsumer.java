package com.smalldragon.yml.function.consumer;

import com.smalldragon.yml.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

import static com.smalldragon.yml.constants.CommonConstants.*;


/**
 * @author YML
 * @descte 用于消费redis并发签到业务 (后期应该删除)
 */
@Slf4j
@RequiredArgsConstructor
@Service("SignInStreamConsumer")
public class SignInStreamConsumer {

    private final RedisUtil redisUtil;

    @PostConstruct
    public void init() {
        // 初始化消费组
        try {
            redisUtil.xGroupCreate(SIGN_IN_STREAM,SIGN_IN_CONSUMER_GROUP,"0-0", true);
        } catch (Exception e) {
            log.warn("消费组已存在或创建失败: {}", e.getMessage());
        }

        // 启动消费者线程
        new Thread(this::consumeMessages).start();
    }

    // 该方法目的就是获取消息队列中的内容,将该内容执行 processSignInMessage 方法,执行成功后,该消息进行确认消费
    private void consumeMessages() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // 从消息队列读取消息
                List<Map.Entry<String, List<StreamEntry>>> messages = redisUtil.xReadGroup(
                        SIGN_IN_CONSUMER_GROUP,
                        SIGN_IN_CONSUMER_DOING,
                        10,
                        1000L,
                        SIGN_IN_STREAM,
                        StreamEntryID.UNRECEIVED_ENTRY
                );

                if (messages != null && !messages.isEmpty()) {
                    for (Map.Entry<String, List<StreamEntry>> streamEntry : messages) {
                        List<StreamEntry> entries = streamEntry.getValue();
                        for (StreamEntry entry : entries) {
                            try {
                                // 将StreamEntry转换为Map
                                Map<String, String> message = entry.getFields();
                                // 处理消息
                                processSignInMessage(message);

                                // 确认消息已处理
                                redisUtil.xAck(SIGN_IN_STREAM, SIGN_IN_CONSUMER_GROUP, String.valueOf(entry.getID()));

                                // 确认消费后就删除已消费的信息
                                redisUtil.xDel(SIGN_IN_STREAM,String.valueOf(entry.getID()));
                            } catch (Exception e) {
                                log.error("处理签到消息失败: entryId={}", entry.getID(), e);
                                // 可以将失败的消息放入死信队列或重试队列
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("消费消息异常", e);
                try {
                    // 防止异常导致频繁循环
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    // 线程被中断时退出循环
                    break;
                }
            }
        }
    }

    private void processSignInMessage(Map<String, String> message) {
        // 获取消息内容
        String userId = message.get(USER_ID);
        String createTime = message.get(CREATE_TIME);


        // 检查是否已经处理过（幂等性检查）
        if (redisUtil.hget("SignInTable", userId + "::" + createTime) != null) {
            log.info("用户 {} 在 {} 的签到已处理过，跳过", userId, createTime);
            return;
        }

        // 执行签到逻辑
        log.info("处理用户 {} 的签到，签到时间: {}", userId, createTime);
        redisUtil.hset("SignInTable", userId + "::" + createTime, "1");

        // 这里可以添加其他业务逻辑，如积分增加、通知等
    }
}

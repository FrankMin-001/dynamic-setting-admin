package com.smalldragon.yml.function.service;

import com.smalldragon.yml.function.dal.SimpleChat.MessageDTO;
import com.smalldragon.yml.function.dal.SimpleChat.MessageQueryDTO;

import java.util.Map;

public interface SimpleChatService {

    /**
     * @Description 创建简易聊天室
     * @Author YML
     * @Date 2025/4/3
     */
    Boolean createSimpleChat();

    /**
     * @Description 关闭临时聊天房间
     * @Author YML
     * @Date 2025/4/3
     */
    Boolean closeRoom();

    /**
     * @Description 查询聊天室是否存在
     * @Author YML
     * @Date 2025/4/3
     */
    Boolean isAlive(String roomId);

    /**
     * @Description 为聊天室发送消息
     * @Author YML
     * @Date 2025/4/3
     */
    Boolean sendMessageByRoomId(MessageDTO messageDTO);

    /**
     * @Description 查询最近消息(默认10条)
     * @Author YML
     * @Date 2025/4/3
     */
    Map<String,String> getRecentMessage(MessageQueryDTO queryDTO);

    /**
     * @Description 给客户端推送消息
     * @Author YML
     * @Date 2025/4/10
     */
    void sendMessageToWebSocket(String roomId,String message);

}

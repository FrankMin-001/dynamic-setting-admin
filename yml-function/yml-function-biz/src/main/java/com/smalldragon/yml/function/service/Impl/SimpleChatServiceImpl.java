package com.smalldragon.yml.function.service.Impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.smalldragon.yml.context.UserContext;
import com.smalldragon.yml.function.dal.SimpleChat.MessageDTO;
import com.smalldragon.yml.function.dal.SimpleChat.MessageQueryDTO;
import com.smalldragon.yml.function.service.SimpleChatService;
import com.smalldragon.yml.function.websocket.SimpleChatWebSocket;
import com.smalldragon.yml.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author YML
 * @Date 2025/4/3 15:31
 **/
@Slf4j
@RequiredArgsConstructor
@Service("SimpleChatServiceImpl")
public class SimpleChatServiceImpl implements SimpleChatService {

    private final RedisUtil redisUtil;

    @Override
    public Boolean createSimpleChat() {
        String loginUsername = UserContext.getLoginUsername();

        if (redisUtil.hasRoom(loginUsername)){
            throw new RuntimeException("存在创建的聊天未关闭!无法重新创建!");
        }

        Map<String, String> messageMap = new HashMap<>();
        messageMap.put(DateUtil.date().getTime()+"::"+loginUsername,"创建聊天室成功!");

        // 创建 Stream 数据类型 (以用户名作为房间名)
        redisUtil.addMessageToRoom(loginUsername, messageMap);

        return true;
    }

    @Override
    public Boolean closeRoom() {
        String loginUsername = UserContext.getLoginUsername();

        if (!redisUtil.hasRoom(loginUsername)){
            throw new RuntimeException("不存在已创建未关闭的聊天室!");
        }

        redisUtil.closeRoom(loginUsername);

        return true;
    }

    @Override
    public Boolean isAlive(String roomId) {
        if (!StrUtil.isEmpty(roomId)){
            return redisUtil.hasRoom(roomId);
        }
        String loginUsername = UserContext.getLoginUsername();
        return redisUtil.hasRoom(loginUsername);
    }

    @Override
    public Boolean sendMessageByRoomId(MessageDTO messageDTO) {
        String message = messageDTO.getMessage();
        String roomId = messageDTO.getRoomId();

        // 判断聊天室是否存在
        validatedRoom(roomId);

        // 发送消息
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put(DateUtil.date().getTime()+"::"+UserContext.getLoginUsername(),message);

        sendMessageToWebSocket(roomId, JSONUtil.toJsonStr(messageMap));
        redisUtil.addMessageToRoom(roomId,messageMap);

        return true;
    }

    @Override
    public Map<String, String> getRecentMessage(MessageQueryDTO queryDTO) {
        String roomId = queryDTO.getRoomId();

        // 判断聊天室是否存在
        validatedRoom(roomId);

        // 获取消息
        return redisUtil.queryRoomMessage(roomId,queryDTO.getCount());
    }

    @Override
    public void sendMessageToWebSocket(String roomId, String message) {
        SimpleChatWebSocket.sendMessage(roomId,message);
    }

    private void validatedRoom(String roomId) {
        if (!redisUtil.hasRoom(roomId)){
            throw new RuntimeException("发送失败,聊天室不存在或已关闭!");
        }
    }

}

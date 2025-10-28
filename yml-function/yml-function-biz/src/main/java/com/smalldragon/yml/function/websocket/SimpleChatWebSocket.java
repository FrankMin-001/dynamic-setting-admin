package com.smalldragon.yml.function.websocket;

import com.smalldragon.yml.function.api.ApiSimpleChatService;
import com.smalldragon.yml.function.service.SimpleChatService;
import com.smalldragon.yml.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YML
 */
@Slf4j
@Component
@ServerEndpoint(value = "/chat/{roomId}")
public class SimpleChatWebSocket {

    private SimpleChatService simpleChatService;
    private static Map<String, Set<Session>> roomSessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(@PathParam("roomId") String roomId, Session session) {
        // 手动获取 Bean
        this.simpleChatService = SpringContextUtil.getBean(SimpleChatService.class);

        // 验证房间是否存在
        Boolean isAlive = simpleChatService.isAlive(roomId);
        if (Boolean.FALSE.equals(isAlive)) {
            log.info("要连接的房间号{}不存在或已关闭!",roomId);
            throw new RuntimeException("要连接的房间号" + roomId + "不存在或已关闭!");
        }

        // 将会话添加到房间
        roomSessions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);

        log.info("连接成功: 房间号-"+roomId);
    }

    @OnClose
    public void onClose(@PathParam("roomId") String roomId, Session session) {
        // 从房间会话中移除会话
        Set<Session> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                roomSessions.remove(roomId);
            }
        }
    }

    public static void sendMessage(String roomId, String message) {
        Set<Session> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            for (Session session : sessions) {
                if (session.isOpen()) {
                    session.getAsyncRemote().sendText(message);
                }
            }
        }
    }

}

package com.nixiak.guidingdog_dashboard.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket服务端 - 用于推送实时数据
 */
@Slf4j
@Component
@ServerEndpoint("/ws/realtime/{dogId}")
public class RealtimeDataWebSocket {

    // 存储每个dogId的所有连接
    private static final ConcurrentHashMap<String, CopyOnWriteArraySet<RealtimeDataWebSocket>> webSocketMap =
            new ConcurrentHashMap<>();

    private Session session;
    private String dogId;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("dogId") String dogId) {
        this.session = session;
        this.dogId = dogId;

        webSocketMap.putIfAbsent(dogId, new CopyOnWriteArraySet<>());
        webSocketMap.get(dogId).add(this);

        log.info("WebSocket连接建立: dogId={}, sessionId={}, 当前连接数={}",
                dogId, session.getId(), webSocketMap.get(dogId).size());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (dogId != null && webSocketMap.containsKey(dogId)) {
            webSocketMap.get(dogId).remove(this);
            if (webSocketMap.get(dogId).isEmpty()) {
                webSocketMap.remove(dogId);
            }
        }
        log.info("WebSocket连接关闭: dogId={}, sessionId={}", dogId, session.getId());
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.debug("收到客户端消息: dogId={}, message={}", dogId, message);
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket发生错误: dogId={}, sessionId={}", dogId, session.getId(), error);
    }

    /**
     * 发送消息
     */
    private void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息失败: dogId={}", dogId, e);
        }
    }

    /**
     * 向指定dogId的所有客户端推送消息
     */
    public static void pushToClients(String dogId, Object data) {
        if (webSocketMap.containsKey(dogId)) {
            try {
                String message = objectMapper.writeValueAsString(data);
                CopyOnWriteArraySet<RealtimeDataWebSocket> webSockets = webSocketMap.get(dogId);

                for (RealtimeDataWebSocket webSocket : webSockets) {
                    webSocket.sendMessage(message);
                }

                log.debug("推送数据到客户端: dogId={}, 连接数={}", dogId, webSockets.size());
            } catch (Exception e) {
                log.error("推送数据失败: dogId={}", dogId, e);
            }
        }
    }

    /**
     * 广播消息到所有客户端
     */
    public static void broadcast(Object data) {
        try {
            String message = objectMapper.writeValueAsString(data);
            webSocketMap.forEach((dogId, webSockets) -> {
                for (RealtimeDataWebSocket webSocket : webSockets) {
                    webSocket.sendMessage(message);
                }
            });
        } catch (Exception e) {
            log.error("广播消息失败", e);
        }
    }

    /**
     * 获取在线连接数
     */
    public static int getOnlineCount(String dogId) {
        if (webSocketMap.containsKey(dogId)) {
            return webSocketMap.get(dogId).size();
        }
        return 0;
    }
}


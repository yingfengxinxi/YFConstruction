package com.qdoner.message.common;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kakarote.core.message.MessageEntity;
import com.qdoner.message.entity.PO.Queues;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Slf4j
@Component
public class WebSocketService extends AbstractWebSocketHandler {

    /**
     * 存储sessionId和webSocketSession
     * 需要注意的是，webSocketSession没有提供无参构造，不能进行序列化，也就不能通过redis存储
     * 在分布式系统中，要想别的办法实现webSocketSession共享
     */
    private static Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    /**
     * webSocket连接创建后调用
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // 获取参数
        String key = String.valueOf(session.getAttributes().get("WS-Key"));
        sessionMap.put(key, session);
        log.info("WebSocket：" + key + " 已连接，当前连接数 => " + sessionMap.size());
    }

    /**
     * 接收到消息会调用  未启用
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {

        } else if (message instanceof BinaryMessage) {

        } else if (message instanceof PongMessage) {

        } else {
            System.out.println("Unexpected WebSocket message type: " + message);
        }
    }

    /**
     * 连接出错会调用
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        String key = String.valueOf(session.getAttributes().get("key"));
        sessionMap.remove(key);
        log.error("WebSocket：连接出错 => " + exception.getMessage());
    }

    /**
     * 连接关闭会调用
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String key = String.valueOf(session.getAttributes().get("WS-Key"));
        sessionMap.remove(key);
        log.info("WebSocket：" + key + " 已断开连接，当前连接数 => " + sessionMap.size());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 判断 指定方式  推送范围
     *
     * @param entity
     * @param queues
     */
    public void send(MessageEntity entity, Queues queues) {
        /*if ("0".equals(queues.getTargetConfigType())) {//全部
            sendAll(entity, queues);
            return;
        }
        if ("1".equals(queues.getTargetConfigType())) {//指定
            sendPoint(entity, queues);
            return;
        }
        if ("2".equals(queues.getTargetConfigType())) {//半指定
            sendPointOrAll(entity, queues);
            return;
        }*/
        sendPoint(entity, queues);
    }

    /**
     * 半指定
     *
     * @param entity
     * @param queues
     */
    private void sendPointOrAll(MessageEntity entity, Queues queues) {
        List<Long> users = entity.getToUsers();
        if (users == null) {
            sendAll(entity, queues);
        } else {
            sendPoint(entity, queues);
        }
    }

    /**
     * 全部
     *
     * @param entity
     * @param queues
     */
    private void sendAll(MessageEntity entity, Queues queues) {
        String[] targets = queues.getTargets().split(",");
        for (String target : targets) {
            sendLikeKey(target + "-", entity.toString());
        }
    }

    /**
     * 指定
     *
     * @param entity
     * @param queues
     */
    private void sendPoint(MessageEntity entity, Queues queues) {
        List<Long> users = entity.getToUsers();
        if (users == null) {
            log.error("MessageCenter:", "指定的接收对象不得为空 => " + entity.toString());
            return;
        }
        pushMessageToUsers(entity, users, queues);
    }


    public void pushMessageToUsers(MessageEntity entity, List<Long> userIds, Queues queues) {
        String[] targets = queues.getTargets().split(",");
        for (Long userId : userIds) {
            for (String target : targets) {
                sendLikeKey(target + "-" + String.valueOf(userId), entity.toString());
            }

        }
    }

    /**
     * 发送用户
     * 包含key
     *
     * @param likeKey
     * @param msg
     */
    private void sendLikeKey(String likeKey, String msg) {
        for (Map.Entry<String, WebSocketSession> entry : sessionMap.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(likeKey + "-")) {
                sendUser(key, msg);
            }
        }

    }

    /**
     * 后端发送消息 包含则发送
     */
    private void sendUser(String key, String message) {
        WebSocketSession session = sessionMap.get(key);
        try {
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


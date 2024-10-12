package com.qdoner.message.common;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.message.MessageEntity;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.utils.UserUtil;
import com.qdoner.message.entity.PO.Queues;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SocketIOService {

    /**
     * 存放已连接的客户端
     */
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private Redis redis;

    /**
     * Spring IoC容器创建之后，在加载SocketIOServiceImpl Bean之后启动
     */
    @PostConstruct
    private void autoStartup() {
        start();
    }

    /**
     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,避免重启项目服务端口占用问题
     */
    @PreDestroy
    private void autoStop() {
        stop();
    }

    public void start() {
        // 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            String wskey = getParamsByClient(client);
            if (wskey != null) {
                clientMap.put(wskey, client);
                log.info("SocketIO：" + wskey + " 已连接，当前连接数 => " + clientMap.size());
            }
        });

        // 监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            String wskey = getParamsByClient(client);
            if (wskey != null) {
                clientMap.remove(wskey);
                client.disconnect();
                log.info("SocketIO：" + wskey + "已断开连接，当前连接数 => " + clientMap.size());
            }
        });

        // 启动服务
        socketIOServer.start();
    }

    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    /**
     * 判断 指定方式  推送范围
     *
     * @param entity
     * @param queues
     */
    public void send(MessageEntity entity, Queues queues) {
        //todo 错误
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
        //2022 12 12 接收目标交由业务系统处理
        sendPoint(entity, queues);
    }

    /*private void sendPointOrAll(MessageEntity entity, Queues queues) {
        List<Long> users = entity.getToUsers();
        if (users == null) {
            sendAll(entity, queues);
        } else {
            sendPoint(entity, queues);
        }
    }

    private void sendAll(MessageEntity entity, Queues queues) {
        String[] targets = queues.getTargets().split(",");
        for (String target : targets) {
            sendLikeKey(queues.getCode(), target, entity.toString());
        }
    }*/

    private void sendPoint(MessageEntity entity, Queues queues) {
        List<Long> users = entity.getToUsers();
        if (users == null) {
            log.error("MessageCenter:", "接收对象不得为空 => " + entity.toString());
            return;
        }
        pushMessageToUsers(queues, entity, users);
    }


    public void pushMessageToUsers(Queues queues, MessageEntity entity, List<Long> userIds) {
        String[] targets = queues.getTargets().split(",");
        for (Long userId : userIds) {
            for (String target : targets) {
                sendLikeKey(queues.getCode(), target + "-" + String.valueOf(userId), entity.toString());
            }
        }
    }

    /**
     * 全体广播  未使用
     *
     * @param eventName
     * @param msgContent
     */
    public void pushMessageByBroadcast(String eventName, String msgContent) {
        socketIOServer.getBroadcastOperations().sendEvent(eventName, msgContent);
    }

    /**
     * 发送用户
     *
     * @param likeKey 端
     * @param msg
     */
    private void sendLikeKey(String eventName, String likeKey, String msg) {
        for (Map.Entry<String, SocketIOClient> entry : clientMap.entrySet()) {
            String key = entry.getKey();
            //端+userId
            if (key.startsWith(likeKey + "-")) {
                SocketIOClient client = clientMap.get(key);
                client.sendEvent(eventName, msg);
            }
        }

    }

    /**
     * 获取客户端key参数
     *
     * @param client: 客户端
     * @return: java.lang.String
     */
    private String getParamsByClient(SocketIOClient client) {
        String target = client.getHandshakeData().getSingleUrlParam("target");
        if (target == null || StringUtils.isEmpty(target.toString())) {
            throw new CrmException(SystemCodeEnum.NO_SOCKET_TARGET);
        }
        List<String> cookies = client.getHandshakeData().getHeaders().get("Cookie");
        String token = "";
        if (cookies != null && cookies.size() > 0) {
            String[] args = cookies.get(0).split(";");
            for (String c : args) {
                c = c.trim();
                if(c.startsWith("Admin-Token=")){
                    token = c.replace("Admin-Token=","");
                }
            }
        }
        UserInfo userInfo = redis.get(token);
        if (userInfo == null) {
            throw new CrmException(SystemCodeEnum.NO_SOCKET_TARGET);
        }
        String key = target + "-" + userInfo.getUserId() + "-" + client.getSessionId();
        return key;
    }
}

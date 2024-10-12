package com.qdoner.message.common;

import com.kakarote.core.common.Const;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.redis.Redis;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.UUID;

public class WebSocketInterceptor implements HandshakeInterceptor {

    @Autowired
    private Redis redis;
    /**
     * handler处理前调用,attributes属性最终在WebSocketSession里,可能通过webSocketSession.getAttributes().get(key值)获得
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse serverHttpResponse, org.springframework.web.socket.WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
            // 组装唯一标识
            Object target = serverHttpRequest.getServletRequest().getParameter("target");
            if (target == null || StringUtils.isEmpty(target.toString())) {
                throw new CrmException(SystemCodeEnum.NO_SOCKET_TARGET);
            }
            String token = serverHttpRequest.getHeaders().getFirst(Const.TOKEN_NAME);
            UserInfo userInfo = redis.get(token);
            if (userInfo == null) {
                throw new CrmException(SystemCodeEnum.NO_SOCKET_TARGET);
            }
            String key = target.toString() + "-" + userInfo.getUserId() + "-" + UUID.randomUUID();
            map.put("WS-Key", key);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, org.springframework.web.socket.WebSocketHandler webSocketHandler, Exception e) {

    }
}

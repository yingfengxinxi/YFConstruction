package com.qdoner.message.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Result;
import com.kakarote.core.message.MessageEntity;

import java.util.Map;


/**
 * 消息发送业务处理
 */
public interface IMService {
    /**
     * 公告
     * @return
     */
    Result send(MessageEntity messageEntity);

    Result sendTRtc(JSONObject data);

    Result registerCid(Map<String,String> map);

}

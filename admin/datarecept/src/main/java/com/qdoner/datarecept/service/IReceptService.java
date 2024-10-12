package com.qdoner.datarecept.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Result;

public interface IReceptService {

    Result receptHttp(String secret, String ip,JSONObject object);

    /**
     * 设备报文
     * @return 响应信息
     */
    String receptMsg(String data,String ip,int port);
    /**
    * @Description:回复报文
    * @author shz
    * @date 2022-9-29 10:44
    * @param 
    * @return 
    */
    String replyMsg(String data);
}

package com.kakarote.build.wxapp.common;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.wxapp.util.WxHttpsUtil;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.utils.BaseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * AccessToken 服务类
 */
@Service
public class AccessTokenSerive {

    @Autowired
    private WxHttpsUtil wxHttpsUtil;

    @Autowired
    private Redis redis;

    private String WXAPP_TOKEN_KEY = "WXAPP_ACCESS_TOKEN";

    private int expires_in = 7200;//超时时间 单位秒

    /**
     * 重启刷新token 避免系统错误造成的token失效
     */
    //@PostConstruct
    public void refreshToken() {
        getAccessToken();
    }

    /**
     * 获取token
     *
     * @return json
     * access_token    string 	获取到的凭证
     * expires_in 	number 	凭证有效时间，单位：秒。目前是7200秒之内的值。
     * errcode 	number 	错误码
     * errmsg 	string 	错误信息
     */
    public String getAccessToken() {
        String token = redis.get(WXAPP_TOKEN_KEY);
        if (StringUtils.isEmpty(token)) {//token失效
            String str = HttpUtil.get(wxHttpsUtil.getAccessToken());
            JSONObject jsonObject = JSONObject.parseObject(str);
            Object errObject = jsonObject.get("errcode");
            if (null != errObject) {
                Integer errcode = Integer.valueOf(errObject.toString());
                if(0 != errcode){
                    throw new RuntimeException(jsonObject.toJSONString());
                }
            }else{
                expires_in = jsonObject.getInteger("expires_in");
                redis.setex(WXAPP_TOKEN_KEY, expires_in, jsonObject.getString("access_token"));
                //
                token = jsonObject.getString("access_token");
            }
        }
        return token;
    }
}

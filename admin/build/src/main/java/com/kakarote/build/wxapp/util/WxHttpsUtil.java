package com.kakarote.build.wxapp.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 微信小程序 https组装工具
 * 2022.03.10 wdw
 */
@Configuration
public class WxHttpsUtil {

    @Value("${wx.wxapp.AppID}")
    private String AppID;

    @Value("${wx.wxapp.AppSecret}")
    private String AppSecret;

    public String code2Session(String jsCode) {
        return "https://api.weixin.qq.com/sns/jscode2session?appid=" + AppID + "&secret=" + AppSecret + "&js_code=" + jsCode + "&grant_type=authorization_code";
    }

    public String getAccessToken() {
        return "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + AppID + "&secret=" + AppSecret;
    }
}

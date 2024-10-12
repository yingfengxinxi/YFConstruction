package com.kakarote.build.wxapp.common;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.wxapp.util.WxHttpsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信小程序服务类
 * 2022.03.10 wdw
 */
@Service
public class WxApiService {

    @Autowired
    private WxHttpsUtil wxHttpsUtil;
    /**
     * 通过jsCode换取openid
     * @return
     */
    public JSONObject getOpenIDByJsCode(String jsCode){
        String str = HttpUtil.get(wxHttpsUtil.code2Session(jsCode));
        JSONObject obj = JSONObject.parseObject(str);
        return obj;
    }
}

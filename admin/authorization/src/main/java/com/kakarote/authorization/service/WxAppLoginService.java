package com.kakarote.authorization.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Result;

public interface WxAppLoginService {

    /**
     * 绑定
     * @param wxBind
     * @return
     */
    Result bind(JSONObject wxBind);

    /**
     * 自动登录
     * @param code
     * @return
     */
    Result login(String code);
}

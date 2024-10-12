package com.kakarote.authorization.controller;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.authorization.service.WxAppLoginService;
import com.kakarote.core.common.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * wxapp入口
 */
@RestController
@RequestMapping("/wxLogin")
public class WxAppController {

    @Autowired
    private WxAppLoginService wxAppLoginService;

    /**
     * 绑定
     * @return
     */
    @PostMapping(value = "/bind/{jsCode}")
    public Result doBind(@PathVariable String jsCode, @RequestBody JSONObject bindData){
        bindData.put("jsCode",jsCode);
        return wxAppLoginService.bind(bindData);
    }

    /**
     * 自动登录
     * @param jsCode
     * @return
     */
    @PostMapping(value = "/{jsCode}")
    public Result doLogin(@PathVariable String jsCode){
        return wxAppLoginService.login(jsCode);
    }
}

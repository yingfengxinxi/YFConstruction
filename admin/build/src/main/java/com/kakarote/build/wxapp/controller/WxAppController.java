package com.kakarote.build.wxapp.controller;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.wxapp.service.IBWxuserBindService;
import com.kakarote.build.wxapp.service.WxAppService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 微信app前端入口
 */
@RestController
@RequestMapping("/wxapp")
public class WxAppController {
    @Autowired
    private WxAppService wxAppService;

    @Autowired
    private IBWxuserBindService wxuserBindService;
    @PostMapping("")
    public Result onBind(@RequestBody JSONObject bindData){
        Result result = wxAppService.bind(bindData);
        return result;
    }

    @PostMapping("/{jsCode}")
    public Result onLogion(@PathVariable String jsCode){
        return wxAppService.login(jsCode);
    }

    @GetMapping("/layout")
    public Result layout(){
        UserInfo userInfo = UserUtil.getUser();
        String openId = userInfo.getWxappOpenId();
        wxuserBindService.removeById(openId);
        UserUtil.removeUser();
        return Result.ok();
    }
}

package com.kakarote.authorization.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "build",contextId = "buildWxApp")
public interface WxAppService {

    @PostMapping("/wxapp/{jsCode}")
    Result onLogion(@PathVariable("jsCode") String jsCode);

    @PostMapping("/wxapp")
    Result onBind(@RequestBody JSONObject bindData);
}

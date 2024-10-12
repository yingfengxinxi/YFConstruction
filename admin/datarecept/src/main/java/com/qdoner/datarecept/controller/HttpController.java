package com.qdoner.datarecept.controller;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Result;
import com.qdoner.datarecept.service.IReceptService;
import com.qdoner.datarecept.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


/**
 * 对接上位机平台
 */
@Slf4j
@RestController
@RequestMapping("/h")
public class HttpController {

    @Autowired
    private IReceptService receptService;

    /**
     * 接收入口
     * 数据需为 JSON格式
     * @param secret 设备密钥
     * @param object 设备数据
     * @return
     */
    @PostMapping("/{secret}/recept")
    public Result recept(HttpServletRequest request,@PathVariable String secret, @RequestBody JSONObject object) {
        String ip = IPUtil.getIpAddr(request);
        return receptService.receptHttp(secret,ip, object);
    }
}

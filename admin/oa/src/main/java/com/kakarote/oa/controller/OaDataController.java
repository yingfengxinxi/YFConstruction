package com.kakarote.oa.controller;

import com.kakarote.core.common.Result;
import com.kakarote.oa.service.IOaDataService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OA数据格式化接口
 */
@RestController
@RequestMapping("/oadata")
@Api(value = "OA数据初始化接口")
public class OaDataController {

    @Autowired
    private IOaDataService oaDataService;

    /**
     * OA数据初始化
     * @param tenantId
     * @return
     */
    @GetMapping("/initData/{tenantId}")
    public Result initData(@PathVariable Integer tenantId){
        return oaDataService.initData(tenantId);
    }
}

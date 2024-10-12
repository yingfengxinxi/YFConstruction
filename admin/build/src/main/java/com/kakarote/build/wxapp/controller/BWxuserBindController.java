package com.kakarote.build.wxapp.controller;


import com.kakarote.build.wxapp.entity.BO.BWxuserBindBO;
import com.kakarote.build.wxapp.service.IBWxuserBindService;
import com.kakarote.core.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 微信用户绑定表
 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2022-03-10
 */
@RestController
@RequestMapping("/bWxuserBind")
public class BWxuserBindController {
    @Autowired
    private IBWxuserBindService wxuserBindService;
    /**
     * 分页列表
     */
    @PostMapping("/list")
    public Result list(@RequestBody BWxuserBindBO wxuserBindBO) {
        return Result.ok(wxuserBindService.selectByPage(wxuserBindBO));
    }



}


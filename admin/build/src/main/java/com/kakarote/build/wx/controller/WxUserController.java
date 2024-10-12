package com.kakarote.build.wx.controller;


import com.kakarote.build.wx.entity.PO.WxUser;
import com.kakarote.build.wx.service.IWxUserService;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shz
 * @since 2021-07-28
 */
@RestController
@RequestMapping("/wxUser")
public class WxUserController {

    @Autowired
    private IWxUserService iWxUserService;

    @PostMapping("/save")
    @ApiOperation("添加")
    public Result add(@RequestBody WxUser wxUser){
        return Result.ok(iWxUserService.add(wxUser));
    }

}


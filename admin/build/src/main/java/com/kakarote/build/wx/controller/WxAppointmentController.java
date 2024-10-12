package com.kakarote.build.wx.controller;


import com.kakarote.build.wx.entity.BO.WxAppointmentBO;
import com.kakarote.build.wx.entity.PO.WxAppointment;
import com.kakarote.build.wx.service.IWxAppointmentService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 业主预约 前端控制器
 * </p>
 *
 * @author shz
 * @since 2021-07-15
 */
@RestController
@RequestMapping("/wxAppointment")
public class WxAppointmentController {

    @Autowired
    private IWxAppointmentService iWxAppointmentService;

    @PostMapping("/save")
    @ApiOperation("添加")
    public Result save(@RequestBody WxAppointment wxAppointment) {
        iWxAppointmentService.add(wxAppointment);
        return Result.ok();
    }

    @PostMapping("/wxSelectByPage")
    @ApiOperation("查询")
    public Result wxSelectByPage(@RequestBody WxAppointmentBO wxAppointment) {
        return Result.ok(iWxAppointmentService.wxSelectByPage(wxAppointment));
    }

    @PostMapping("/wxGetById")
    @ApiOperation("查询")
    public Result wxGetById(@RequestBody WxAppointmentBO wxAppointment){
        return Result.ok(iWxAppointmentService.wxGetById(wxAppointment.getId()));
    }
}


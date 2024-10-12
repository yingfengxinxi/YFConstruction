package com.kakarote.build.qual.controller;


import com.kakarote.build.qual.service.IQualMonitBtbZbService;
import com.kakarote.core.common.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 标养室监测--监测数据填报--子表 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-03-25
 */
@RestController
@RequestMapping("/buildQualMonitBtbZb")
public class QualMonitBtbZbController {

    @Autowired
    private IQualMonitBtbZbService iQualMonitBtbZbService;

    @PostMapping("/queryByZHUId/{valveId}")
    @ApiOperation("根据主表主键查询信息")
    @OperateLog(detail = "根据主表主键查询信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryByZHUId(@PathVariable @NotNull Integer valveId){
        return R.ok(iQualMonitBtbZbService.selectT(valveId));
    }

}


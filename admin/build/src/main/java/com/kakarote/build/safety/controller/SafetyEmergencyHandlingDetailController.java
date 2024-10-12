package com.kakarote.build.safety.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.safety.entity.BO.SafetyEmergencyHandlingBO;
import com.kakarote.build.safety.entity.BO.SafetyEmergencyHandlingDetailBO;
import com.kakarote.build.safety.entity.PO.SafetyEmergencyHandling;
import com.kakarote.build.safety.entity.PO.SafetyEmergencyHandlingDetail;
import com.kakarote.build.safety.service.ISafetyEmergencyHandlingDetailService;
import com.kakarote.build.safety.service.ISafetyEmergencyHandlingService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 应急事件处置明细 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-04-06
 */
@RestController
@RequestMapping("/buildSafetyEmergencyHandlingDetail")
public class SafetyEmergencyHandlingDetailController {

    @Autowired
    private ISafetyEmergencyHandlingDetailService iSafetyEmergencyHandlingDetailService;


    @PostMapping("/list")
    @ApiOperation("查询信息列表")
    public Result<BasePage<SafetyEmergencyHandlingDetail>> list(@RequestBody SafetyEmergencyHandlingDetailBO safetyEmergencyHandlingDetailBO) {
        BasePage<SafetyEmergencyHandlingDetail> list = iSafetyEmergencyHandlingDetailService.selectList(safetyEmergencyHandlingDetailBO);
        return Result.ok(list);
    }

    @PostMapping("/queryById/{valveId}")
    @ApiOperation("根据主键查询信息")
    public Result queryById(@PathVariable @NotNull String valveId){
        return R.ok(iSafetyEmergencyHandlingDetailService.getById(valveId));
    }

    @PostMapping("/save")
    @ApiOperation("新增信息")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result save(@Validated @RequestBody SafetyEmergencyHandlingDetail safetyEmergencyHandlingDetail) {
        return Result.ok(iSafetyEmergencyHandlingDetailService.save(safetyEmergencyHandlingDetail));
    }

    @PostMapping("/updateById")
    @ApiOperation("修改信息")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result updateById(@Validated @RequestBody SafetyEmergencyHandlingDetail safetyEmergencyHandlingDetail) {
        return Result.ok(iSafetyEmergencyHandlingDetailService.updateById(safetyEmergencyHandlingDetail));
    }

    @PostMapping("/delTrueByIds")
    @ApiOperation("删除信息且删除详情信息(根据pid)")
    @Transactional
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            iSafetyEmergencyHandlingDetailService.removeById(id);
        }
        return Result.ok();
    }

//    @PostMapping("/queryByPid/{valveId}")
//    @ApiOperation("根据pid查询信息")
//    public Result queryByPid(@PathVariable @NotNull Integer valveId){
//        return R.ok(iSafetyEmergencyHandlingDetailService.getById(valveId));
//    }
}


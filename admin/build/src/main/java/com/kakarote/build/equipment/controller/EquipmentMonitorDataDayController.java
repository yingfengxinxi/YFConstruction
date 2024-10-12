package com.kakarote.build.equipment.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.equipment.service.IEquipmentMonitorDataDayService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-监测项日数据表 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-10-28
 */
@RestController
@RequestMapping("/eqMonDataDay")
public class EquipmentMonitorDataDayController {

    @Autowired
    private IEquipmentMonitorDataDayService dataDayService;

    @GetMapping("/getDayDataForPit/{proId}")
    @ApiOperation("查询根据工程获取基坑小时数据 折线图")
    @OperateLog(detail = "查询根据工程获取基坑小时数据 折线图", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<JSONObject> getDayDataForPit(@PathVariable Integer proId) {
        return Result.ok(dataDayService.getDayDataByPoint(proId));
    }

    /**
     * 三维大屏使用
     * @param equipmentId
     * @return
     */
    @GetMapping("/getDayDataByEquiId/{equipmentId}")
    @ApiOperation("查询根据设备获取小时数据 折线图")
    @OperateLog(detail = "查询根据设备获取小时数据 折线图", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<JSONObject> getDayDataByEquiId(@PathVariable Integer equipmentId) {
        return Result.ok(dataDayService.getDayDataByEquiId(equipmentId));
    }
}


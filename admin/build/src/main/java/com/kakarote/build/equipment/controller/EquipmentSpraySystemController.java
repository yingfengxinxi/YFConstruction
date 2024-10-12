package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.BO.EquipmentSpraySystemBO;
import com.kakarote.build.equipment.entity.PO.EquipmentSpraySystem;
import com.kakarote.build.equipment.service.IEquipmentSpraySystemService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自动喷淋系统 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-06-01
 */
@RestController
@RequestMapping("/equipmentSpraySystem")
public class EquipmentSpraySystemController {

    @Autowired
    private IEquipmentSpraySystemService spraySystemService;

    @PostMapping("/list")
    @ApiOperation("查询计划执行列表")
    @OperateLog(detail = "查询计划执行列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<EquipmentSpraySystem>> list(@RequestBody EquipmentSpraySystemBO spraySystemBO) {
        return Result.ok(spraySystemService.selectByPage(spraySystemBO));
    }

    @PostMapping("/listOperation")
    @ApiOperation("查询喷淋操作记录列表")
    @OperateLog(detail = "查询喷淋操作记录列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<EquipmentSpraySystem>> listOperation(@RequestBody EquipmentSpraySystemBO spraySystemBO) {
        return Result.ok(spraySystemService.selectByPageOperation(spraySystemBO));
    }

    /**
     * equipmentId：设备id；必填
     * operationType：操作类型 1：启动 0：关闭; 必填
     * @param map
     * @return
     */
    @PostMapping("/spraySwitch")
    @ApiOperation("app自动喷淋系统-单个设备开关")
    @OperateLog(detail = "app自动喷淋系统-单个设备开关", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result spraySwitch(@RequestBody Map<String, Object> map) {
        return Result.ok(spraySystemService.spraySwitch(map));
    }

    /**
     * isSwitch 操作类型 1：启动 0：关闭; 必填
     * @param isSwitch
     * @return
     */
    @PostMapping("/spraySwitch/{isSwitch}")
    @ApiOperation("app自动喷淋系统-全开或全关")
    @OperateLog(detail = "app自动喷淋系统-全开或全关", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result sprayUnifiedSwitch(@PathVariable String isSwitch) {
        return Result.ok(spraySystemService.sprayUnifiedSwitch(isSwitch));
    }


    /**
     * 喷淋设备在线率分析
     * @return
     */
    @PostMapping("/selectPieData")
    @ApiOperation("喷淋设备在线率分析--饼状图")
    @OperateLog(detail = "喷淋设备在线率分析--饼状图", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectPieData(){
        List<Map<String, Object>> list = spraySystemService.selectPieData();
        return R.ok(list);
    }


    /**
     * 项目扬尘报警数量统计数量统计

     * @return
     */
    @PostMapping("/selectBarData")
    @ApiOperation("项目扬尘报警数量统计--柱状图")
    @OperateLog(detail = "项目扬尘报警数量统计--柱状图", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectBarData(){
        List<Map<String, Object>> list = spraySystemService.selectBarData();
        return R.ok(list);
    }

    /**
     * 设备开启时段分析数量统计

     * @return
     */
    @PostMapping("/selectKqsdfxData")
    @ApiOperation("设备开启时段分析--柱状图")
    @OperateLog(detail = "设备开启时段分析--柱状图", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectKqsdfxData(){
        List<Map<String, Object>> list = spraySystemService.selectKqsdfxData();
        return R.ok(list);
    }

    /**
     * 喷淋时长分析数量统计

     * @return
     */
    @PostMapping("/selectPlscfxData")
    @ApiOperation("喷淋时长--柱状图、折线图")
    @OperateLog(detail = "喷淋时长--柱状图、折线图", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectPlscfxData(){
        List<Map<String, Object>> list = spraySystemService.selectPlscfxData();
        return R.ok(list);
    }

}


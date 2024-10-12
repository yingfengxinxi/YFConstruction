package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.BO.EquipmentPitReportBO;
import com.kakarote.build.equipment.service.IEquipmentPitReportService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.qdoner.build.equipment.entity.PO.EquipmentPitReport;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shz
 * @since 2023-05-07
 */
@RestController
@RequestMapping("/equipmentPitReport")
public class EquipmentPitReportController {

    @Autowired
    private IEquipmentPitReportService reportService;

    @PostMapping("/selectPage")
    @ApiOperation("查询基坑监测报告列表")
    @OperateLog(detail = "查询基坑监测报告列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<EquipmentPitReport>> selectPage(@RequestBody EquipmentPitReportBO pitReportBO) {
        return Result.ok(reportService.selectPage(pitReportBO));
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("查询基坑监测报告详情")
    @OperateLog(detail = "查询基坑监测报告详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable Integer id){
        return Result.ok(reportService.getById(id));
    }

    @PostMapping("/save")
    @ApiOperation("更新基坑监测报告列表")
    @OperateLog(detail = "更新基坑监测报告列表", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result save(@RequestBody EquipmentPitReport equipmentPitReport){
        return Result.ok(reportService.saveOrUpdate(equipmentPitReport));
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("查询基坑监测报告详情")
    @OperateLog(detail = "查询基坑监测报告详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result delete(@PathVariable Integer id){
        return Result.ok(reportService.removeById(id));
    }
}


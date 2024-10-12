package com.kakarote.build.safety.controller;


import com.kakarote.build.safety.entity.BO.SafetyInspectionInfoBO;
import com.kakarote.build.safety.entity.BO.SafetyInspectionPlanBO;
import com.kakarote.build.safety.entity.PO.SafetyInspectionInfo;
import com.kakarote.build.safety.entity.PO.SafetyInspectionPlan;
import com.kakarote.build.safety.service.ISafetyInspectionInfoService;
import com.kakarote.build.utils.DateUtil;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 施工安全--安全巡检信息 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-04-08
 */
@RestController
@RequestMapping("/safetyInspectionInfo")
public class SafetyInspectionInfoController {

    @Autowired
    private ISafetyInspectionInfoService inspectionInfoService;

    @PostMapping("/list")
    @ApiOperation("查询安全巡检信息列表")
    @OperateLog(detail = "查询安全巡检信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<SafetyInspectionInfo>> list(@RequestBody SafetyInspectionInfoBO inspectionInfoBO) {
        return Result.ok(inspectionInfoService.selectByPage(inspectionInfoBO));
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("根据Id查询巡检信息")
    @OperateLog(detail = "根据Id查询巡检信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable @NotNull Integer id) {
        Map<String, Object> data = inspectionInfoService.selectDataById(id);
        return Result.ok(data);
    }

    @PostMapping("/add")
    @ApiOperation("新增安全巡检数据")
    @OperateLog(detail = "新增安全巡检数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getPrintData(@RequestBody SafetyInspectionInfo inspectionInfo) {
        inspectionInfo.setExecuteDate(DateUtil.parseDate(new Date()));
        inspectionInfoService.saveOrUpdate(inspectionInfo);
        return Result.ok();
    }

    @PostMapping("/update")
    @ApiOperation("执行或复检巡查信息")
    @OperateLog(detail = "执行或复检巡查信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result update(@RequestBody Map<String, Object> params) {
        return Result.ok(inspectionInfoService.updateInspectionInfo(params));
    }

    @PostMapping("/getPrintData/{id}")
    @ApiOperation("查询整改通知单数据")
    @OperateLog(detail = "查询整改通知单数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getPrintData(@PathVariable @NotNull Integer id) {
        Map<String, Object> data = inspectionInfoService.getPrintData(id);
        return Result.ok(data);
    }

}


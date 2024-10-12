package com.kakarote.build.labour.controller;


import com.kakarote.build.labour.service.IBLabourInspectionCheckService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * <p>
 * 企业报验检查表 前端控制器
 * </p>
 *
 * @author zhang
 * @since 2022-11-30
 */
@RestController
@RequestMapping("/bLabourInspectionCheck")
public class BLabourInspectionCheckController {

    @Autowired
    private IBLabourInspectionCheckService inspectionCheckService;

    /**
     * 根据报验id查询检查数据
     * @param inspectionId
     * @return
     */
    @PostMapping("/selectByInspectionId/{inspectionId}")
    @ApiOperation("查询检查数据")
    @OperateLog(detail = "查询检查数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectByInspectionId(@PathVariable @NotNull String inspectionId) {
        Map<String, Object> data = inspectionCheckService.selectByInspectionId(inspectionId);
        return Result.ok(data);
    }


    /**
     * 分配和调整方法
     * @return
     */
    @PostMapping("/saveCheckData")
    @ApiOperation("保存检查数据")
    @OperateLog(detail = "保存检查数据", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result insertOrupdateData(@RequestBody Map<String, Object> params) {
        inspectionCheckService.saveCheckData(params);
        return Result.ok();
    }



}


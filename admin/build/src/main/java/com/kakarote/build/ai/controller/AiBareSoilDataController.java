package com.kakarote.build.ai.controller;


import com.kakarote.build.ai.entity.BO.AiBareSoilDataBO;
import com.kakarote.build.ai.entity.PO.AiBareSoilData;
import com.kakarote.build.ai.service.IAiBareSoilDataService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 裸土报警数据 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2022-01-06
 */
@RestController
@RequestMapping("/aiBareSoilData")
public class AiBareSoilDataController {

    @Autowired
    private IAiBareSoilDataService aiBareSoilDataService;

    @PostMapping("/selectByPage")
    @ApiOperation("查询裸土分析报警数据")
    @OperateLog(detail = "查询裸土分析报警数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<AiBareSoilData>> selectByPage(@RequestBody AiBareSoilDataBO aiBareSoilDataBO){
        return Result.ok(aiBareSoilDataService.selectByPage(aiBareSoilDataBO));
    }


    /**
     * 获取详情
     */

    @GetMapping("/selectById/{id}")
    public Result getById(@PathVariable String id) {
        return Result.ok(aiBareSoilDataService.getById(id));
    }

    /**
     * 该接口宁津相关项目使用
     *
     * @return
     */
    @PostMapping("/selectAlertNumber")
    @ApiOperation("智能分析-报警数量")
    @OperateLog(detail = "智能分析-报警数量", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>> selectAlertNumber() {
        return Result.ok(aiBareSoilDataService.selectAlertNumber());
    }

    /**
     * 该接口宁津相关项目使用
     *
     * @return
     */
    @PostMapping("/selectAlertTrend")
    @ApiOperation("智能分析-报警趋势")
    @OperateLog(detail = "智能分析-报警趋势", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectAlertTrend(@RequestBody Map<String, Object> params) {
        return Result.ok(aiBareSoilDataService.selectAlertTrend(params));
    }

    /**
     * 该接口宁津相关项目使用
     *
     * @return
     */
    @PostMapping("/selectProjectAlertNumber")
    @ApiOperation("智能分析-报警趋势")
    @OperateLog(detail = "智能分析-报警趋势", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectProjectAlertNumber() {
        return Result.ok(aiBareSoilDataService.selectProjectAlertNumber());
    }

    /**
     * 该接口宁津相关项目使用
     *
     * @return
     */
    @PostMapping("/selectPageNj")
    @ApiOperation("智能分析-分页查询报警列表")
    @OperateLog(detail = "智能分析-分页查询报警列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String, Object>>> selectPageNj(@RequestBody AiBareSoilDataBO aiBareSoilDataBO) {
        return Result.ok(aiBareSoilDataService.selectPageNj(aiBareSoilDataBO));
    }

    /**
     * 该接口宁津相关项目使用
     *
     * @return
     */
    @PostMapping("/selectTypeProportion")
    @ApiOperation("智能分析-裸土覆盖报警分析报警类型占比")
    @OperateLog(detail = "报警数据分析-裸土覆盖报警分析报警类型占比", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectTypeProportion() {
        return Result.ok(aiBareSoilDataService.selectTypeProportion());
    }
}


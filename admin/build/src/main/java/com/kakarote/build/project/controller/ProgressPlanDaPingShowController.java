package com.kakarote.build.project.controller;

import com.kakarote.build.project.service.ProgressPlanDaPingShowService;
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

import java.util.List;
import java.util.Map;

/**
 * @Title: ProgressPlanDaPingShowController
 * @Package com.kakarote.build.project.controller
 * @Description: 大屏展示计划进度
 * @author: ZangYu
 * @date: 2021-07-14 16:16
 */
@RestController
@RequestMapping("/progressPlanDaPingShow")
public class ProgressPlanDaPingShowController {

    @Autowired
    private ProgressPlanDaPingShowService progressPlanDaPingShowService;

    @PostMapping("/selectMapForDaPingShowProgress")
    @ApiOperation("大屏展示--进度--总计划")
    @OperateLog(detail = "大屏展示--进度--总计划", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectMapForDaPingShowProgress(@RequestBody Map<String,Object> map) throws Exception {

        Map<String, Object> stringObjectMap = progressPlanDaPingShowService.selectMapForDaPingShowProgress(map);
        return Result.ok(stringObjectMap);
    }

    @PostMapping("/selectMapForDaPingShowProgressMonth")
    @ApiOperation("大屏展示--进度--月计划")
    @OperateLog(detail = "大屏展示--进度--月计划", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectMapForDaPingShowProgressMonth(@RequestBody Map<String,Object> map) throws Exception {

        Map<String, Object> stringObjectMap = progressPlanDaPingShowService.selectMapForDaPingShowProgressMonth(map);
        return Result.ok(stringObjectMap);
    }

    @PostMapping("/selectByMapForDaPing")
    @ApiOperation("大屏展示--进度--每日填报")
    @OperateLog(detail = "大屏展示--进度--每日填报", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectByMapForDaPing(@RequestBody Map<String,Object> map) {

        List<Map> list = progressPlanDaPingShowService.selectByMapForDaPing(map);
        return Result.ok(list);
    }

    /**
     * 该接口三维大屏使用
     * @param params
     * @return
     */
    @PostMapping("/selectPlanProjectDuration")
    @ApiOperation("总计划 计划工期")
    @OperateLog(detail = "总计划 计划工期", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectPlanProjectDuration(@RequestBody Map<String,Object> params) {

        Map<String, Object> map = progressPlanDaPingShowService.selectPlanProjectDuration(params);
        return Result.ok(map);
    }

    /**
     *
     * @param params
     * @return
     */
    @PostMapping("/selectPlanProgressList")
    @ApiOperation("三维大屏展示--进度--总计划")
    @OperateLog(detail = "三维大屏展示--进度--总计划", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectPlanProgressList(@RequestBody Map<String,Object> params) {

        Map<String, Object> list = progressPlanDaPingShowService.selectPlanProgressList(params);
        return Result.ok(list);
    }

}
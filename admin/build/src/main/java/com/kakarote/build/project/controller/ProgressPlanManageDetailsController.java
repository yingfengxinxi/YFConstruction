package com.kakarote.build.project.controller;


import com.kakarote.build.project.entity.BO.ProgressPlanManageBO;
import com.kakarote.build.project.entity.PO.ProgressModelsetDetails;
import com.kakarote.build.project.entity.PO.ProgressPlanManageDetails;
import com.kakarote.build.project.service.IProgressModelsetDetailsService;
import com.kakarote.build.project.service.IProgressPlanManageDetailsService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 进度管理--计划管理--子表 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-05-06
 */
@RestController
@RequestMapping("/progressPlanManageDetails")
public class ProgressPlanManageDetailsController {

    @Autowired
    private IProgressPlanManageDetailsService iProgressPlanManageDetailsService;

    @Autowired
    private IProgressModelsetDetailsService modelsetDetailsService;

    @PostMapping("/selectByMap")
    @ApiOperation("查询信息")
    @Transactional
    @OperateLog(detail = "查询信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectByMap(@RequestBody ProgressPlanManageBO map) {
        BasePage<Map> mapBasePage = iProgressPlanManageDetailsService.selectByMap(map);
        return Result.ok(mapBasePage);
    }

    @PostMapping("/selectByMapNoPage")
    @ApiOperation("查询信息")
    @Transactional
    @OperateLog(detail = "查询信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectByMapNoPage(@RequestBody ProgressPlanManageBO map) {
        List<Map> maps = iProgressPlanManageDetailsService.selectByMapNoPage(map);
        return Result.ok(maps);
    }

    @PostMapping("/save")
    @ApiOperation("保存信息")
    @Transactional
    @OperateLog(detail = "保存信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result save(@RequestBody Map<String,Object>map) throws Exception{
        R save = iProgressPlanManageDetailsService.saveAndDelete(map);
        return Result.ok(save);
    }

    @PostMapping("/selectPlanLastTime")
    @ApiOperation("查询最近生成的计划信息")
    @Transactional
    @OperateLog(detail = "查询最近生成的计划信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectPlanLastTime() {
        Map<String, Object> stringObjectMap = iProgressPlanManageDetailsService.selectPlanLastTime();
        return Result.ok(stringObjectMap);
    }

    @PostMapping("/selectPlanLastTimeSHOUYE")
    @ApiOperation("查询最近生成的计划信息--首页")
    @OperateLog(detail = "查询最近生成的计划信息--首页", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectPlanLastTimeSHOUYE() {

        Map<String, Object> stringObjectMap = iProgressPlanManageDetailsService.selectPlanLastTimeSHOUYE();
        return Result.ok(stringObjectMap);
    }



    @PostMapping("/getMaxMinFormLastPlan")
    @ApiOperation("月计划--从最近生成计划的 计划开始、计划结束 获取最小开始月份和最大结束月份")
    @OperateLog(detail = "月计划--从最近生成计划的 计划开始、计划结束 获取最小开始月份和最大结束月份", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getMaxMinFormLastPlan() {
        Map<String, Object> maxMinFormLastPlan = iProgressPlanManageDetailsService.getMaxMinFormLastPlan();
        if(maxMinFormLastPlan == null) {
            return Result.error(500,"总计划为空!");
        }
        return Result.ok(maxMinFormLastPlan);
    }

    @PostMapping("/selectByMapForMonthPlan")
    @ApiOperation("生成月计划")
    @OperateLog(detail = "生成月计划", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectByMapForMonthPlan(@RequestBody Map<String,Object>map) {
        List<Map> maps = iProgressPlanManageDetailsService.selectByMapForMonthPlan(map);
        return Result.ok(maps);
    }

    /**
     * app端使用分頁查詢
     * @param planManageBO
     * @return
     */
    @PostMapping("/list")
    @ApiOperation("app-查詢信息")
    @OperateLog(detail = "app-查詢信息",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String, Object>>> list(@RequestBody ProgressPlanManageBO planManageBO){
        BasePage<Map<String, Object>> list = iProgressPlanManageDetailsService.selectByPage(planManageBO);
        return Result.ok(list);
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("根据id获取计划详情")
    @OperateLog(detail = "根据id获取计划详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<ProgressPlanManageDetails> getById(@PathVariable Integer id) {
        ProgressPlanManageDetails data = iProgressPlanManageDetailsService.getById(id);
        // 根据前置节点id查询任务
        if (data.getPreNode() != null && !data.getPreNode().equals("")) {
            ProgressModelsetDetails modelsetDetails = modelsetDetailsService.getById(data.getPreNode());
            if (modelsetDetails != null) {
                data.setPreNodeName(modelsetDetails.getName());
            }
        }
        return Result.ok(data);
    }

    @PostMapping("/update")
    @ApiOperation("更新计划")
    @OperateLog(detail = "更新计划", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result<Long> update(@RequestBody ProgressPlanManageDetails planManageDetails) {
        iProgressPlanManageDetailsService.updateById(planManageDetails);
        return Result.ok();
    }

}


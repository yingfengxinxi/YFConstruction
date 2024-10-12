package com.kakarote.build.project.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.project.entity.BO.ProgressPlanMonthBO;
import com.kakarote.build.project.entity.PO.ProgressModelsetDetails;
import com.kakarote.build.project.entity.PO.ProgressPlanMonthDetails;
import com.kakarote.build.project.service.IProgressModelsetDetailsService;
import com.kakarote.build.project.service.IProgressPlanMonthDetailsService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.workflow.WfAnnotation;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 进度管理--月计划明细 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-06-24
 */
@RestController
@RequestMapping("/progressPlanMonthDetails")
public class ProgressPlanMonthDetailsController {
    @Autowired
    private IProgressPlanMonthDetailsService iProgressPlanMonthDetailsService;

    @Autowired
    private IProgressModelsetDetailsService modelsetDetailsService;


    @PostMapping("/selectByMapNoPage")
    @ApiOperation("查询信息")
    @Transactional
    @OperateLog(detail = "查询信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectByMapNoPage(@RequestBody Map map) {
        List<Map> maps = iProgressPlanMonthDetailsService.selectByMapNoPage(map);
        return Result.ok(maps);
    }

    @PostMapping("/save")
    @ApiOperation("保存信息")
    @Transactional
    @OperateLog(detail = "保存全部信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result save(@RequestBody List<ProgressPlanMonthDetails> lists) {


        iProgressPlanMonthDetailsService.saveOrDelete(lists);
        return Result.ok();
    }

    @PostMapping("/selectById/{id}")
    @ApiOperation("根据主键查询信息")
    @OperateLog(detail = "根据主键查询信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectById(@PathVariable String id) {

        ProgressPlanMonthDetails byId = iProgressPlanMonthDetailsService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("/selectByLast/{mid}")
    @ApiOperation("根据mid查询树")
    @OperateLog(detail = "根据mid查询树", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectByLast(@PathVariable Integer mid) {

        List<Map> byId = iProgressPlanMonthDetailsService.selectByLast(mid);
        return Result.ok(byId);
    }

    @PostMapping("/updateOne")
    @ApiOperation("修改单条信息--进度填报")
    @WfAnnotation
    @OperateLog(detail = "修改单条信息--进度填报流程", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result updateOne(@RequestBody ProgressPlanMonthDetails progressPlanMonthDetails) {

        iProgressPlanMonthDetailsService.updateById(progressPlanMonthDetails);
        return Result.ok();
    }

    @PostMapping("/selectTreeMonth")
    @ApiOperation("查询月计划树结构数据")
    @OperateLog(detail = "查询月计划树结构数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectTree(@RequestBody Map<String,Object> map){
        return Result.ok(iProgressPlanMonthDetailsService.selectTree(map));
    }


    @PostMapping("/selectIdAndName/{mid}")
    @ApiOperation("查询月计划id和名称")
    @OperateLog(detail = "查询月计划id和名称",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectIdAndName(@PathVariable Integer mid){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("id","name").eq("mid",mid);

        List<Map> list = iProgressPlanMonthDetailsService.getBaseMapper().selectMaps(queryWrapper);
        return Result.ok(list);
    }




    @PostMapping("/selectLastMidFormMonth")
    @ApiOperation("查询月计划最新的一条数据的mid--周计划使用")
    @OperateLog(detail = "查询月计划最新的一条数据的mid--周计划使用", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectLastMidFormMonth() {
        Map maps = iProgressPlanMonthDetailsService.selectLastMidFormMonth();
        return Result.ok(maps);
    }

    @PostMapping("/selectByMapForWeekPlan")
    @ApiOperation("生成周计划")
    @OperateLog(detail = "生成周计划", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectByMapForWeekPlan(@RequestBody Map<String,Object>map) {
        List<Map> maps = iProgressPlanMonthDetailsService.selectByMapForWeekPlan(map);
        return Result.ok(maps);
    }

    @PostMapping("/exportExcel")
    @ApiOperation("导出")
    @OperateLog(detail = "导出",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public void exportExcel(@RequestBody Map<String,Object>map, HttpServletResponse response){
        iProgressPlanMonthDetailsService.exportExcel(map,response);
    }

    /**
     * app端使用分頁查詢
     * @param planMonthBO
     * @return
     */
    @PostMapping("/list")
    @ApiOperation("app-查詢信息")
    @OperateLog(detail = "app-查詢信息",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String, Object>>> list(@RequestBody ProgressPlanMonthBO planMonthBO){
        BasePage<Map<String, Object>> list = iProgressPlanMonthDetailsService.selectByPage(planMonthBO);
        return Result.ok(list);
    }

    @PostMapping("/deleteByMid/{mid}")
    @ApiOperation("根据mid删除月计划")
    @OperateLog(detail = "根据mid删除月计划", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result deleteByMid(@PathVariable Integer mid) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mid",mid);
        iProgressPlanMonthDetailsService.remove(queryWrapper);

        return Result.ok();
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("根据id获取计划详情")
    @OperateLog(detail = "根据id获取计划详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<ProgressPlanMonthDetails> getById(@PathVariable String id) {
        ProgressPlanMonthDetails data = iProgressPlanMonthDetailsService.getById(id);
        // 根据前置节点id查询任务
        if (data.getPreNode() != null && !data.getPreNode().equals("")) {
            ProgressModelsetDetails modelsetDetails = modelsetDetailsService.getById(data.getPreNode());
            if (modelsetDetails != null) {
                data.setPreNodeName(modelsetDetails.getName());
            }
        }
        return Result.ok(data);
    }
    
}


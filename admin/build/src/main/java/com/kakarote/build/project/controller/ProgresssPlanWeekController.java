package com.kakarote.build.project.controller;


import com.kakarote.build.project.entity.BO.ProgresssPlanWeekBO;
import com.kakarote.build.project.entity.PO.ProgressPlanMonthDetails;
import com.kakarote.build.project.entity.PO.ProgresssPlanWeek;
import com.kakarote.build.project.service.IProgressPlanMonthDetailsService;
import com.kakarote.build.project.service.IProgresssPlanWeekService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 计划管理--周计划 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-07-05
 */
@RestController
@RequestMapping("/progresssPlanWeek")
public class ProgresssPlanWeekController {

    @Autowired
    private IProgresssPlanWeekService iProgresssPlanWeekService;

    @PostMapping("/selectByMapNoPage")
    @ApiOperation("查询信息")
    @Transactional
    @OperateLog(detail = "查询信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectByMapNoPage(@RequestBody Map map) {
        List<Map> maps = iProgresssPlanWeekService.selectByMapNoPage(map);
        return Result.ok(maps);
    }
    @PostMapping("/selectAppList")
    @ApiOperation("查询信息")
    @Transactional
    @OperateLog(detail = "查询信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectAppList(@RequestBody ProgresssPlanWeekBO progresssPlanWeekBO) {
        List<Map> maps = iProgresssPlanWeekService.selectAppList(progresssPlanWeekBO);
        return Result.ok(maps);
    }
    @PostMapping("/save")
    @ApiOperation("保存信息")
    @Transactional
    @OperateLog(detail = "保存全部信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result save(@RequestBody List<ProgresssPlanWeek> lists) {

        iProgresssPlanWeekService.saveOrDelete(lists);
        return Result.ok();
    }
    @PostMapping("/AppUpdateById")
    @ApiOperation("列表数据修改")
    @Transactional
    @OperateLog(detail = "列表数据修改", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result AppUpdateById(@RequestBody ProgresssPlanWeek progresssPlanWeek) {
        progresssPlanWeek.setUpdateTime(new Date());
        progresssPlanWeek.setUpdateUserId(UserUtil.getUserId());
        iProgresssPlanWeekService.updateById(progresssPlanWeek);
        return Result.ok();
    }
    @PostMapping("/selectByLast/{mid}")
    @ApiOperation("根据mid查询最近生成的周树")
    @OperateLog(detail = "根据mid查询最近生成的周树", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectByLast(@PathVariable Integer mid) {

        List<Map> byId = iProgresssPlanWeekService.selectByLast(mid);
        return Result.ok(byId);
    }
    @PostMapping("/selectByLastAsApp/{mid}")
    @ApiOperation("根据mid查询最近生成的计划（APP）")
    @OperateLog(detail = "根据mid查询最近生成的计划（APP）", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectByLastAsApp(@PathVariable Integer mid) {

        List<Map> byId = iProgresssPlanWeekService.selectByLastAsApp(mid);
        return Result.ok(byId);
    }
    @PostMapping("/getByIdAsApp/{id}")
    @ApiOperation("根据id查询计划（APP）")
    @OperateLog(detail = "根据id查询计划（APP）", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getByIdAsApp(@PathVariable String id) {
        return Result.ok(iProgresssPlanWeekService.getByIdAsApp(id));
    }
    @PostMapping("/SaveAsApp/{id}")
    @ApiOperation("根据id保存计划（APP）")
    @OperateLog(detail = "根据id保存计划（APP）", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result SaveAsApp(@PathVariable ProgresssPlanWeek progresssPlanWeek) {
        return Result.ok(iProgresssPlanWeekService.save(progresssPlanWeek));
    }
    @PostMapping("/exportExcel")
    @ApiOperation("导出")
    @OperateLog(detail = "导出",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public void exportExcel(@RequestBody Map<String,Object>map, HttpServletResponse response){
        iProgresssPlanWeekService.exportExcel(map,response);
    }

}


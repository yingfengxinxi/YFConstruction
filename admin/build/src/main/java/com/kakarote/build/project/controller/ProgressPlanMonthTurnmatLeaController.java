package com.kakarote.build.project.controller;


import com.kakarote.build.project.entity.BO.ProgressPlanMonthBO;
import com.kakarote.build.project.entity.PO.ProgressPlanMonthTurnmatLea;
import com.kakarote.build.project.service.IProgressPlanMonthTurnmatLeaService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 计划管理--月计划--周转材租赁明细 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-06-29
 */
@RestController
@RequestMapping("/progressPlanMonthTurnmatLea")
public class ProgressPlanMonthTurnmatLeaController {
    @Autowired
    private IProgressPlanMonthTurnmatLeaService iProgressPlanMonthTurnmatLeaService;


    @PostMapping("/list")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> list(@RequestBody ProgressPlanMonthBO progressPlanMonthBO) {
        BasePage<Map<String,Object>> list = iProgressPlanMonthTurnmatLeaService.selectList(progressPlanMonthBO);
        return Result.ok(list);
    }

    @PostMapping("/save")
    @ApiOperation("新增信息")
    @Transactional
    @OperateLog(detail = "新增信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result edit(@Validated @RequestBody ProgressPlanMonthTurnmatLea progressPlanMonthTurnmatLea) {
        iProgressPlanMonthTurnmatLeaService.save(progressPlanMonthTurnmatLea);
        return Result.ok(progressPlanMonthTurnmatLea.getId());
    }

    @PostMapping("/updateById")
    @ApiOperation("修改信息")
    @Transactional
    @OperateLog(detail = "修改信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result updateById(@Validated @RequestBody ProgressPlanMonthTurnmatLea progressPlanMonthTurnmatLea) {
        iProgressPlanMonthTurnmatLeaService.updateById(progressPlanMonthTurnmatLea);
        return Result.ok(progressPlanMonthTurnmatLea.getId());
    }


    @PostMapping("/delById/{id}")
    @ApiOperation("根据主键删除信息")
    @Transactional
    @OperateLog(detail = "根据主键删除信息",types = LogTypes.BUILD,behavior = LogBehavior.DELETE)
    public Result delById(@PathVariable Integer id){

        iProgressPlanMonthTurnmatLeaService.removeById(id);

        return Result.ok();
    }

    @PostMapping("/selectById/{id}")
    @ApiOperation("根据主键查询信息")
    @OperateLog(detail = "根据主键查询信息",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result<ProgressPlanMonthTurnmatLea> selectById(@PathVariable Integer id){
        ProgressPlanMonthTurnmatLea byId = iProgressPlanMonthTurnmatLeaService.getById(id);
        return Result.ok(byId);
    }

//    @PostMapping("/exportExcel")
//    @ApiOperation("导出")
//    @OperateLog(detail = "导出",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
//    public void exportExcel(@RequestBody LabourOddVisaBO labourOddVisaBO, HttpServletResponse response){
//        iProgressPlanMonthTurnmatLeaService.exportExcel(labourOddVisaBO,response);
//    }
}


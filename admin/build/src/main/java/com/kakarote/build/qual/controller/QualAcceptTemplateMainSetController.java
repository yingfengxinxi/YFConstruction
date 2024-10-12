package com.kakarote.build.qual.controller;


import com.kakarote.build.qual.entity.BO.QualAcceptTemplateMainSetBO;
import com.kakarote.build.qual.entity.BO.QualDigitInfoBO;
import com.kakarote.build.qual.entity.PO.QualAcceptTemplateMainSet;
import com.kakarote.build.qual.entity.PO.QualDigitInfo;
import com.kakarote.build.qual.service.IQualAcceptTemplateMainSetService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 施工质量--分户验收--模板设置--主表 前端控制器
 * </p>
 *
 * @author wnj
 * @since 2021-06-28
 */
@RestController
@RequestMapping("/qualAcceptTemplateMainSet")
public class QualAcceptTemplateMainSetController {
    @Autowired
    private IQualAcceptTemplateMainSetService iQualAcceptTemplateMainSetService;

    @PostMapping("/list")
    @ApiOperation("查询验收模板列表")
    @OperateLog(detail = "查询验收模板列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<QualAcceptTemplateMainSet>> list(@RequestBody QualAcceptTemplateMainSetBO qualAcceptTemplateMainSetBO) {
        return Result.ok( iQualAcceptTemplateMainSetService.selectByPage(qualAcceptTemplateMainSetBO));
    }

    @GetMapping("/getById/{id}")
    @ApiOperation("获取验收模板详情")
    public Result getMenuById(@PathVariable Integer id) {
        return Result.ok(iQualAcceptTemplateMainSetService.selectById(id));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存验收模板")
    @OperateLog(detail = "保存验收模板", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdateMessage(@Validated @RequestBody Map<String,Object> map) {
        return Result.ok(iQualAcceptTemplateMainSetService.SaveOrUpdate(map));
    }

    @GetMapping("/del/{id}")
    @ApiOperation("删除验收模板")
    @OperateLog(detail = "删除验收模板", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result del(@PathVariable Integer id) {
        return Result.ok(iQualAcceptTemplateMainSetService.deleteById(id));
    }

}



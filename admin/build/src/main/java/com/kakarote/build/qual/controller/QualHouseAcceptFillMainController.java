package com.kakarote.build.qual.controller;


import com.kakarote.build.qual.entity.BO.QualAcceptTemplateMainSetBO;
import com.kakarote.build.qual.entity.BO.QualHouseAcceptFillMainBO;
import com.kakarote.build.qual.entity.PO.QualAcceptTemplateMainSet;
import com.kakarote.build.qual.entity.PO.QualHouseAcceptFillMain;
import com.kakarote.build.qual.service.IQualHouseAcceptFillMainService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工质量--分户验收--填报--主表 前端控制器
 * </p>
 *
 * @author wnj
 * @since 2021-06-29
 */
@RestController
@RequestMapping("/qualHouseAcceptFillMain")
public class QualHouseAcceptFillMainController {
    @Autowired
    private IQualHouseAcceptFillMainService iQualHouseAcceptFillMainService;
    @PostMapping("/list")
    @ApiOperation("查询验收模板列表")
    @OperateLog(detail = "查询验收模板列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<QualHouseAcceptFillMain>> list(@RequestBody QualHouseAcceptFillMainBO QualHouseAcceptFillMainBO) {
        return Result.ok( iQualHouseAcceptFillMainService.selectByPage(QualHouseAcceptFillMainBO));
    }

    @PostMapping("/wxlist")
    @ApiOperation("查询验收模板列表")
    public Result<BasePage<QualHouseAcceptFillMainBO>> wxlist(@RequestBody QualHouseAcceptFillMainBO QualHouseAcceptFillMainBO) {
        return Result.ok( iQualHouseAcceptFillMainService.wxselectByPage(QualHouseAcceptFillMainBO));
    }

    @GetMapping("/getById/{id}")
    @ApiOperation("获取验收模板详情")
    public Result getMenuById(@PathVariable Integer id) {
        return Result.ok(iQualHouseAcceptFillMainService.selectById(id));
    }

    @GetMapping("/wxselectById")
    @ApiOperation("获取验收模板详情")
    public Result wxselectById( String id) {
        return Result.ok(iQualHouseAcceptFillMainService.wxselectById(Integer.valueOf(id)));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存验收模板")
    @OperateLog(detail = "保存验收模板", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdateMessage(@Validated @RequestBody Map<String,Object> map) {
        return Result.ok(iQualHouseAcceptFillMainService.SaveOrUpdate(map));
    }

    @PostMapping("/wxSaveOrUpdate")
    @ApiOperation("保存验收模板")
//    @OperateLog(detail = "保存验收模板", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result wxSaveOrUpdate(@Validated @RequestBody Map<String,Object> map) {
        return Result.ok(iQualHouseAcceptFillMainService.wxSaveOrUpdate(map));
    }

    @PostMapping("/del")
    @ApiOperation("删除验收模板")
    @OperateLog(detail = "删除验收模板", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result del(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for(String id:ids){
            iQualHouseAcceptFillMainService.deleteById(Integer.valueOf(id));
        }
        return Result.ok();
    }

}


package com.kakarote.build.qual.controller;


import com.kakarote.build.qual.entity.BO.QualDigitInfoBO;
import com.kakarote.build.qual.entity.BO.QualPlanBO;
import com.kakarote.build.qual.entity.PO.QualDigitInfo;
import com.kakarote.build.qual.service.IQualDigitInfoService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 数字质量资料 前端控制器
 * </p>
 *
 * @author pz
 * @since 2021-03-22
 */
@RestController
@RequestMapping("/qualDigitInfo")
public class QualDigitInfoController {

    @Autowired
    private IQualDigitInfoService qualDigitInfoService;

    @PostMapping("/list")
    @ApiOperation("查询数字质量资料")
    @OperateLog(detail = "查询数字质量资料", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<QualDigitInfo>> list(@RequestBody QualDigitInfoBO qualDigitInfoBO) {
        return Result.ok( qualDigitInfoService.selectByPage(qualDigitInfoBO));
    }

    @GetMapping("/getById/{digitalInfoId}")
    @ApiOperation("获取质量问题详情")
    public Result<Object> getMenuById(@PathVariable Integer digitalInfoId) {
        return Result.ok(qualDigitInfoService.getById(digitalInfoId));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存数字质量资料")
    @OperateLog(detail = "保存数字质量资料", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result<Long> saveOrUpdateMessage(@RequestBody QualDigitInfo qualDigitInfo) {
        qualDigitInfoService.saveOrUpdate(qualDigitInfo);
        return Result.ok();
    }
//
//    @GetMapping("/del/{digitalInfoId}")
//    @ApiOperation("删除数字质量资料")
//    @OperateLog(detail = "删除数字质量资料", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
//    public Result del(@PathVariable Integer digitalInfoId) {
//        qualDigitInfoService.removeById(digitalInfoId);
//        return R.ok();
//    }

    @PostMapping("/delTrueByIds")
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            qualDigitInfoService.removeById(id);
        }
        return Result.ok();
    }
}


package com.kakarote.build.wifi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.wifi.entity.BO.WifiCourseLibraryBO;
import com.kakarote.build.wifi.entity.BO.WifiQuestionsBO;
import com.kakarote.build.wifi.entity.PO.WifiCourseLibrary;
import com.kakarote.build.wifi.entity.PO.WifiQuestions;
import com.kakarote.build.wifi.service.IWifiCourseLibraryService;
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

import java.util.List;
import java.util.Map;

/**
 * <p>
 * wifi教育-课程库 前端控制器
 * </p>
 *
 * @author wnj
 * @since 2021-08-19
 */
@RestController
@RequestMapping("/wifiCourseLibrary")
public class WifiCourseLibraryController {
    @Autowired
    private IWifiCourseLibraryService IWifiCourseLibraryService;

    @PostMapping("/getLibraryList")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<WifiCourseLibrary>> getLibraryList(@RequestBody WifiCourseLibraryBO WifiCourseLibraryBO) {
        BasePage<WifiCourseLibrary> list = IWifiCourseLibraryService.getLibraryList(WifiCourseLibraryBO);
        return Result.ok(list);
    }

    @PostMapping("/save")
    @ApiOperation("新增信息")
    @Transactional
    @OperateLog(detail = "新增信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result edit(@Validated @RequestBody WifiCourseLibrary WifiCourseLibrary) {
        IWifiCourseLibraryService.save(WifiCourseLibrary);
        return Result.ok(WifiCourseLibrary.getId());
    }

    @PostMapping("/updateById")
    @ApiOperation("修改信息")
    @Transactional
    @OperateLog(detail = "修改信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result updateById(@Validated @RequestBody WifiCourseLibrary WifiCourseLibrary) {
        boolean b = IWifiCourseLibraryService.updateById(WifiCourseLibrary);
        return Result.ok(WifiCourseLibrary.getId());
    }

    @PostMapping("/delById/{id}")
    @ApiOperation("根据主键删除信息")
    @Transactional
    @OperateLog(detail = "根据主键删除信息",types = LogTypes.BUILD,behavior = LogBehavior.DELETE)
    public Result delById(@PathVariable String id){
        IWifiCourseLibraryService.removeById(id);
        return Result.ok();
    }

    @PostMapping("/selectById/{id}")
    @ApiOperation("根据主键查询信息")
    @Transactional
    @OperateLog(detail = "根据主键查询信息",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result selectById(@PathVariable String id){
        return Result.ok(IWifiCourseLibraryService.getById(id));
    }
}


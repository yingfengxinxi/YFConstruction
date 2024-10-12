package com.kakarote.build.wifi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.wifi.entity.BO.WifiQuestionsBO;
import com.kakarote.build.wifi.entity.BO.WifiTestPaperRulesBO;
import com.kakarote.build.wifi.entity.PO.WifiQuestOption;
import com.kakarote.build.wifi.entity.PO.WifiQuestions;
import com.kakarote.build.wifi.entity.PO.WifiTestPaperRules;
import com.kakarote.build.wifi.service.IWifiQuestOptionService;
import com.kakarote.build.wifi.service.IWifiQuestionsService;
import com.kakarote.build.wifi.service.IWifiTestPaperRulesDelService;
import com.kakarote.build.wifi.service.IWifiTestPaperRulesService;
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
 * wifi答题教育--试卷规则配置 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
@RestController
@RequestMapping("/wifiTestPaperRules")
public class WifiTestPaperRulesController {
    @Autowired
    private IWifiTestPaperRulesService iWifiTestPaperRulesService;
    @Autowired
    private IWifiTestPaperRulesDelService iWifiTestPaperRulesDelService;

    @PostMapping("/list")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> list(@RequestBody WifiTestPaperRulesBO wifiTestPaperRulesBO) {
        BasePage<Map<String,Object>> list = iWifiTestPaperRulesService.selectList(wifiTestPaperRulesBO);
        return Result.ok(list);
    }

    @PostMapping("/save")
    @ApiOperation("新增信息")
    @Transactional
    @OperateLog(detail = "新增信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result edit(@Validated @RequestBody WifiTestPaperRules wifiTestPaperRules) {
        iWifiTestPaperRulesService.save(wifiTestPaperRules);
        return Result.ok(wifiTestPaperRules.getId());
    }

    @PostMapping("/updateById")
    @ApiOperation("修改信息")
    @Transactional
    @OperateLog(detail = "修改信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result updateById(@Validated @RequestBody WifiTestPaperRules wifiTestPaperRules) {
        boolean b = iWifiTestPaperRulesService.updateById(wifiTestPaperRules);
        return Result.ok(wifiTestPaperRules.getId());
    }


    @PostMapping("/delById/{id}")
    @ApiOperation("根据主键删除信息")
    @Transactional
    @OperateLog(detail = "根据主键删除信息",types = LogTypes.BUILD,behavior = LogBehavior.DELETE)
    public Result delById(@PathVariable String id){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("paper_rules_id",id);
        iWifiTestPaperRulesDelService.remove(queryWrapper);

        iWifiTestPaperRulesService.removeById(id);

        return Result.ok();
    }

    @PostMapping("/selectById/{id}")
    @ApiOperation("根据主键查询信息")
    @OperateLog(detail = "根据主键查询信息",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result selectById(@PathVariable String id){
        WifiTestPaperRules one = iWifiTestPaperRulesService.query().eq("id", id).ne("project_id", 0).one();

//        WifiTestPaperRules byId = iWifiTestPaperRulesService.getById(id);
        return Result.ok(one);
    }
}


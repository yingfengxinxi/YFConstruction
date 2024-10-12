package com.kakarote.build.wifi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.wifi.entity.BO.WifiTestPaperRulesBO;
import com.kakarote.build.wifi.entity.PO.WifiTestPaperRules;
import com.kakarote.build.wifi.entity.PO.WifiTestPaperRulesDel;
import com.kakarote.build.wifi.service.IWifiTestPaperRulesDelService;
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
 * wifi教育答题--试卷规则配置详情 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
@RestController
@RequestMapping("/wifiTestPaperRulesDel")
public class WifiTestPaperRulesDelController {
    @Autowired
    private IWifiTestPaperRulesDelService iWifiTestPaperRulesDelService;

    @PostMapping("/list")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String,Object>>> list(@RequestBody Map<String,Object>map) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("paper_rules_id",(String)map.get("paperRulesId"));
        queryWrapper.ne("project_id",0);
        queryWrapper.orderByDesc("create_time");
        List<Map<String,Object>> list = iWifiTestPaperRulesDelService.getBaseMapper().selectMaps(queryWrapper);
        return Result.ok(list);
    }

    @PostMapping("/save")
    @ApiOperation("新增信息")
    @Transactional
    @OperateLog(detail = "新增信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result edit(@Validated @RequestBody WifiTestPaperRulesDel wifiTestPaperRulesDel) {
        iWifiTestPaperRulesDelService.save(wifiTestPaperRulesDel);
        return Result.ok(wifiTestPaperRulesDel.getId());
    }

    @PostMapping("/updateById")
    @ApiOperation("修改信息")
    @Transactional
    @OperateLog(detail = "修改信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result updateById(@Validated @RequestBody WifiTestPaperRulesDel wifiTestPaperRulesDel) {
        boolean b = iWifiTestPaperRulesDelService.updateById(wifiTestPaperRulesDel);
        return Result.ok(wifiTestPaperRulesDel.getId());
    }


    @PostMapping("/delById/{id}")
    @ApiOperation("根据主键删除信息")
    @Transactional
    @OperateLog(detail = "根据主键删除信息",types = LogTypes.BUILD,behavior = LogBehavior.DELETE)
    public Result delById(@PathVariable String id){

        iWifiTestPaperRulesDelService.removeById(id);
        
        return Result.ok();
    }

    @PostMapping("/selectById/{id}")
    @ApiOperation("根据主键查询信息")
    @OperateLog(detail = "根据主键查询信息",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result selectById(@PathVariable String id){
        WifiTestPaperRulesDel one = iWifiTestPaperRulesDelService.query().eq("id", id).ne("project_id", 0).one();

//        WifiTestPaperRulesDel byId = iWifiTestPaperRulesDelService.getById(id);
        return Result.ok(one);
    }

}


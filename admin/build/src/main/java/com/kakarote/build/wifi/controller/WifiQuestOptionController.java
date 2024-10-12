package com.kakarote.build.wifi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.wifi.entity.BO.WifiQuestionsBO;
import com.kakarote.build.wifi.entity.PO.WifiQuestOption;
import com.kakarote.build.wifi.entity.PO.WifiQuestions;
import com.kakarote.build.wifi.service.IWifiQuestOptionService;
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
 * wifi教育答题--问题选项表 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
@RestController
@RequestMapping("/wifiQuestOption")
public class WifiQuestOptionController {

    @Autowired
    private IWifiQuestOptionService iWifiQuestOptionService;

    @PostMapping("/getQuestOptionList/{questId}")
    @ApiOperation("根据试题id查询选项信息列表")
    @OperateLog(detail = "根据试题id查询选项信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<WifiQuestOption>> getQuestionList(@PathVariable String questId) {
        List<WifiQuestOption> list = iWifiQuestOptionService
                .query()
                .eq("quest_id",questId)
                .ne("tenant_id",0)
                .and(wrapper -> wrapper
                        .ne("project_id", 0)
                        .or()
                        .isNull("project_id")
                )
                .orderByAsc("answer_Flag")
                .orderByDesc("create_time")
                .list();
        return Result.ok(list);
    }

    @PostMapping("/save")
    @ApiOperation("新增信息")
    @Transactional
    @OperateLog(detail = "新增信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result edit(@Validated @RequestBody WifiQuestOption wifiQuestOption) {

        iWifiQuestOptionService.save(wifiQuestOption);
        return Result.ok(wifiQuestOption.getId());
    }

    @PostMapping("/updateById")
    @ApiOperation("修改信息")
    @Transactional
    @OperateLog(detail = "修改信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result updateById(@Validated @RequestBody WifiQuestOption wifiQuestOption) {
        boolean b = iWifiQuestOptionService.updateById(wifiQuestOption);
        return Result.ok(wifiQuestOption.getId());
    }


    /**
     * 删除方法 已屏蔽tenant_id 和 project_id
     * @param id
     * @return
     */
    @PostMapping("/delById/{id}")
    @ApiOperation("根据主键删除信息")
    @Transactional
    @OperateLog(detail = "根据主键删除信息",types = LogTypes.BUILD,behavior = LogBehavior.DELETE)
    public Result delById(@PathVariable String id){
        iWifiQuestOptionService.deleteById(id);
        return Result.ok();
    }

    @PostMapping("/selectById/{id}")
    @ApiOperation("根据主键查询信息")
    @Transactional
    @OperateLog(detail = "根据主键查询信息",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result selectById(@PathVariable String id){
        WifiQuestOption one = iWifiQuestOptionService.query()
                .eq("id", id)
                .ne("tenant_id",0)
                .and(wrapper -> wrapper
                        .ne("project_id", 0)
                        .or()
                        .isNull("project_id")
                )
                .one();

//        WifiQuestOption byId = iWifiQuestOptionService.getById(id);
        return Result.ok(one);
    }
    
}


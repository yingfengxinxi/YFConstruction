package com.kakarote.build.wifi.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.project.entity.PO.ProgressModelset;
import com.kakarote.build.project.service.IProgressModelsetDetailsService;
import com.kakarote.build.project.service.IProgressModelsetService;
import com.kakarote.build.wifi.entity.PO.WifiQuestType;
import com.kakarote.build.wifi.entity.PO.WifiQuestions;
import com.kakarote.build.wifi.service.IWifiQuestTypeService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 * WiFi教育答题--问题类别管理 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
@RestController
@RequestMapping("/wifiQuestType")
public class WifiQuestTypeController {
    @Autowired
    private IWifiQuestTypeService iWifiQuestTypeService;


    @PostMapping("/queryChildrenByMap")
    @ApiOperation("查询数据")
    @OperateLog(detail = "查询数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result queryChildrenByMap(@RequestBody Map<String,Object>map){
        List<Map> list = iWifiQuestTypeService.queryChildrenByMap(map);
        return Result.ok(list);
    }


    @PostMapping("/selectTree")
    @ApiOperation("查询树结构数据")
    @OperateLog(detail = "查询树结构数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectTree(){
        return Result.ok(iWifiQuestTypeService.selectTree());
    }

    @PostMapping("/selectQuestionIdAndName")
    @ApiOperation("查询问题类别")
    @OperateLog(detail = "查询问题类别",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result selectQuestionIdAndName(){

        QueryWrapper<WifiQuestType> queryWrapper = new QueryWrapper();
        queryWrapper.select("id", "type_name as name").ne("project_id",0);
        queryWrapper.ne("tenant_id",0);
        queryWrapper.and(wrapper -> wrapper
                .ne("project_id", 0)
                .or()
                .isNull("project_id")
        );
        List list1 = iWifiQuestTypeService.getBaseMapper().selectMaps(queryWrapper);

        return Result.ok(list1);
    }

    @PostMapping("/save")
    @ApiOperation("新增信息")
    @Transactional
    @OperateLog(detail = "新增信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result edit(@Validated @RequestBody WifiQuestType wifiQuestType) {
        String s = UUID.randomUUID().toString();
        wifiQuestType.setId(s);
        iWifiQuestTypeService.save(wifiQuestType);
        return Result.ok(wifiQuestType.getId());
    }

    @PostMapping("/updateById")
    @ApiOperation("修改信息")
    @Transactional
    @OperateLog(detail = "修改信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result updateById(@Validated @RequestBody WifiQuestType wifiQuestType) {
        boolean b = iWifiQuestTypeService.updateById(wifiQuestType);
        return Result.ok(wifiQuestType.getId());
    }


    @PostMapping("/queryById/{id}")
    @ApiOperation("根据主键查询信息")
    @OperateLog(detail = "根据主键查询信息",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result queryById(@PathVariable String id){
        WifiQuestType byId = iWifiQuestTypeService.query().
                select("*","case when\n" +
                        "        parentId != '0'\n" +
                        "        then\n" +
                        "        (select s.type_name from b_wifi_quest_type s where s.id = b_wifi_quest_type.parentId)\n" +
                        "        else '此任务为根任务，没有父任务' end\tas pname").
                eq("id",id).
                ne("tenant_id",0).
                and(wrapper -> wrapper
                        .ne("project_id", 0)
                        .or()
                        .isNull("project_id")
                ).one();
        return Result.ok(byId);
    }

    /**
     * 删除方法 已屏蔽tenant_id 和 project_id
     * @param id
     * @return
     */
    @PostMapping("/deleteMenu/{id}")
    @ApiOperation("根据主键循环删除信息")
    @Transactional
    @OperateLog(detail = "根据主键循环删除信息",types = LogTypes.BUILD,behavior = LogBehavior.DELETE)
    public Result deleteMenu(@PathVariable String id){

        iWifiQuestTypeService.delById(id);

        return Result.ok();
    }

}


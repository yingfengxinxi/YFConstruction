package com.kakarote.build.project.controller;


import com.kakarote.build.project.entity.PO.ProgressPlanDayFillin;
import com.kakarote.build.project.entity.PO.ProgressPlanMonthDetails;
import com.kakarote.build.project.service.IProgressPlanDayFillinService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 计划管理---每日填报 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-07-16
 */
@RestController
@RequestMapping("/progressPlanDayFillin")
public class ProgressPlanDayFillinController {

    @Autowired
    private IProgressPlanDayFillinService progressPlanDayFillinService;

    @PostMapping("/selectByMap")
    @ApiOperation("查询信息")
    @OperateLog(detail = "查询信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectByMap(@RequestBody Map map) throws Exception{

        List<Map> maps = progressPlanDayFillinService.selectByMap(map);
        return Result.ok(maps);
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("新增或者修改信息")
    @OperateLog(detail = "新增或者修改信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdate(@RequestBody List<ProgressPlanDayFillin> list){

        progressPlanDayFillinService.saveOrUpdateBatch(list);

        return Result.ok();
    }


}


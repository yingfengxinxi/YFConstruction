package com.kakarote.build.safety.controller;


import cn.hutool.core.util.IdUtil;
import com.kakarote.build.safety.entity.BO.SafetyEducationPlanBO;
import com.kakarote.build.safety.entity.PO.SafetyEducationPlan;
import com.kakarote.build.safety.service.ISafetyEducationPlanService;

import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 安全教育计划 前端控制器
 * </p>
 *
 * @author wnj
 * @since 2021-08-09
 */
@RestController
@RequestMapping("/safetyEducationPlan")
public class SafetyEducationPlanController {

    @Autowired
    private ISafetyEducationPlanService SafetyEducationPlanService;

    /**
     * 分页列表
     */
    @PostMapping("/list")
    public Result list(@RequestBody SafetyEducationPlanBO SEPBO) {
        return Result.ok(SafetyEducationPlanService.selectByPage(SEPBO));
    }

    /**
     * 获取详情
     */
    @PostMapping("/getById/{id}")
    public Result getById(@PathVariable String id) {
        return Result.ok(SafetyEducationPlanService.getById(id));
    }

    /**
     * 新增/修改
     */
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SafetyEducationPlan safetyEducationPlan) {
        if (safetyEducationPlan.getId() == null) {
            safetyEducationPlan.setId(IdUtil.randomUUID());
            safetyEducationPlan.setCreateBy(String.valueOf(UserUtil.getUserId()));
            safetyEducationPlan.setCreateTime(new Date());
            SafetyEducationPlanService.save(safetyEducationPlan);
        }else{
            safetyEducationPlan.setUpdateBy(String.valueOf(UserUtil.getUserId()));
            safetyEducationPlan.setUpdateTime(new Date());
            SafetyEducationPlanService.updateById(safetyEducationPlan);
        }
        return Result.ok();
    }

    /**
     * 物理删除
     */
    @PostMapping("/delByIds")
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            SafetyEducationPlanService.removeById(id);
        }
        return Result.ok();
    }

    /**
     * 删除
     */
    @GetMapping(value = "/deleteDetails/{id}")
    @ApiOperation("删除数据详情")
    @OperateLog(detail = "删除数据详情",types= LogTypes.SYS ,behavior = LogBehavior.DELETE)
    public Result remove(@PathVariable String id)
    {

        return Result.ok( SafetyEducationPlanService.removeById(id));
    }
}


package com.kakarote.build.safety.controller;


import com.kakarote.build.safety.entity.BO.SafetyLogBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEducation;
import com.kakarote.build.safety.entity.PO.SafetyLog;
import com.kakarote.build.safety.service.ISafetyLogService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 安全日志 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-04-06
 */
@RestController
@RequestMapping("/safetyLog")
public class SafetyLogController {

    @Autowired
    private ISafetyLogService iSafetyLogService;

    @RequestMapping("/list")
    @ApiOperation("查询安全日志列表")
    @OperateLog(detail = "查询安全日志列表",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result<BasePage<SafetyLog>> list(@RequestBody SafetyLogBO safetyLogBO){
        return Result.ok(iSafetyLogService.selectList(safetyLogBO));
    }

    @RequestMapping("/selectById/{id}")
    @ApiOperation("查询安全日志")
    @OperateLog(detail = "查询安全日志",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectById(@PathVariable String id){
        return Result.ok(iSafetyLogService.getById(id));
    }

    @RequestMapping("/save")
    @ApiOperation("更新安全日志")
    @OperateLog(detail = "更新安全日志",types= LogTypes.BUILD ,behavior = LogBehavior.UPDATE)
    public Result save(@RequestBody SafetyLog safetyLog){
        iSafetyLogService.saveOrUpdate(safetyLog);
        return Result.ok();
    }

    @RequestMapping("/delete/{id}")
    @ApiOperation("删除安全日志")
    @OperateLog(detail = "删除安全日志",types= LogTypes.BUILD ,behavior = LogBehavior.DELETE)
    public Result delete(@PathVariable Integer id){
        iSafetyLogService.removeById(id);
        return Result.ok();
    }

}


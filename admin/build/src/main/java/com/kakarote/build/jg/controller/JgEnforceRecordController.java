package com.kakarote.build.jg.controller;


import com.kakarote.build.jg.entity.BO.JgEducationTrainRecordBO;
import com.kakarote.build.jg.entity.BO.JgEnforceRecordBO;
import com.kakarote.build.jg.entity.PO.JgEducationTrainRecord;
import com.kakarote.build.jg.entity.PO.JgEnforceRecord;
import com.kakarote.build.jg.service.IJgEnforceRecordService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 视频监控执法记录 前端控制器
 * </p>
 *
 * @author wnj
 * @since 2021-11-25
 */
@RestController
@RequestMapping("/jgEnforceRecord")
public class JgEnforceRecordController {

    @Autowired
    private IJgEnforceRecordService iJgEnforceRecordService;

    @PostMapping("/list")
    @ApiOperation("查询视频监控执法记录列表")
    @OperateLog(detail = "查询视频监控执法记录列表", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<BasePage<JgEnforceRecord>> list(@RequestBody JgEnforceRecordBO jgEnforceRecordBO) {
        return Result.ok(iJgEnforceRecordService.selectByPage(jgEnforceRecordBO));
    }

    @PostMapping("/save")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation("保存或更新视频监控执法记录")
    @OperateLog(detail = "保存或更新视频监控执法记录", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result save(@RequestBody JgEnforceRecord jgEnforceRecord) {
        if (jgEnforceRecord.getId() == null) {
            jgEnforceRecord.setCreateBy(String.valueOf(UserUtil.getUserId()));
            jgEnforceRecord.setCreateTime(new Date());
            iJgEnforceRecordService.save(jgEnforceRecord);
        } else {
            jgEnforceRecord.setUpdateBy(String.valueOf(UserUtil.getUserId()));
            jgEnforceRecord.setUpdateTime(new Date());
            iJgEnforceRecordService.updateById(jgEnforceRecord);
        }
        return Result.ok();
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("查询视频监控执法记录详情")
    @OperateLog(detail = "查询视频监控执法记录详情", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result getById(@PathVariable Integer id) {
        JgEnforceRecord jgEnforceRecord = iJgEnforceRecordService.getById(id);
        return Result.ok(jgEnforceRecord);
    }

    @PostMapping("/delete")
    @ApiOperation("删除视频监控执法记录")
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(detail = "删除视频监控执法记录", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        return R.ok(iJgEnforceRecordService.removeByIds(ids));
    }

}


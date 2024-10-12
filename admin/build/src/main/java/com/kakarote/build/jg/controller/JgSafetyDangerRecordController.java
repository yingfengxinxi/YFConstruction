package com.kakarote.build.jg.controller;


import com.kakarote.build.jg.entity.BO.JgSafetyDangerRecordBO;
import com.kakarote.build.jg.entity.PO.JgSafetyDangerRecord;
import com.kakarote.build.jg.service.IJgSafetyDangerRecordService;
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
 * 安全隐患记录 前端控制器
 * </p>
 *
 * @author wnj
 * @since 2021-11-26
 */
@RestController
    @RequestMapping("/jgSafetyDangerRecord")
public class JgSafetyDangerRecordController {

    @Autowired
    private IJgSafetyDangerRecordService iJgSafetyDangerRecordService;

    @PostMapping("/list")
    @ApiOperation("查询安全隐患记录列表--新建表")
    @OperateLog(detail = "查询安全隐患记录列表", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<BasePage<JgSafetyDangerRecord>> list(@RequestBody JgSafetyDangerRecordBO jgSafetyDangerRecordBO) {
        return Result.ok(iJgSafetyDangerRecordService.selectByPage(jgSafetyDangerRecordBO));
    }

    @PostMapping("/save")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation("保存或更新安全隐患记录")
    @OperateLog(detail = "保存或更新安全隐患记录", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result save(@RequestBody JgSafetyDangerRecord jgSafetyDangerRecord) {
        if (jgSafetyDangerRecord.getId() == null) {
            jgSafetyDangerRecord.setCreateBy(String.valueOf(UserUtil.getUserId()));
            jgSafetyDangerRecord.setCreateTime(new Date());
            iJgSafetyDangerRecordService.save(jgSafetyDangerRecord);
        } else {
            jgSafetyDangerRecord.setUpdateBy(String.valueOf(UserUtil.getUserId()));
            jgSafetyDangerRecord.setUpdateTime(new Date());
            iJgSafetyDangerRecordService.updateById(jgSafetyDangerRecord);
        }
        return Result.ok();
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("查询安全隐患记录详情")
    @OperateLog(detail = "查询安全隐患记录详情", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result getById(@PathVariable Integer id) {
        JgSafetyDangerRecord jgSafetyDangerRecord = iJgSafetyDangerRecordService.getById(id);
        return Result.ok(jgSafetyDangerRecord);
    }

    @PostMapping("/delete")
    @ApiOperation("删除安全隐患记录")
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(detail = "删除安全隐患记录", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        return R.ok(iJgSafetyDangerRecordService.removeByIds(ids));
    }
}


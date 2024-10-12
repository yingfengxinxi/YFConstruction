package com.kakarote.build.jg.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.build.equipment.entity.BO.EquipmentAssetsBO;
import com.kakarote.build.equipment.entity.PO.EquipmentAssets;
import com.kakarote.build.equipment.service.IEquipmentAssetsService;
import com.kakarote.build.jg.entity.BO.JgEducationTrainRecordBO;
import com.kakarote.build.jg.entity.PO.JgEducationTrainRecord;
import com.kakarote.build.jg.service.IJgEducationTrainRecordService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 安全教育培训记录 前端控制器
 * </p>
 *
 * @author wnj
 * @since 2021-11-23
 */
@RestController
@RequestMapping("/jgEducationTrainRecord")
public class JgEducationTrainRecordController {
    @Autowired
    private IJgEducationTrainRecordService iJgEducationTrainRecordService;

    @PostMapping("/list")
    @ApiOperation("查询教育培训记录列表")
    @OperateLog(detail = "查询教育培训记录列表", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<BasePage<JgEducationTrainRecord>> list(@RequestBody JgEducationTrainRecordBO jgEducationTrainRecordBO) {
        return Result.ok(iJgEducationTrainRecordService.selectByPage(jgEducationTrainRecordBO));
    }

    @PostMapping("/save")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation("保存或更新教育培训记录")
    @OperateLog(detail = "保存或更新教育培训记录", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result save(@RequestBody JgEducationTrainRecord jgEducationTrainRecord) {
        if (jgEducationTrainRecord.getId() == null) {
            jgEducationTrainRecord.setCreateBy(String.valueOf(UserUtil.getUserId()));
            jgEducationTrainRecord.setCreateTime(new Date());
            iJgEducationTrainRecordService.save(jgEducationTrainRecord);
        } else {
            jgEducationTrainRecord.setUpdateBy(String.valueOf(UserUtil.getUserId()));
            jgEducationTrainRecord.setUpdateTime(new Date());
            iJgEducationTrainRecordService.updateById(jgEducationTrainRecord);
        }
        return Result.ok();
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("查询教育培训记录详情")
    @OperateLog(detail = "查询教育培训记录详情", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result getById(@PathVariable Integer id) {
        JgEducationTrainRecord jgEducationTrainRecord = iJgEducationTrainRecordService.getById(id);
        return Result.ok(jgEducationTrainRecord);
    }

    @PostMapping("/delete")
    @ApiOperation("删除教育培训记录")
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(detail = "删除教育培训记录", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        return R.ok(iJgEducationTrainRecordService.removeByIds(ids));
    }

}


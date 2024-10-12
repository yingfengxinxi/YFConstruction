package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.BO.EquipmentSprayTaskBO;
import com.kakarote.build.equipment.entity.PO.EquipmentSprayTask;
import com.kakarote.build.equipment.service.IEquipmentSprayTaskService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 喷淋定期任务表 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-08-05
 */
@RestController
@RequestMapping("/equipmentSprayTask")
public class EquipmentSprayTaskController {

    @Autowired
    private IEquipmentSprayTaskService sprayTaskService;

    @PostMapping("/list")
    @ApiOperation("查询喷淋定期任务表")
    @OperateLog(detail = "查询喷淋定期任务表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<EquipmentSprayTask>> list(@RequestBody EquipmentSprayTaskBO sprayTaskBO) {
        return Result.ok(sprayTaskService.selectByPage(sprayTaskBO));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新安全检查计划")
    @OperateLog(detail = "保存或更新安全检查计划", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdate(@RequestBody EquipmentSprayTask sprayTask) {
        if (sprayTask.getId() != null) {
            Long userId = UserUtil.getUser().getUserId();
            sprayTask.setUpdateUserId(userId);
            sprayTaskService.updateById(sprayTask);
        } else {
            sprayTaskService.save(sprayTask);
        }
        return Result.ok();
    }

    /**
     * 获取详情
     */
    @PostMapping("/getById/{id}")
    @ApiOperation("查询喷淋定期任务信息")
    @OperateLog(detail = "查询喷淋定期任务信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable Integer id) {
        return Result.ok(sprayTaskService.getById(id));
    }

    /**
     * 删除质量检查信息
     */
    @PostMapping("/delTrueByIds")
    @ApiOperation("删除喷淋定期任务信息")
    @OperateLog(detail = "删除喷淋定期任务信息", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            sprayTaskService.removeById(id);
        }
        return Result.ok();
    }
}


package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.BO.EquipmentSetBO;
import com.kakarote.build.equipment.entity.PO.EquipmentSet;
import com.kakarote.build.equipment.service.IEquipmentSetService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
@RestController
@RequestMapping("/equipmentSet")
public class EquipmentSetController {

    @Autowired
    private IEquipmentSetService setService;

    @PostMapping("/list")
    @ApiOperation("查询计划执行列表")
    @OperateLog(detail = "查询计划执行列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<EquipmentSet>> list(@RequestBody EquipmentSetBO setBO) {
        return Result.ok(setService.selectByPage(setBO));
    }

    @PostMapping("/listByAsset")
    @ApiOperation("查询执行记录")
    @OperateLog(detail = "查询执行记录", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<EquipmentSet>> listByAsset(@RequestBody EquipmentSetBO setBO) {
        return Result.ok(setService.selectListByAssetId(setBO));
    }

    @PostMapping("/save")
    @ApiOperation("计划执行保存")
    @OperateLog(detail = "维护计划保存", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result save(@RequestBody EquipmentSet equipmentSet) {
        equipmentSet.setUpdateUserId(UserUtil.getUserId());
        setService.updateById(equipmentSet);
        return Result.ok();
    }

    @PostMapping("/get/{id}")
    @ApiOperation("查询执行计划详情")
    @OperateLog(detail = "查询维护计划详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<EquipmentSet> get(@PathVariable Integer id) {
        EquipmentSet equipmentPlan = setService.getById(id);
        return Result.ok(equipmentPlan);
    }
}


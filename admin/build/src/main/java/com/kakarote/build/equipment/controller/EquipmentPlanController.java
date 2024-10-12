package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.BO.EquipmentPlanBO;
import com.kakarote.build.equipment.entity.PO.EquipmentPlan;
import com.kakarote.build.equipment.service.IEquipmentPlanAssetService;
import com.kakarote.build.equipment.service.IEquipmentPlanService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 设备监测-预防性维护计划-维护计划设置 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
@RestController
@RequestMapping("/equipmentPlan")
public class EquipmentPlanController {

    @Autowired
    private IEquipmentPlanService equipmentPlanService;

    @Autowired
    private IEquipmentPlanAssetService equipmentPlanAssetService;

    @PostMapping("/list")
    @ApiOperation("查询维护计划列表")
    @OperateLog(detail = "查询维护计划列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<EquipmentPlan>> list(@RequestBody EquipmentPlanBO equipmentPlanBO) {
        return Result.ok(equipmentPlanService.selectByPage(equipmentPlanBO));
    }

    @PostMapping("/get/{id}")
    @ApiOperation("查询维护计划详情")
    @OperateLog(detail = "查询维护计划详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<EquipmentPlan> get(@PathVariable Integer id) {
        EquipmentPlan equipmentPlan = equipmentPlanService.getById(id);
        return Result.ok(equipmentPlan);
    }

    @PostMapping("/save")
    @ApiOperation("维护计划保存")
    @OperateLog(detail = "维护计划保存", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result save(@RequestBody EquipmentPlan equipmentPlan) {
        if (equipmentPlan.getPlanId() == null) {
            equipmentPlanService.save(equipmentPlan);
        } else {
            equipmentPlan.setUpdateUserId(UserUtil.getUserId());
            equipmentPlanService.updateById(equipmentPlan);
        }
        // 保存设备信息
        equipmentPlanAssetService.updatePlanAsset(equipmentPlan);
        return Result.ok();
    }

    @PostMapping("/delete")
    @ApiOperation("删除维护计划")
    @OperateLog(detail = "删除维护计划", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@RequestBody EquipmentPlan equipmentPlan) {
        // 删除关联的设备信息
        Integer planId = equipmentPlan.getPlanId();
        equipmentPlanAssetService.removeByPlanId(planId);
        // 删除对应计划
        equipmentPlanService.removeById(planId);
        return R.ok();
    }
}


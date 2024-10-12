package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.BO.EquipmentPlanAssetBO;
import com.kakarote.build.equipment.entity.PO.EquipmentPlanAsset;
import com.kakarote.build.equipment.service.IEquipmentPlanAssetService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 计划维护设备信息 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
@RestController
@RequestMapping("/equipmentPlanAsset")
public class EquipmentPlanAssetController {

    @Autowired
    private IEquipmentPlanAssetService equipmentPlanAssetService;

    @PostMapping("/list")
    @ApiOperation("维护计划设备列表")
    @OperateLog(detail = "维护计划设备列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<EquipmentPlanAsset>> list(@RequestBody EquipmentPlanAssetBO planAssetBO) {
        return Result.ok(equipmentPlanAssetService.selectByPage(planAssetBO));
    }

}


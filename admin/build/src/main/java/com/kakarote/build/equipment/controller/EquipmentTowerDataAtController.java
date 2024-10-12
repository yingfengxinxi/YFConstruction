package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.BO.EquipmentTowerDataAtBO;
import com.kakarote.build.equipment.service.IEquipmentTowerDataAtService;
import com.kakarote.build.equipment.service.impl.EquipmentTowerDataAtServiceImpl;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 塔机设备实时数据表 前端控制器
 * </p>
 *
 * @author shz
 * @since 2022-03-22
 */
@RestController
@RequestMapping("/equipmentTowerDataAt")
public class EquipmentTowerDataAtController {

    @Autowired
    private IEquipmentTowerDataAtService equipmentTowerDataAtService;


    @PostMapping("/saveData")
    @ApiOperation("塔机实时数据上传接口")
    @OperateLog(detail = "塔机实时数据上传接口", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result saveData(@RequestBody List list) {
        return equipmentTowerDataAtService.saveData(list);
    }

    @PostMapping("/selectPage")
    @ApiOperation("塔机数据列表查询接口")
    @OperateLog(detail = "塔机数据列表查询接口", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result selectPage(@RequestBody EquipmentTowerDataAtBO towerDataAtBO){
        return Result.ok(equipmentTowerDataAtService.selectPage(towerDataAtBO));
    }
}


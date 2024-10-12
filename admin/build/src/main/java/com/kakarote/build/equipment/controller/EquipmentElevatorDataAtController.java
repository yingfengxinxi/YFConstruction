package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.BO.EquipmentElevatorDataAtBO;
import com.kakarote.build.equipment.entity.PO.EquipmentElevatorDataAt;
import com.kakarote.build.equipment.service.IEquipmentElevatorDataAtService;
import com.kakarote.build.equipment.service.impl.EquipmentElevatorDataAtServiceImpl;
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
 * 升降电梯设备实时数据表 前端控制器
 * </p>
 *
 * @author shz
 * @since 2022-03-22
 */
@RestController
@RequestMapping("/equipmentElevatorDataAt")
public class EquipmentElevatorDataAtController {

    @Autowired
    private IEquipmentElevatorDataAtService equipmentElevatorDataAtService;

    @PostMapping("/saveData")
    @ApiOperation("升降机实时数据上传接口")
    public Result saveData(@RequestBody List list) {
        return equipmentElevatorDataAtService.saveData(list);
    }

    /**
    * @Description:升降机数据列表查询接口
    * @author shz
    * @date 2022-4-14 9:59
    * @param elevatorDataAt
    * @return Result
    */
    @PostMapping("/selectPage")
    @ApiOperation("升降机数据列表查询接口")
    @OperateLog(detail = "升降机数据列表查询接口", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result selectPage(@RequestBody EquipmentElevatorDataAtBO elevatorDataAt){
        return Result.ok(equipmentElevatorDataAtService.selectPage(elevatorDataAt));
    }
}


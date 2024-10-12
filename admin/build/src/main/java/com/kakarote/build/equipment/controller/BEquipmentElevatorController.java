package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.PO.BEquipmentElevator;
import com.kakarote.build.equipment.service.IBEquipmentElevatorService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 升降电梯配置信息 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-12-27
 */
@RestController
@RequestMapping("/bEquipmentElevator")
public class BEquipmentElevatorController {
    @Autowired
    private IBEquipmentElevatorService elevatorService;
    @PostMapping("/get/{eqID}")
    @ApiOperation("查询升降电梯扩展详情")
    @OperateLog(detail = "查询升降电梯扩展详情", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result get(@PathVariable Integer eqID) {
        List<BEquipmentElevator> towerList = elevatorService.lambdaQuery().eq(BEquipmentElevator::getEquipmentId,eqID).list();
        if(towerList.size() > 0){
            return Result.ok(towerList.get(0));
        }
        return Result.ok();
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新升降电梯扩展信息")
    @OperateLog(detail = "保存或更新升降电梯扩展信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result saveOrUpdate(@RequestBody BEquipmentElevator elevator) {

        if (elevator.getId() == null) {
            elevatorService.save(elevator);
        } else {
            elevatorService.updateById(elevator);
        }
        return Result.ok();
    }

    @PostMapping("/delByEqID/{eqID}")
    @ApiOperation("删除升降电梯扩展详情")
    @OperateLog(detail = "删除升降电梯扩展详情", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delByEqID(@PathVariable Integer eqID) {
        Map map = new HashMap<>();
        map.put("equipment_id",eqID);
        elevatorService.getBaseMapper().deleteByMap(map);
        return Result.ok();
    }
}


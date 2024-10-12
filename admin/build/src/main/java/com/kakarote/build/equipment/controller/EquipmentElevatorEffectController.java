package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.BO.EquipmentElevatorEffectBO;
import com.kakarote.build.equipment.service.IEquipmentElevatorEffectService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shz
 * @since 2021-12-29
 */
@RestController
@RequestMapping("/equipmentElevatorEffect")
public class EquipmentElevatorEffectController {

    @Autowired
    private IEquipmentElevatorEffectService equipmentElevatorEffectService;

    /**
    * @Description:载重统计
    * @author shz
    * @date 2021-12-31 11:45
    * @param 
    * @return 
    */
    @GetMapping("/selectDZTJ/{id}")
    @ApiOperation("载重统计")
    public Result selectDZTJ(@PathVariable Integer id){
        return Result.ok(equipmentElevatorEffectService.selectDZTJ(id));
    }

    /**
    * @Description:今日运行统计
    * @author shz
    * @date 2021-12-31 11:46
    * @param 
    * @return 
    */
    @GetMapping("/selectJRGX/{id}")
    @ApiOperation("今日运行统计")
    public Result selectJRGX(@PathVariable Integer id){
        return Result.ok(equipmentElevatorEffectService.selectJRGX(id));
    }

    /**
    * @Description:升降机司机信息
    * @author shz
    * @date 2021-12-31 11:47
    * @param 
    * @return 
    */
    @GetMapping("/selectSJXX/{code}")
    @ApiOperation("升降机司机信息")
    public Result selectSJXX(@PathVariable String code){
        return Result.ok(equipmentElevatorEffectService.selectSJXX(code));
    }

    @PostMapping("/saveData")
    @ApiOperation("塔机实时数据上传接口")
    @OperateLog(detail = "塔机实时数据上传接口", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result saveData(@RequestBody List list) {
        return equipmentElevatorEffectService.saveData(list);
    }

    @PostMapping("/selectPage")
    @ApiOperation("升降机功效列表查询接口")
    @OperateLog(detail = "升降机功效列表查询接口", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result selectPage(@RequestBody EquipmentElevatorEffectBO elevatorEffectBO){
        return Result.ok(equipmentElevatorEffectService.selectPage(elevatorEffectBO));
    }
}


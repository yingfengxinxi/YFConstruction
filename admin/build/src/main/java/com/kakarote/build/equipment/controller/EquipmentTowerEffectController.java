package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.BO.EquipmentTowerEffectBO;
import com.kakarote.build.equipment.service.IEquipmentTowerEffectService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shz
 * @since 2021-12-22
 */
@RestController
@RequestMapping("/equipmentTowerEffect")
public class EquipmentTowerEffectController {

    @Autowired
    private IEquipmentTowerEffectService equipmentTowerEffectService;

    /**
    * @Description:吊重力矩统计
    * @author shz
    * @date 2021-12-28 11:47
    * @param 
    * @return 
    */
    @GetMapping("/selectDZTJ/{id}")
    @ApiOperation("吊重力矩统计")
    public Result selectDZTJ(@PathVariable Integer id){
        Map map = equipmentTowerEffectService.selectDZTJ(id);
        return Result.ok(map);
    }

    /**
    * @Description:塔吊今日运转数据
    * @author shz
    * @date 2021-12-28 14:19
    * @param 
    * @return 
    */
    @GetMapping("/selectJRGX/{id}")
    @ApiOperation("塔吊今日运转数据")
    public Result selectJRGX(@PathVariable Integer id){
        Map map = equipmentTowerEffectService.selectJRGX(id);
        return Result.ok(map);
    }

    /**
     * @Description:塔吊司机信息
     * @author shz
     * @date 2021-12-28 14:19
     * @param
     * @return
     */
    @GetMapping("/selectSJXX/{code}")
    @ApiOperation("塔吊司机信息")
    public Result selectSJXX(@PathVariable String code){
        Map map = equipmentTowerEffectService.selectSJXX(code);
        return Result.ok(map);
    }

    @PostMapping("/saveData")
    @ApiOperation("塔机实时数据上传接口")
    @OperateLog(detail = "塔机实时数据上传接口", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result saveData(@RequestBody List list) {
        return equipmentTowerEffectService.saveData(list);
    }

    @PostMapping("/selectPage")
    @ApiOperation("塔机功效列表查询接口")
    @OperateLog(detail = "塔机功效列表查询接口", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result selectPage(@RequestBody EquipmentTowerEffectBO towerEffectBO){
        return Result.ok(equipmentTowerEffectService.selectPage(towerEffectBO));
    }
}


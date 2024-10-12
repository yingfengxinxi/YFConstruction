package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.PO.EquipmentTowerCrane;
import com.kakarote.build.equipment.service.impl.EquipmentTowerCraneServiceImpl;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 塔机配置表 前端控制器
 * </p>
 *
 * @author shz
 * @since 2021-11-18
 */
@RestController
@RequestMapping("/equipmentTowerCrane")
public class EquipmentTowerCraneController {

    @Autowired
    private EquipmentTowerCraneServiceImpl equipmentTowerCraneService;

    @PostMapping("/getByDeviceId/{id}")
    @ApiOperation("查询塔机配置")
    @OperateLog(detail = "查询塔机配置", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable Integer id) {
        return Result.ok(equipmentTowerCraneService.getByDeviceId(id));
    }

    @PostMapping("/save")
    @ApiOperation("更新塔机配置")
    @OperateLog(detail = "更新塔机配置", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result save(@RequestBody EquipmentTowerCrane equipmentTowerCrane){
        equipmentTowerCraneService.saveOrUpdate(equipmentTowerCrane);
        return Result.ok();
    }

    @PostMapping("/getTdgx")
    @ApiOperation("查询塔吊工效")
    @OperateLog(detail = "查询塔吊工效", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getTdgx(@RequestBody Map<String, Object> map){
        return Result.ok(equipmentTowerCraneService.getTdgx(map));
    }

    @PostMapping("/getTzphb")
    @ApiOperation("查询吊装排行榜")
    @OperateLog(detail = "查询吊装排行榜", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getTzphb(@RequestBody Map<String, Object> map){
        return Result.ok(equipmentTowerCraneService.getTzphb(map));
    }

    @PostMapping("/getQtdzxlfx")
    @ApiOperation("查询群塔吊装分析")
    @OperateLog(detail = "查询群塔吊装分析", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getQtdzxlfx(@RequestBody Map<String, Object> map){
        return Result.ok(equipmentTowerCraneService.getQtdzxlfx(map));
    }

    @PostMapping("/getXhdzcs")
    @ApiOperation("查询循环调转次数")
    @OperateLog(detail = "查询循环调转次数", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getXhdzcs(@RequestBody Map<String, Object> map){
        return Result.ok(equipmentTowerCraneService.getXhdzcs(map));
    }

    @PostMapping("/getDtzlztbfb")
    @ApiOperation("查询吊装重量、载重百分比")
    @OperateLog(detail = "查询吊装重量、载重百分比", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getDtzlztbfb(@RequestBody Map<String, Object> map){
        return Result.ok(equipmentTowerCraneService.getDtzlztbfb(map));
    }

    @PostMapping("/getHsztddzcs")
    @ApiOperation("近四周塔吊吊装次数")
    @OperateLog(detail = "近四周塔吊吊装次数", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getHsztddzcs(@RequestBody Map<String, Object> map){
        return Result.ok(equipmentTowerCraneService.getHsztddzcs(map));
    }
}


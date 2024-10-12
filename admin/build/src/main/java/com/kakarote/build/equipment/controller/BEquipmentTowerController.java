package com.kakarote.build.equipment.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.build.equipment.entity.PO.BEquipmentTower;
import com.kakarote.build.equipment.entity.PO.EquipmentAssets;
import com.kakarote.build.equipment.service.IBEquipmentTowerService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 塔机配置表 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-12-22
 */
@RestController
@RequestMapping("/bEquipmentTower")
public class BEquipmentTowerController {
    @Autowired
    private IBEquipmentTowerService towerService;
    @PostMapping("/get/{eqID}")
    @ApiOperation("查询塔机扩展详情")
    @OperateLog(detail = "查询塔机扩展详情", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result get(@PathVariable Integer eqID) {
        List<BEquipmentTower> towerList = towerService.lambdaQuery().eq(BEquipmentTower::getEquipmentId,eqID).list();
        if(towerList.size() > 0){
            return Result.ok(towerList.get(0));
        }
        return Result.ok();
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新塔机扩展信息")
    @OperateLog(detail = "保存或更新塔机扩展信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result saveOrUpdate(@RequestBody BEquipmentTower tower) {

        if (tower.getId() == null) {
            towerService.save(tower);
        } else {
            towerService.updateById(tower);
        }
        return Result.ok();
    }

    @PostMapping("/delByEqID/{eqID}")
    @ApiOperation("删除塔机扩展详情")
    @OperateLog(detail = "删除塔机扩展详情", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delByEqID(@PathVariable Integer eqID) {
        Map map = new HashMap<>();
        map.put("equipment_id",eqID);
        towerService.getBaseMapper().deleteByMap(map);
        return Result.ok();
    }
}


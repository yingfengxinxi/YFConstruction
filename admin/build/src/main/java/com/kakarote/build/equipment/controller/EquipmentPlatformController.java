package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.PO.EquipmentPlatform;
import com.kakarote.build.equipment.service.IEquipmentPlatformService;
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
 * 卸料平台配置表 前端控制器
 * </p>
 *
 * @author shz
 * @since 2022-08-29
 */
@RestController
@RequestMapping("/equipmentPlatform")
public class EquipmentPlatformController {

    @Autowired
    private IEquipmentPlatformService platformService;

    @PostMapping("/get/{eqID}")
    @ApiOperation("查询卸料平台扩展详情")
    @OperateLog(detail = "查询卸料平台扩展详情", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result get(@PathVariable Integer eqID) {
        List<EquipmentPlatform> towerList = platformService.lambdaQuery().eq(EquipmentPlatform::getEquipmentId,eqID).list();
        if(towerList.size() > 0){
            return Result.ok(towerList.get(0));
        }
        return Result.ok();
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新塔机扩展信息")
    @OperateLog(detail = "保存或更新塔机扩展信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result saveOrUpdate(@RequestBody EquipmentPlatform tower) {
        if (tower.getId() == null) {
            platformService.save(tower);
        } else {
            platformService.updateById(tower);
        }
        return Result.ok();
    }

    @PostMapping("/delByEqID/{eqID}")
    @ApiOperation("删除塔机扩展详情")
    @OperateLog(detail = "删除塔机扩展详情", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delByEqID(@PathVariable Integer eqID) {
        Map map = new HashMap<>();
        map.put("equipment_id",eqID);
        platformService.getBaseMapper().deleteByMap(map);
        return Result.ok();
    }

}


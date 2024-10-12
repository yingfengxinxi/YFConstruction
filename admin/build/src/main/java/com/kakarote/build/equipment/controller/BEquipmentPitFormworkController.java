package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.PO.BEquipmentPitFormwork;
import com.kakarote.build.equipment.service.IBEquipmentPitFormworkService;
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
 * 深基坑及高支模扩展信息 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-12-27
 */
@RestController
@RequestMapping("/bEquipmentPitFormwork")
public class BEquipmentPitFormworkController {
    @Autowired
    private IBEquipmentPitFormworkService pitFormworkService;
    @PostMapping("/get/{eqID}")
    @ApiOperation("查询基坑/高支模扩展详情")
    @OperateLog(detail = "查询基坑/高支模扩展详情", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result get(@PathVariable Integer eqID) {
        List<BEquipmentPitFormwork> towerList = pitFormworkService.lambdaQuery().eq(BEquipmentPitFormwork::getEquipmentId,eqID).list();
        if(towerList.size() > 0){
            return Result.ok(towerList.get(0));
        }
        return Result.ok();
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新基坑/高支模扩展信息")
    @OperateLog(detail = "保存或更新基坑/高支模扩展信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result saveOrUpdate(@RequestBody BEquipmentPitFormwork pitFormwork) {

        if (pitFormwork.getId() == null) {
            pitFormworkService.save(pitFormwork);
        } else {
            pitFormworkService.updateById(pitFormwork);
        }
        return Result.ok();
    }

    @PostMapping("/delByEqID/{eqID}")
    @ApiOperation("删除基坑/高支模扩展详情")
    @OperateLog(detail = "删除基坑/高支模扩展详情", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delByEqID(@PathVariable Integer eqID) {
        Map map = new HashMap<>();
        map.put("equipment_id",eqID);
        pitFormworkService.getBaseMapper().deleteByMap(map);
        return Result.ok();
    }
}


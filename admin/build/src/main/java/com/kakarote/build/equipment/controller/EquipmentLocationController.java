package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.BO.EquipmentLocationBO;
import com.kakarote.build.equipment.entity.PO.EquipmentAssets;
import com.kakarote.build.equipment.entity.PO.EquipmentLocation;
import com.kakarote.build.equipment.service.IEquipmentAssetsService;
import com.kakarote.build.equipment.service.IEquipmentLocationService;
import com.kakarote.core.common.*;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备位置信息 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
@RestController
@RequestMapping("/equipmentLocation")
public class EquipmentLocationController {

    @Autowired
    private IEquipmentLocationService locationService;

    @Autowired
    private IEquipmentAssetsService assetsService;

    @PostMapping("/queryTreeList")
    @ApiOperation("查询设备位置信息树形数据")
    @OperateLog(detail = "查询设备位置信息树形数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> queryTreeList() {
        List<Map<String, Object>> list = locationService.queryAllList(0);
        return Result.ok(list);
    }
    @PostMapping("/queryTreeListByParentId/{parentId}")
    @ApiOperation("查询设备位置信息树形数据(懒加载)")
    @OperateLog(detail = "查询设备位置信息树形数据(懒加载)", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> queryTreeListByParentId(@PathVariable Integer parentId,@RequestBody Map<String,Object> map) {
        List<Map<String, Object>> list = locationService.queryTreeListByParentId(parentId ,map);
        return Result.ok(list);
    }
    @PostMapping("/get/{id}")
    @ApiOperation("查询位置详情")
    @OperateLog(detail = "查询位置详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<EquipmentLocation> get(@PathVariable Integer id) {
        EquipmentLocation location = locationService.selectById(id);
        return Result.ok(location);
    }

    @PostMapping("/save")
    @ApiOperation("保存位置信息")
    @OperateLog(detail = "保存位置信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result save(@RequestBody EquipmentLocation equipmentLocation) {
        if (equipmentLocation.getLocationId() == null) {
            locationService.save(equipmentLocation);
        } else {
            equipmentLocation.setUpdateUserId(UserUtil.getUserId());
            locationService.updateById(equipmentLocation);
        }
        return Result.ok();
    }

    @PostMapping("/delete")
    @ApiOperation("删除位置信息")
    @OperateLog(detail = "删除位置信息", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@RequestBody EquipmentLocation location) {
        // 核实是否有关联数据
        List<EquipmentAssets> list = assetsService.lambdaQuery().eq(EquipmentAssets::getLocationId, location.getLocationId()).list();
        if (list.size() > 0) {
            return R.error(-1, "该位置被【"+list.size()+"】个资产设备关联，不得删除");
        }
        locationService.removeById(location.getLocationId());
        return R.ok();
    }

    @PostMapping("/groupQueryTreeList")
    @ApiOperation("查询设备位置信息树形数据")
    @OperateLog(detail = "查询设备位置信息树形数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> groupQueryTreeList(@RequestBody EquipmentLocationBO equipmentLocationBO) {
        List<Map<String, Object>> list = locationService.groupQueryAllList(0, equipmentLocationBO);
        return Result.ok(list);
    }
}


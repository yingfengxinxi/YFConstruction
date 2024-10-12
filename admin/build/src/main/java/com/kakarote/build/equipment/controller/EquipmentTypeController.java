package com.kakarote.build.equipment.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.build.equipment.entity.PO.EquipmentAssets;
import com.kakarote.build.equipment.entity.PO.EquipmentType;
import com.kakarote.build.equipment.service.IEquipmentAssetsService;
import com.kakarote.build.equipment.service.IEquipmentTypeService;
import com.kakarote.core.common.*;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备类型信息 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
@RestController
@RequestMapping("/equipmentType")
public class EquipmentTypeController {
    @Autowired
    private IEquipmentTypeService typeService;

    @Autowired
    private IEquipmentAssetsService assetsService;

    @PostMapping("/queryTreeList")
    @ApiOperation("查询设备位置信息树形数据")
    @OperateLog(detail = "查询设备位置信息树形数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> queryTreeList() {
        List<Map<String, Object>> list = typeService.queryAllList(0);
        return Result.ok(list);
    }
    @PostMapping("/queryTreeListByParentId/{parentId}")
    @ApiOperation("查询设备位置信息树形数据")
    @OperateLog(detail = "查询设备位置信息树形数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> queryTreeListByParentId(@PathVariable Integer parentId) {
        List<Map<String, Object>> list = typeService.queryTreeListByParentId(parentId);
        return Result.ok(list);
    }
    @PostMapping("/get/{id}")
    @ApiOperation("查询位置详情")
    @OperateLog(detail = "查询位置详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<EquipmentType> get(@PathVariable Integer id) {
        EquipmentType location = typeService.selectById(id);
        return Result.ok(location);
    }

    @PostMapping("/save")
    @ApiOperation("保存位置信息")
    @OperateLog(detail = "保存位置信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result save(@RequestBody EquipmentType equipmentType) {

        if (equipmentType.getTypeId() == null) {
            // 类型编号重复校验
            LambdaQueryWrapper<EquipmentType> codeQueryWrapper = new LambdaQueryWrapper<>();
            codeQueryWrapper.eq(EquipmentType::getTypeCode, equipmentType.getTypeCode());
            List<EquipmentType> listOne = typeService.list(codeQueryWrapper);
            if (listOne != null && listOne.size()> 0) {
                throw new CrmException(500, "类型编号已存在，请重新输入!");
            }

            // 类型编号重复校验
            LambdaQueryWrapper<EquipmentType> nameQueryWrapper = new LambdaQueryWrapper<>();
            nameQueryWrapper.eq(EquipmentType::getTypeName, equipmentType.getTypeName());
            List<EquipmentType> listTwo = typeService.list(nameQueryWrapper);
            if (listTwo != null && listTwo.size()> 0) {
                throw new CrmException(500, "类型名称已存在，请重新输入!");
            }

            typeService.save(equipmentType);
        } else {
            equipmentType.setUpdateUserId(UserUtil.getUserId());
            typeService.updateById(equipmentType);
        }
        return Result.ok();
    }

    @PostMapping("/delete")
    @ApiOperation("删除位置信息")
    @OperateLog(detail = "删除位置信息", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@RequestBody EquipmentType equipmentType) {
        // 核实是否有关联数据
        List<EquipmentAssets> list = assetsService.lambdaQuery().eq(EquipmentAssets::getLocationId, equipmentType.getTypeId()).list();
        if (list.size() > 0) {
            return R.error(-1, "该位置被【"+list.size()+"】个资产设备关联，不得删除");
        }
        typeService.removeById(equipmentType.getTypeId());
        return R.ok();
    }

    /**
     * 集团数据使用
     * @return
     */
    @PostMapping("/queryTreeGroupList")
    @ApiOperation("查询设备位置信息树形数据")
    @OperateLog(detail = "查询设备位置信息树形数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> queryTreeGroupList() {
        List<Map<String, Object>> list = typeService.queryAllGroupList(0);
        return Result.ok(list);
    }

    @PostMapping("/getPlaneById/{id}")
    @ApiOperation("查询位置详情")
    @OperateLog(detail = "查询位置详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<EquipmentType> getPlaneById(@PathVariable Integer id) {
        EquipmentType location = typeService.getPlaneById(id);
        return Result.ok(location);
    }

    /**
    * @Description:查询所有设备类型
    * @author shz
    * @date 2023-5-15 14:50
    * @param 
    * @return 
    */
    @GetMapping("/selectAll")
    @ApiOperation("查询所有设备类型")
    @OperateLog(detail = "查询所有设备类型", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List> selectAll() {
        List location = typeService.list();
        return Result.ok(location);
    }

    @GetMapping("/selectAllMonitorType")
    @ApiOperation("查询所有监测类型")
    @OperateLog(detail = "查询所有监测类型", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectAllMonitorType(){
        List<EquipmentType> location = typeService.selectAllMonitorType();
        return Result.ok(location);
    }
}


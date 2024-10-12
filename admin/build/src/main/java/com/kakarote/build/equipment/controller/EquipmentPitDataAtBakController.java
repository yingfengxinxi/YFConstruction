package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.BO.EquipmentPitDataAtBakBO;
import com.kakarote.build.equipment.service.IEquipmentPitDataAtBakService;
import com.kakarote.build.equipment.service.impl.EquipmentPitDataAtBakServiceImpl;
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
 * 设备监测-基坑设备实时数据表 前端控制器
 * </p>
 *
 * @author shz
 * @since 2022-03-30
 */
@RestController
@RequestMapping("/equipmentPitDataAtBak")
public class EquipmentPitDataAtBakController {

    @Autowired
    private IEquipmentPitDataAtBakService equipmentPitDataAtBakService;

    @PostMapping("/saveData")
    @ApiOperation("基坑设备实时数据接口")
    @OperateLog(detail = "基坑设备实时数据上传接口", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result saveData(@RequestBody List list) {
        return equipmentPitDataAtBakService.saveData(list);
    }

//    @PostMapping("/selectPage")
//    @ApiOperation("升降机数据列表查询接口")
//    @OperateLog(detail = "升降机数据列表查询接口", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
//    public Result selectPage(@RequestBody EquipmentPitDataAtBakBO pitDataAt){
//        return Result.ok(equipmentPitDataAtBakService.selectPage(pitDataAt));
//    }
}


package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.BO.EquipmentHelmetGpsBO;
import com.kakarote.build.equipment.entity.PO.EquipmentHelmetGps;
import com.kakarote.build.equipment.service.IEquipmentHelmetGpsService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 智慧安全帽-定位数据 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/equipmentHelmetGps")
public class EquipmentHelmetGpsController {

    @Autowired
    private IEquipmentHelmetGpsService iEquipmentHelmetGpsService;

    @PostMapping("/list")
    @ApiOperation("查询只能安全帽数据")
    @OperateLog(detail = "查询只能安全帽数据", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<BasePage<EquipmentHelmetGps>> list(@RequestBody EquipmentHelmetGpsBO equipmentHelmetGpsBO) {
        return Result.ok(iEquipmentHelmetGpsService.selectByPage(equipmentHelmetGpsBO));
    }

    @PostMapping("/selectActionTrack")
    @ApiOperation("查询活动轨迹")
    @OperateLog(detail = "查询活动轨迹", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<List> selectActionTrack(@RequestBody EquipmentHelmetGpsBO equipmentHelmetGpsBO) {
        return Result.ok(iEquipmentHelmetGpsService.selectActionTrack(equipmentHelmetGpsBO));
    }

}


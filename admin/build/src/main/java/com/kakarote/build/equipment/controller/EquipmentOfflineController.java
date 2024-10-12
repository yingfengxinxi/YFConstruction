package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.BO.EquipmentOfflineBO;
import com.kakarote.build.equipment.service.IEquipmentOfflineService;
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

/**
 * <p>
 * 设备上下线记录表 前端控制器
 * </p>
 *
 * @author shz
 * @since 2022-08-02
 */
@RestController
@RequestMapping("/equipmentOffline")
public class EquipmentOfflineController {

    @Autowired
    IEquipmentOfflineService offlineService;

    @PostMapping("/selectPage")
    @ApiOperation("设备上下线记录列表查询接口")
    @OperateLog(detail = "设备上下线记录列表查询接口", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result selectPage(@RequestBody EquipmentOfflineBO offlineBO){
        return Result.ok(offlineService.selectPage(offlineBO));
    }
}


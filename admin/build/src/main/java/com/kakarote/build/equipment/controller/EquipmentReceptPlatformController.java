package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.PO.EquipmentReceptPlatform;
import com.kakarote.build.equipment.service.IEquipmentReceptPlatformService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 设备数据对接平台 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-11-11
 */
@RestController
@RequestMapping("/receptPlatform")
public class EquipmentReceptPlatformController {

    @Autowired
    private IEquipmentReceptPlatformService receptPlatformService;

    @PostMapping("/list")
    @ApiOperation("查询设备数据对接平台配置")
    @OperateLog(detail = "查询设备数据对接平台配置", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getList(@RequestBody EquipmentReceptPlatform platform) {
        List<EquipmentReceptPlatform> platformList = receptPlatformService.lambdaQuery().eq(EquipmentReceptPlatform::getStatus,platform.getStatus()).list();
        return Result.ok(platformList);
    }


}


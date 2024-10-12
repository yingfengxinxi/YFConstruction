package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.PO.EquipmentMonitorWarn;
import com.kakarote.build.equipment.service.IEquipmentMonitorWarnService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 设备监测-监测类型通用设置 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-10-27
 */
@RestController
@RequestMapping("/equipmentMonitorWarn")
public class EquipmentMonitorWarnController {

    @Autowired
    private IEquipmentMonitorWarnService warnService;

    @GetMapping("/getByType/{typeId}")
    @ApiOperation("查询监测类型配置")
    @OperateLog(detail = "查询监测类型配置", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getByTypeId(@PathVariable String typeId) {
        UserInfo userInfo = UserUtil.getUser();
        EquipmentMonitorWarn warn = warnService.getByTypeId(typeId, String.valueOf(userInfo.getTenantId())
                , String.valueOf(userInfo.getProjectId()));
        return Result.ok(warn);
    }

    @PostMapping("/save")
    @ApiOperation("保存监测类型配置")
    @OperateLog(detail = "保存监测类型配置", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdate(@RequestBody EquipmentMonitorWarn warn) {
        Integer id = warn.getId();
        if (id == null) {
            warn.setCreateUserId(UserUtil.getUserId());
            warnService.save(warn);
        } else {
            warn.setUpdateUserId(UserUtil.getUserId());
            warnService.updateById(warn);
        }

        return Result.ok();
    }
}


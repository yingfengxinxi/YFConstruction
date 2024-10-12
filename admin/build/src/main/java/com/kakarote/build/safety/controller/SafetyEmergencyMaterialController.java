package com.kakarote.build.safety.controller;


import com.kakarote.build.safety.entity.BO.BSafetySafetyDataBO;
import com.kakarote.build.safety.entity.BO.SafetyEmergencyMaterialBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyData;
import com.kakarote.build.safety.entity.PO.SafetyEmergencyMaterial;
import com.kakarote.build.safety.service.ISafetyEmergencyMaterialService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 应急物资管理 前端控制器
 * </p>
 *
 * @author wnj
 * @since 2021-04-06
 */
@RestController
@RequestMapping("/safetyEmergencyMaterial")
public class SafetyEmergencyMaterialController {
    @Autowired
    private ISafetyEmergencyMaterialService ISafetyEmergencyMaterialService;

    @RequestMapping("/list")
    @ApiOperation("查询应急物资管理列表")
    @OperateLog(detail = "查询应急物资管理列表", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<BasePage<SafetyEmergencyMaterial>> list(@RequestBody SafetyEmergencyMaterialBO SafetyEmergencyMaterialBO) {
        BasePage<SafetyEmergencyMaterial> list = ISafetyEmergencyMaterialService.selectList(SafetyEmergencyMaterialBO);
        return Result.ok(list);
    }

    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据", types = LogTypes.SYS, behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody SafetyEmergencyMaterial SafetyEmergencyMaterial) {
        SafetyEmergencyMaterial.setCreateBy(UserUtil.getUser().getRealname());
        SafetyEmergencyMaterial.setCreateTime(new Date());
        SafetyEmergencyMaterial.setCheckStatus(0);
        SafetyEmergencyMaterial.setOwnerUserId(UserUtil.getUser().getUserId());
        return Result.ok(ISafetyEmergencyMaterialService.add(SafetyEmergencyMaterial));
    }

    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据", types = LogTypes.SYS, behavior = LogBehavior.UPDATE)
    public Result edit(@RequestBody SafetyEmergencyMaterial SafetyEmergencyMaterial) {
        return Result.ok(ISafetyEmergencyMaterialService.edit(SafetyEmergencyMaterial));
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除数据")
    @OperateLog(detail = "删除数据", types = LogTypes.SYS, behavior = LogBehavior.DELETE)
    public Result remove(@PathVariable("id") String id) {

        return Result.ok(ISafetyEmergencyMaterialService.delete(id));
    }

    /**
     * 根据数据主键ID查询信息
     *
     * @param id 数据主键
     * @return
     */
    @GetMapping(value = "/selectById/{id}")
    @ApiOperation("查询单条数据详细")
    @OperateLog(detail = "查询单条数据详细", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result getInfo(@PathVariable String id) {
        return Result.ok(ISafetyEmergencyMaterialService.selectById(id));
    }

}

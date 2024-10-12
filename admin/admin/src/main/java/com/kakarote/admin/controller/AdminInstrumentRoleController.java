package com.kakarote.admin.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.admin.service.IAdminInstrumentInfoService;
import com.kakarote.admin.service.IAdminInstrumentRoleService;
import com.kakarote.core.common.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 仪表盘权限表 前端控制器
 * </p>
 *
 * @author author
 * @since 2020-12-08
 */
@RestController
@RequestMapping("/adminInstrumentRole")
public class AdminInstrumentRoleController {

    @Autowired
    private IAdminInstrumentRoleService adminInstrumentRoleService;

    @PostMapping("/query/{roleId}")
    @ApiOperation("查询角色仪表盘设置")
    @OperateLog(detail = "查询角色仪表盘设置--租户管理员用",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<JSONObject> query(@PathVariable("roleId") Integer roleId) {
        JSONObject object = adminInstrumentRoleService.queryModelSort(roleId);
        return Result.ok(object);
    }

    @PostMapping("/setModelSort")
    @ApiOperation("修改角色仪表盘排序")
    @OperateLog(detail = "修改角色仪表盘排序--租户管理员用",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result setModelSort(@RequestBody JSONObject jsonObject) {
        adminInstrumentRoleService.setModelSort(jsonObject);
        return R.ok();
    }

    @PostMapping("/queryModelByRoleIds")
    @ApiOperation("查询角色仪表盘设置")
    @OperateLog(detail = "查询角色仪表盘设置--首页展示用",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public JSONObject queryModelByRoleIds(@RequestBody List<Integer> roleIds) {
        return adminInstrumentRoleService.queryModelByRoleIds(roleIds);
    }

    @PostMapping("/getMaxDataType")
    @ApiOperation("根据用户角色获取仪表盘数据权限")
    @OperateLog(detail = "根据用户角色获取仪表盘数据权限--首页展示用",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Integer getMaxDataType(@RequestBody List<Integer> roleIds) {
        return adminInstrumentRoleService.getMaxDataType(roleIds);
    }
}


package com.kakarote.admin.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.admin.service.IAdminRoleMenuDpService;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shz
 * @since 2022-09-07
 */
@RestController
@RequestMapping("/adminRoleMenuDp")
public class AdminRoleMenuDpController {

    @Autowired
    private IAdminRoleMenuDpService adminRoleMenuDpService;

    @PostMapping("/updateRoleMenu")
    @ApiOperation("保存角色菜单关系")
    public Result updateRoleMenu(@RequestBody Map map) {
        adminRoleMenuDpService.updateRoleMenu(map);
        return R.ok();
    }

    @GetMapping("/getByRoleId/{roleId}")
    @ApiOperation("获取角色权限")
    public Result getByRoleId(@PathVariable("roleId") Long roleId){
        JSONObject object = new JSONObject().fluentPut("role_id", roleId);
        return R.ok(adminRoleMenuDpService.listByMap(object));
    }

    @GetMapping("/getByUser")
    @ApiOperation("获取角色权限")
    public Result getByUser(String platform){
        return R.ok(adminRoleMenuDpService.getByUser());
    }
}


package com.kakarote.admin.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.admin.entity.BO.AdminDeptBO;
import com.kakarote.admin.entity.BO.AdminUserBO;
import com.kakarote.admin.entity.PO.AdminDept;
import com.kakarote.admin.entity.VO.AdminDeptVO;
import com.kakarote.admin.entity.VO.AdminUserVO;
import com.kakarote.admin.service.IAdminMenuService;
import com.kakarote.admin.service.IAdminTenantService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/adminTenant")
@Api(tags = "租户模块")
public class AdminTenantController {

    @Autowired
    private IAdminMenuService adminMenuService;
    @Autowired
    private IAdminTenantService adminTenantService;

    @PostMapping("/queryDeptList")
    @ApiOperation("查询部门列表")
    @OperateLog(detail="查询部门列表",types= LogTypes.SYS,behavior=LogBehavior.SELECT)
    public Result<List<AdminDeptVO>> queryDeptList() {
        List<AdminDeptVO> deptList = adminTenantService.queryDeptList();
        return Result.ok(deptList);
    }

    @PostMapping("/queryUserList")
    @ApiOperation("查询员工列表")
    @OperateLog(detail="查询员工列表",types= LogTypes.SYS,behavior=LogBehavior.SELECT)
    public Result<BasePage<AdminUserVO>> queryUserList(@RequestBody AdminUserBO adminUserBO) {
        return R.ok(adminTenantService.queryUserList(adminUserBO));
    }

    @RequestMapping("/getMenuList/{tenantId}")
    @ApiOperation("查询全部菜单--租户权限分配用")
    @OperateLog(detail="查询全部菜单--租户权限分配用",types= LogTypes.SYS,behavior=LogBehavior.SELECT)
    public Result<JSONObject> getMenuListByType(@PathVariable("tenantId") Integer tenantId) {
        JSONObject byType = adminMenuService.getAllMenuList(tenantId);
        return Result.ok(byType);
    }

    @PostMapping("/getRoleByType")
    @ApiOperation("通过部门Id查询权限")
    @OperateLog(detail="通过部门Id查询权限",types= LogTypes.SYS,behavior=LogBehavior.SELECT)
    public Result<List<AdminDept>> getRoleByType() {
        List<AdminDept> DeptByType = adminTenantService.getRoleByType();
        return R.ok(DeptByType);
    }

    @PostMapping("/updateTenantMenu")
    @ApiOperation("保存部门（租户）菜单关系")
    @OperateLog(detail="保存部门（租户）菜单关系",types= LogTypes.SYS,behavior=LogBehavior.UPDATE)
    public Result updateTenantMenu(@RequestBody AdminDept adminDept){
        adminTenantService.updateDeptMenu(adminDept);
        return R.ok();
    }

    @PostMapping("/addTenant")
    @ApiOperation("新增租户")
    @OperateLog(detail="新增租户",types= LogTypes.SYS,behavior=LogBehavior.INSERT)
    @Transactional(rollbackFor = Exception.class)
    public Result addTenant(@RequestBody @Valid AdminDeptBO adminDept) {
        adminTenantService.addTenant(adminDept);
        return R.ok();
    }

    @PostMapping("/addTenantAdmin")
    @ApiOperation("新增租户管理员")
    @OperateLog(detail="新增租户管理员",types= LogTypes.SYS,behavior=LogBehavior.INSERT)
    @GlobalTransactional(rollbackFor = Exception.class,timeoutMills = 120000)
    public Result addTenantAdmin(@RequestBody AdminUserVO adminUserVO) {
        adminTenantService.addTenantAdmin(adminUserVO);
        return R.ok();
    }
}


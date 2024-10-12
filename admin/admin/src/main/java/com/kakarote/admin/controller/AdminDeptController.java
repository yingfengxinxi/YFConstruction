package com.kakarote.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.kakarote.admin.entity.BO.AdminDeptBO;
import com.kakarote.admin.entity.BO.AdminDeptQueryBO;
import com.kakarote.admin.entity.PO.AdminDept;
import com.kakarote.admin.entity.VO.AdminDeptVO;
import com.kakarote.admin.service.IAdminDeptService;
import com.kakarote.admin.service.impl.AdminMenuServiceImpl;
import com.kakarote.core.common.*;
import com.kakarote.core.feign.admin.entity.SimpleDept;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@RestController
@RequestMapping("/adminDept")
@Api(tags = "部门管理相关接口")
public class AdminDeptController {

    @Autowired
    private IAdminDeptService adminDeptService;
    @Autowired
    private AdminMenuServiceImpl adminMenuService;

    @PostMapping("/queryDeptTree")
    @ApiOperation("查询部门列表树")
    @OperateLog(detail = "查询部门列表树", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<List<AdminDeptVO>> queryDeptTree(@RequestBody AdminDeptQueryBO queryBO) {
        List<AdminDeptVO> deptList = adminDeptService.queryDeptTree(queryBO);
        return Result.ok(deptList);
    }

    @PostMapping("/addDept")
    @ApiOperation("新增部门")
    @OperateLog(detail = "新增部门", types = LogTypes.SYS, behavior = LogBehavior.INSERT)
    public Result addDept(@RequestBody @Valid AdminDeptBO adminDept) {
        Integer deptId = adminDeptService.addDept(adminDept);
        return Result.ok();
    }

    @PostMapping("/setDept")
    @ApiOperation("修改部门")
    @OperateLog(detail = "修改部门（租户）", types = LogTypes.SYS, behavior = LogBehavior.UPDATE)
    public Result setDept(@RequestBody @Valid AdminDeptBO adminDept) {
        adminDeptService.setDept(adminDept);
        return Result.ok();
    }

    @PostMapping("/deleteDept/{deptId}")
    @ApiOperation("删除部门")
    @OperateLog(detail = "删除部门", types = LogTypes.SYS, behavior = LogBehavior.DELETE)
    public Result deleteDept(@PathVariable("deptId") Integer deptId) {
        adminDeptService.deleteDept(deptId);
        return Result.ok();
    }

    @RequestMapping("/getNameByDeptId")
    @ApiExplain("根据部门ID获取部门名称")
    @OperateLog(detail = "根据部门ID获取部门名称", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result getNameByDeptId(Integer deptId) {
        return R.ok(adminDeptService.getNameByDeptId(deptId));
    }

    @RequestMapping("/getPIdByUser")
    @ApiExplain("根据登录用户获取部门父ID")
    @OperateLog(detail = "根据登录用户获取部门父ID", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result getPIdByUser() {
        return R.ok(adminDeptService.getPIdByUser());
    }

    @RequestMapping("/queryChildDeptId")
    @ApiExplain("根据部门ID下的子部门")
    @OperateLog(detail = "根据部门ID下的子部门", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<List<Integer>> queryChildDeptId(@NotNull Integer deptId) {
        return R.ok(adminDeptService.queryChildDept(deptId));
    }

    @PostMapping("/queryDeptByIds")
    @ApiExplain("根据部门ID获取用户")
    @OperateLog(detail = "根据部门ID获取用户", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<List<SimpleDept>> queryDeptByIds(@RequestBody List<Integer> ids) {
        List<SimpleDept> simpleDepts = adminDeptService.queryDeptByIds(ids);
        return R.ok(simpleDepts);
    }

    @RequestMapping("/getNameByDeptIdForAPP/{deptId}")
    @ApiExplain("根据部门ID获取部门名称-app端")
    @OperateLog(detail = "根据部门ID获取部门名称-app端", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result getNameByDeptIdForAPP(@PathVariable("deptId") Integer deptId) {
        return R.ok(adminDeptService.getNameByDeptId(deptId));
    }

    @RequestMapping("/getParentId/{deptId}")
    @ApiExplain("根据部门ID获取上级部门ID")
    //@OperateLog(detail = "根据部门ID获取上级部门ID", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result getParentId(@PathVariable("deptId") Integer deptId) {
        AdminDept adminDept = adminDeptService.getById(deptId);
        if (adminDept != null) {
            return R.ok(adminDept.getPid());
        }
        return R.ok();
    }

    @PostMapping("/addDeptMap")
    @ApiOperation("同步账号:新增部门")
    @OperateLog(detail = "同步账号:新增部门", types = LogTypes.SYS, behavior = LogBehavior.INSERT)
    public Integer addDept(@RequestBody Map map) {
        String name = map.get("name").toString();
        //是否已存在
        List<AdminDept> list = adminDeptService.lambdaQuery().eq(AdminDept::getName,name).list();
        if(list.size() > 0){
            return -1;
        }
        AdminDeptBO adminDept = new AdminDeptBO();
        BeanUtil.copyProperties(map, adminDept);
        Integer deptId = adminDeptService.addDept(adminDept);
        return deptId;
    }
}


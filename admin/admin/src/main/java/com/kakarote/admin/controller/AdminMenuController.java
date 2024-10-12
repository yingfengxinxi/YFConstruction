package com.kakarote.admin.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.admin.common.AdminRoleTypeEnum;
import com.kakarote.admin.entity.PO.AdminMenu;
import com.kakarote.admin.entity.VO.AdminMenuVO;
import com.kakarote.admin.service.IAdminMenuService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 后台菜单表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@RestController
@RequestMapping("/adminMenu")
@Api(tags = "菜单模块")
public class AdminMenuController {

    @Autowired
    private IAdminMenuService adminMenuService;

    @RequestMapping("/getMenuListByType/{type}")
    @ApiOperation("根据类型查询菜单")
    public Result<JSONObject> getMenuListByType(@PathVariable("type") Integer type) {
        AdminRoleTypeEnum typeEnum = AdminRoleTypeEnum.parse(type);
        JSONObject byType = adminMenuService.getMenuListByType(typeEnum);
        return Result.ok(byType);
    }
    @RequestMapping("/getMenuListByTenant")
    @ApiOperation("根据角色所在租户ID查询菜单")
    @OperateLog(detail = "根据角色所在租户ID查询菜单--租户用",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<JSONObject> getMenuListByType() {
        JSONObject byType = adminMenuService.getMenuListByTenant();
        return Result.ok(byType);
    }

    @RequestMapping("/getWorkMenuList")
    @ApiOperation("查询项目管理菜单")
    public Result<JSONObject> getWorkMenuList() {
        JSONObject byType = adminMenuService.getMenuListByType(AdminRoleTypeEnum.WORK);
        return Result.ok(byType);
    }

    @RequestMapping("/queryMenuId")
    public Result<Integer> queryMenuId(@RequestParam("realm1") String realm1,@RequestParam("realm2") String realm2,
                                       @RequestParam("realm3") String realm3){
        Integer menuId = adminMenuService.queryMenuId(realm1,realm2,realm3);
        return Result.ok(menuId);

    }


    @GetMapping("/queryByParentId/{parentId}")
    @ApiOperation("根据上级查询菜单")
    public Result<List<AdminMenu>> queryByParentId(@PathVariable("parentId") Integer parentId) {
        List<AdminMenu> list = adminMenuService.queryByParentId(parentId);
        return Result.ok(list);
    }
    @GetMapping("/queryByParentIdTenant/{parentId}")
    @ApiOperation("根据上级查询菜单")
    @OperateLog(detail="菜单查询",types=LogTypes.SYS,behavior=
            LogBehavior.SELECT)
    public Result<List<AdminMenu>> queryByParentIdTenant(@PathVariable("parentId") Integer parentId) {
        List<AdminMenu> list = adminMenuService.queryByParentIdTenant(parentId);
        return Result.ok(list);
    }

    @PostMapping("/saveMenu")
    @ApiOperation("保存菜单")
    public Result<Integer> saveMenu(@RequestBody AdminMenu adminMenu) {
        Integer i = adminMenuService.saveMenu(adminMenu);
        return Result.ok(i);
    }
    @PostMapping("/updateMenu")
    @ApiOperation("更新菜单")
    public Result<Integer> updateMenu(@RequestBody AdminMenu adminMenu) {
        Integer i = adminMenuService.updateMenu(adminMenu);
        return Result.ok(i);
    }
    @GetMapping("/deleteMenu/{menuId}")
    @ApiOperation("删除菜单")
    public Result<Object> deleteMenu(@PathVariable String menuId) {
        return adminMenuService.deleteMenu(menuId);
    }

    @GetMapping("/getMenuById/{menuId}")
    @ApiOperation("获取菜单详情")
    public Result<Object> getMenuById(@PathVariable Integer menuId) {
        return adminMenuService.getMenuById(menuId);
    }

    @PostMapping("/queryHeaderMenuList")
    @ApiOperation("置顶应用")
    public Result<Object> queryHeaderMenuList() {
        List<AdminMenu> list = adminMenuService.queryHeaderMenuList(UserUtil.getUserId());
        return Result.ok(list);
    }

    @PostMapping("/queryAllMenuList")
    @ApiOperation("全部应用")
    public Result<Object> queryAllMenuList() {
        List<AdminMenu> list = adminMenuService.queryAllAppList();
        return Result.ok(list);
    }
    @PostMapping("/queryAppList")
    @ApiOperation("应用管理")
    @OperateLog(detail="应用查询",types=LogTypes.SYS,behavior=
            LogBehavior.SELECT)
    public Result<Object> queryAppList() {
        List<AdminMenu> list = adminMenuService.queryAppList();
        return Result.ok(list);
    }
    @PostMapping("/queryMenuListForLogin")
    @ApiOperation("所有权限菜单")
    public Result<Object> querMenuList(@RequestBody UserInfo userInfo) {
        List<AdminMenu> list = adminMenuService.queryMenuListForLogin(userInfo);
        return Result.ok(list);
    }

    @PostMapping("/queryMenuListByAuth")
    @ApiOperation("所有权限菜单")
    public Result<Object> queryMenuListByAuth() {
        List<AdminMenu> list = adminMenuService.queryMenuListForLogin(UserUtil.getUser());
        return Result.ok(list);
    }

    @PostMapping("/queryMenuListByPath")
    @ApiOperation("根据路由查询菜单")
    public Result<Object> queryMenuListByPath(@RequestBody AdminMenu adminMenu) {
        List<AdminMenu> list = adminMenuService.queryMenuListByPath(adminMenu);
        return Result.ok(list);
    }
}


package com.kakarote.admin.controller;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.admin.common.AdminRoleTypeEnum;
import com.kakarote.admin.entity.PO.AdminMenu;
import com.kakarote.admin.entity.PO.AdminRole;
import com.kakarote.admin.entity.VO.AdminRoleVO;
import com.kakarote.admin.service.IAdminMenuService;
import com.kakarote.admin.service.IAdminRoleService;
import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@RestController
@RequestMapping("/adminRole")
@Api(tags = "角色模块")
public class AdminRoleController {
    @Autowired
    private IAdminRoleService adminRoleService;
    @Autowired
    private IAdminMenuService adminMenuService;

    @PostMapping("/auth")
    @ApiOperation("角色权限")
    public Result<JSONObject> auth() {
        JSONObject object = adminRoleService.auth(UserUtil.getUserId());
        return R.ok(object);
    }

    @PostMapping("/authFromUser")
    @ApiOperation("角色权限")
    public Result<JSONObject> authFromUser() {
        List<AdminMenu> adminMenus = adminMenuService.queryMenuListForLogin(UserUtil.getUser());
        return R.ok(parseOauthJson(adminMenus, 0));
    }

    /**
     * 对权限进行清洗组装
     * 递归组装
     * oauth:{crm:true}\{crm:{leaders:true}}\{crm:{leaders:{index:true}}}
     * router:{
     * path: '/404',
     * component: () => import('@/views/404'),
     * hidden: true,
     * meta: {
     * title: '统计分析',
     * icon: 'results-solid'
     * }
     * },
     *
     * @return
     */
    private JSONObject parseOauthJson(List<AdminMenu> adminAllMenus, Integer parentId) {
        //权限
        JSONObject oauthObject = new JSONObject();
        adminAllMenus.forEach(adminMenu -> {
            if (Objects.equals(parentId, adminMenu.getParentId())) {
                JSONObject object = parseOauthJson(adminAllMenus, adminMenu.getMenuId());
                if (!object.isEmpty()) {
                    if (parentId == 0) {
                        oauthObject.put(adminMenu.getRealm(), object);
                    } else {
                        oauthObject.put(adminMenu.getRealm(), object);
                    }
                } else {
                    if (parentId == 0) {
                        oauthObject.put(adminMenu.getRealm(), Boolean.TRUE);
                    } else {
                        oauthObject.put(adminMenu.getRealm(), Boolean.TRUE);
                    }
                }
            }
        });
        return oauthObject;
    }

    @PostMapping("/routerForUser")
    @ApiOperation("用户路由")
    public Result<JSONObject> routerFromUser() {
        JSONObject result = new JSONObject();
        List<AdminMenu> adminMenus = adminMenuService.queryMenuListForLogin(UserUtil.getUser());
        //菜单路由
        JSONObject routers = parseMenuJson(adminMenus, 0);
        result.put("routers", routers);
        //result.put("firstRoute", getFirstRoute(routers));//初次加载路由
        //动态路由
        JSONObject addRouters = parseRoutersJson(adminMenus, 0);
        result.put("addRouters", addRouters);
        return R.ok(result);
    }

    @PostMapping("/routerFromApp")
    @ApiOperation("APP端用户路由")
    public Result<JSONObject> routerFromApp() {
        JSONObject result = new JSONObject();
        List<AdminMenu> adminMenus = adminMenuService.queryMenuListForAppLogin(UserUtil.getUser());
        //菜单路由
//        JSONObject routers = parseMenuJson(adminMenus, 0);
//        result.put("routers", routers);
        //result.put("firstRoute", getFirstRoute(routers));//初次加载路由
        AdminMenu topMenu = adminMenus.stream().filter(m -> m.getMenuType() == 5 && m.getParentId() == 0).findFirst().get();
        //动态路由
        List<Object> addRouters = parseRoutersList(adminMenus, topMenu.getMenuId());
        result.put("addRouters", addRouters);
        return R.ok(result);
    }
    /**
     * @param adminAllMenus
     * @param parentId
     * @return
     */
    private JSONObject parseMenuJson(List<AdminMenu> adminAllMenus, Integer parentId) {
        //路由
        JSONObject routerObject = new JSONObject(16, true);
        adminAllMenus.forEach(adminMenu -> {
            if (Objects.equals(parentId, adminMenu.getParentId())
                    && (1 == (adminMenu.getMenuType()) || 2 == (adminMenu.getMenuType()))) {
                JSONObject router = new JSONObject();
                //处理path
                String path = adminMenu.getPath();
                if (!path.startsWith("/")) {
                    path += "/" + path;
                }
                router.put("path", path);
                if (!StringUtils.isEmpty(adminMenu.getComponent())) {
                    router.put("component", adminMenu.getComponent());
                }
                router.put("sort", adminMenu.getSort());
                router.put("menuType", adminMenu.getMenuType());
                router.put("name", adminMenu.getMenuId());
                //applicationId 应用ID
                router.put("applicationId", String.valueOf(adminMenu.getMenuId()));
                //meta
                JSONObject meta = new JSONObject();
                //处理hide
                String hide = adminMenu.getIsHide();
                if("1".equals(hide)){
                    router.put("hidden", true);
                    String activePath = adminMenu.getActivePath();
                    if(!StringUtils.isEmpty(activePath)){
                        meta.put("activeMenu", activePath);
                    }
                }
                //处理disabled
                String isDisabled = adminMenu.getIsDisabled();
                if("1".equals(isDisabled)){
                    meta.put("disabled", true);
                }
                meta.put("icon", adminMenu.getIcon());
                meta.put("title", adminMenu.getMenuName());
                meta.put("realm", adminMenu.getPath());
                router.put("meta", meta);
                List<JSONObject> object = new ArrayList<>();
                object = getMenuChildrenJson(adminAllMenus, adminMenu.getMenuId());
                if (object.size() > 0) {
                    router.put("children", object);
                }
                routerObject.put(String.valueOf(adminMenu.getMenuId()), router);

            }
        });
        return routerObject;
    }

    /**
     * 获取孩子菜单
     *
     * @param adminAllMenus
     * @param parentId
     * @return
     */
    private List<JSONObject> getMenuChildrenJson(List<AdminMenu> adminAllMenus, Integer parentId) {
        //路由
        List<JSONObject> jsonArray = new ArrayList<>();
        adminAllMenus.forEach(adminMenu -> {
            if (Objects.equals(parentId, adminMenu.getParentId())
                    && (1 == (adminMenu.getMenuType()) || 2 == (adminMenu.getMenuType()))
                    && !StringUtils.isEmpty(adminMenu.getPath())) {
                JSONObject router = new JSONObject();
                //处理path
                String childPath = adminMenu.getPath();
                router.put("path", childPath);
                router.put("name", adminMenu.getMenuId());
                router.put("menuType", adminMenu.getMenuType());
                router.put("component", adminMenu.getComponent());
                //meta
                JSONObject meta = new JSONObject();
                //处理hide
                String hide = adminMenu.getIsHide();
                if("1".equals(hide)){
                    router.put("hidden", true);
                    String activePath = adminMenu.getActivePath();
                    if(!StringUtils.isEmpty(activePath)){
                        meta.put("activeMenu", activePath);
                    }
                }
                //处理disabled
                String isDisabled = adminMenu.getIsDisabled();
                if("1".equals(isDisabled)){
                    meta.put("disabled", true);
                }
                meta.put("icon", adminMenu.getIcon());
                meta.put("title", adminMenu.getMenuName());
                meta.put("realm", adminMenu.getPath());
                router.put("meta", meta);
                List<JSONObject> childrenJson = getMenuChildrenJson(adminAllMenus, adminMenu.getMenuId());
                if (childrenJson.size() > 0) {
                    router.put("children", childrenJson);
                    //router.put("hidden",true);
                }
                jsonArray.add(router);
            }
        });
        return jsonArray;
    }
    /**
     * @param adminAllMenus
     * @param parentId
     * @return
     */
    private JSONObject parseRoutersJson(List<AdminMenu> adminAllMenus, Integer parentId) {
        //路由
        JSONObject routerObject = new JSONObject(16, true);
        adminAllMenus.forEach(adminMenu -> {
            if (Objects.equals(parentId, adminMenu.getParentId())
                    && (1 == (adminMenu.getMenuType()) || 2 == (adminMenu.getMenuType())
                        || 5 == (adminMenu.getMenuType()))) {
                List<JSONObject> jsonArray = new ArrayList<>();
                JSONObject router = new JSONObject();
                //处理path
                String path = adminMenu.getPath();
                if (!path.startsWith("/")) {
                    path += "/" + path;
                }
                router.put("path", path);
                if (!StringUtils.isEmpty(adminMenu.getComponent())) {
                    router.put("component", adminMenu.getComponent());
                }
                router.put("name", adminMenu.getMenuId());
                //applicationId 应用ID
                router.put("applicationId", String.valueOf(adminMenu.getMenuId()));

                //meta
                JSONObject meta = new JSONObject();
                //处理hide
                String hide = adminMenu.getIsHide();
                if("1".equals(hide)){
                    router.put("hidden", true);
                    String activePath = adminMenu.getActivePath();
                    if(!StringUtils.isEmpty(activePath)){
                        meta.put("activeMenu", activePath);
                    }
                }
                //处理disabled
                String isDisabled = adminMenu.getIsDisabled();
                if("1".equals(isDisabled)){
                    meta.put("disabled", true);
                }
                meta.put("icon", adminMenu.getIcon());
                meta.put("title", adminMenu.getMenuName());
                meta.put("realm", adminMenu.getPath());
                router.put("meta", meta);
                List<JSONObject> object = new ArrayList<>();
                object = getRouterChildrenJson(adminAllMenus, adminMenu.getMenuId(), "");
                if (object.size() > 0) {
                    router.put("children", object);
                }
                //20210924 By PangZhen 追加：一级菜单为页面时将路由改为二级
                if(adminMenu.getParentId()==0 && object.size()==0){
//                    System.out.println("===="+adminMenu.getMenuName());
                    router.put("path", "/" + adminMenu.getPath());
                    router.put("component", "layout/CommonLayou");
                    ArrayList<JSONObject> list = new ArrayList();
                    JSONObject routeCopy =  (JSONObject) router.clone();
                    routeCopy.put("path",adminMenu.getPath());
                    routeCopy.put("component", adminMenu.getComponent());
                    list.add(routeCopy);
                    router.put("children", list);
//                    System.out.println("===="+router.toJSONString());
                }
                jsonArray.add(router);
                routerObject.put(String.valueOf(adminMenu.getMenuId()), jsonArray);

            }
        });
        return routerObject;
    }
    private List<Object> parseRoutersList(List<AdminMenu> adminAllMenus, Integer parentId) {
        //路由
        List<Object> routerObject = new ArrayList<>();
        adminAllMenus.forEach(adminMenu -> {
            if (Objects.equals(parentId, adminMenu.getParentId())
                    && (1 == (adminMenu.getMenuType()) || 2 == (adminMenu.getMenuType())
                    || 5 == (adminMenu.getMenuType()))) {
                List<JSONObject> jsonArray = new ArrayList<>();
                JSONObject router = new JSONObject();
                //处理path
                String path = adminMenu.getPath();
                if (!path.startsWith("/")) {
                    path += "/" + path;
                }
                router.put("path", path);
                if (!StringUtils.isEmpty(adminMenu.getComponent())) {
                    router.put("component", adminMenu.getComponent());
                }
                router.put("name", adminMenu.getMenuId());
                //applicationId 应用ID
                router.put("applicationId", String.valueOf(adminMenu.getMenuId()));

                //meta
                JSONObject meta = new JSONObject();
                //处理hide
                String hide = adminMenu.getIsHide();
                if("1".equals(hide)){
                    router.put("hidden", true);
                    String activePath = adminMenu.getActivePath();
                    if(!StringUtils.isEmpty(activePath)){
                        meta.put("activeMenu", activePath);
                    }
                }
                //处理disabled
                String isDisabled = adminMenu.getIsDisabled();
                if("1".equals(isDisabled)){
                    meta.put("disabled", true);
                }
                meta.put("icon", adminMenu.getIcon());
                meta.put("title", adminMenu.getMenuName());
                meta.put("realm", adminMenu.getPath());
                router.put("meta", meta);
                List<JSONObject> object = new ArrayList<>();
                object = getRouterChildrenJson(adminAllMenus, adminMenu.getMenuId(), "");
                if (object.size() > 0) {
                    router.put("children", object);
                }
                //20210924 By PangZhen 追加：一级菜单为页面时将路由改为二级
                if(adminMenu.getParentId()==0 && object.size()==0){
//                    System.out.println("===="+adminMenu.getMenuName());
                    router.put("path", "/" + adminMenu.getPath());
                    router.put("component", "layout/CommonLayou");
                    ArrayList<JSONObject> list = new ArrayList();
                    JSONObject routeCopy =  (JSONObject) router.clone();
                    routeCopy.put("path",adminMenu.getPath());
                    routeCopy.put("component", adminMenu.getComponent());
                    list.add(routeCopy);
                    router.put("children", list);
//                    System.out.println("===="+router.toJSONString());
                }
                jsonArray.add(router);
                routerObject.add(jsonArray);

            }
        });
        return routerObject;
    }
    /**
     * 获取孩子路由
     *
     * @param adminAllMenus
     * @param parentId
     * @return
     */
    private List<JSONObject> getRouterChildrenJson(List<AdminMenu> adminAllMenus, Integer parentId, String path) {
        //路由
        List<JSONObject> jsonArray = new ArrayList<>();
        adminAllMenus.forEach(adminMenu -> {
            if (Objects.equals(parentId, adminMenu.getParentId())
                    //&& (1 == (adminMenu.getMenuType()) || 2 == (adminMenu.getMenuType()))
                    && !StringUtils.isEmpty(adminMenu.getPath())) {
                JSONObject router = new JSONObject();
                //处理path
                String childPath = adminMenu.getPath();
                if (!"".equals(path) && !childPath.startsWith("/")) {
                    childPath = path + "/" + childPath;
                }
                router.put("path", childPath);
                if (!StringUtils.isEmpty(adminMenu.getComponent())) {
                    router.put("component", adminMenu.getComponent());
                }
                router.put("name", adminMenu.getMenuId());
                //meta
                JSONObject meta = new JSONObject();
                //处理hide
                String hide = adminMenu.getIsHide();
                if("1".equals(hide)){
                    router.put("hidden", true);
                    String activePath = adminMenu.getActivePath();
                    if(!StringUtils.isEmpty(activePath)){
                        meta.put("activeMenu", activePath);
                    }
                }
                //处理disabled
                String isDisabled = adminMenu.getIsDisabled();
                if("1".equals(isDisabled)){
                    meta.put("disabled", true);
                }
                meta.put("icon", adminMenu.getIcon());
                meta.put("title", adminMenu.getMenuName());
                meta.put("realm", adminMenu.getPath());
                router.put("meta", meta);
                jsonArray.add(router);
                List<JSONObject> childrenJson = getRouterChildrenJson(adminAllMenus, adminMenu.getMenuId(), childPath);
                /*if (object.size() > 0) {
                    router.put("children", object);
                    //router.put("hidden",true);
                }*/
                jsonArray.addAll(childrenJson);
            }
        });
        return jsonArray;
    }

    @PostMapping("/queryNoAuthMenu")
    @ApiOperation("获取未授权的菜单")
    public Result<List<String>> queryNoAuthMenu(@RequestParam("userId") @NotNull Long userId) {
        return R.ok(adminRoleService.queryNoAuthMenu(userId));
    }

    @PostMapping("/getAllRoleList")
    @ApiOperation("全局角色查询")
    public Result<List<AdminRoleVO>> getAllRoleList() {
        List<AdminRoleVO> allRoleList = adminRoleService.getAllRoleList();
        return R.ok(allRoleList);
    }

    @PostMapping("/getRoleTypeList")
    @ApiOperation("获取角色类型列表")
    public Result<List<Map<String, Object>>> getRoleTypeList() {
        List<Map<String, Object>> data = new ArrayList<>(6);
        data.add(new JSONObject().fluentPut("name", "系统管理角色").fluentPut("roleType", 1));
        data.add(new JSONObject().fluentPut("name", "办公管理角色").fluentPut("roleType", 7));
        data.add(new JSONObject().fluentPut("name", "客户管理角色").fluentPut("roleType", 2));
        data.add(new JSONObject().fluentPut("name", "项目管理角色").fluentPut("roleType", 8));
        return R.ok(data);
    }

    @PostMapping("/getRoleByType/{type}")
    @ApiOperation("通过角色类型查询角色")
    public Result<List<AdminRole>> getRoleByType(@PathVariable("type") Integer type) {
        List<AdminRole> roleByType = adminRoleService.getRoleByType(AdminRoleTypeEnum.parse(type));
        return R.ok(roleByType);
    }

//    @PostMapping("/getRoleById") //改为getRoleByIdForRoleAuth
//    @ApiOperation("通过角色类型查询角色")
//    public Result<List<AdminRole>> getRoleById() {
//        List<AdminRole> roleByType = adminRoleService.getRoleById();
//        return R.ok(roleByType);
//    }

    @PostMapping("/getRoleByIdForRoleAuth")
    @ApiOperation("通过角色类型查询角色--角色权限管理用")
    public Result<List<AdminRole>> getRoleByIdForRoleAuth() {
        List<AdminRole> roleByType = adminRoleService.getRoleByIdForRoleAuth();
        return R.ok(roleByType);
    }

    @PostMapping("/getRoleList")
    @ApiOperation("查询角色列表")
    public Result<List<AdminRole>> getRoleList() {
        List<AdminRole> roleByType = adminRoleService.getRoleList();
        return R.ok(roleByType);
    }

    @PostMapping("/queryRoleByRoleType")
    @ApiExplain("通过角色类型查询角色")
    public Result<List<Integer>> queryRoleByRoleType(@RequestParam("type") Integer type) {
        List<AdminRole> recordList = adminRoleService.lambdaQuery().select(AdminRole::getRoleId).eq(AdminRole::getRoleType, type).list();
        return R.ok(recordList.stream().map(AdminRole::getRoleId).collect(Collectors.toList()));
    }

    @PostMapping("/queryDataType")
    @ApiExplain("查询数据权限")
    public Result<Integer> queryDataType(@RequestParam("userId") Long userId, @RequestParam("menuId") Integer menuId) {
        Integer dataType = adminRoleService.queryDataType(userId, menuId);
        return R.ok(dataType);
    }
    @PostMapping("/queryDataTypeByRealm")
    @ApiExplain("查询数据权限")
    public Result<Integer> queryDataTypeByRealm(@RequestParam("userId") Long userId, @RequestParam("realm") String realm) {
        Integer dataType = adminRoleService.queryDataTypeByRealm(userId, realm);
        return R.ok(dataType);
    }

    @PostMapping("/queryMaxDataType")
    @ApiExplain("查询数据权限")
    public Result<Integer> queryMaxDataType(@RequestParam("userId") Long userId, @RequestParam("menuId") Integer menuId) {
        Integer dataType = adminRoleService.queryMaxDataType(userId, menuId);
        return R.ok(dataType);
    }

    @PostMapping("/queryUserByAuth")
    @ApiExplain("查询数据权限")
    public Result<List<Long>> queryUserByAuth(@RequestParam("userId") Long userId, @RequestParam("realm") String realm) {
        List<Long> longs = adminRoleService.queryUserByAuth(userId, realm);
        return R.ok(longs);
    }

    @PostMapping("/add")
    @ApiOperation("添加角色")
    public Result add(@RequestBody AdminRole adminRole) {
        adminRoleService.add(adminRole);
        return R.ok();
    }

    @PostMapping("/update")
    @ApiOperation("修改角色")
    public Result update(@RequestBody AdminRole adminRole) {
        adminRoleService.add(adminRole);
        return R.ok();
    }

    @PostMapping("/delete")
    @ApiOperation("删除角色")
    public Result delete(@RequestParam("roleId") Integer roleId) {
        adminRoleService.delete(roleId);
        return R.ok();
    }

    @PostMapping("/copy")
    @ApiOperation("复制角色")
    public Result copy(@RequestParam("roleId") Integer roleId) {
        adminRoleService.copy(roleId);
        return R.ok();
    }

    @PostMapping("/relatedUser")
    @ApiOperation("角色关联员工")
    public Result relatedUser(@RequestParam("userIds") String userIds, @RequestParam("roleIds") String roleIds) {
        adminRoleService.relatedUser(StrUtil.splitTrim(userIds, Const.SEPARATOR), StrUtil.splitTrim(roleIds, Const.SEPARATOR));
        return R.ok();
    }

    @PostMapping("/unbindingUser")
    @ApiOperation("取消角色关联员工")
    public Result unbindingUser(@RequestParam("userId") Long userId, @RequestParam("roleId") Integer roleId) {
        adminRoleService.unbindingUser(userId, roleId);
        return R.ok();
    }

    @PostMapping("/updateRoleMenu")
    @ApiOperation("保存角色菜单关系")
    public Result updateRoleMenu(@RequestBody AdminRole adminRole) {
        adminRoleService.updateRoleMenu(adminRole);
        return R.ok();
    }

    @PostMapping(value = "/queryWorkRole")
    @ApiExplain("查询项目管理角色")
    public Result<Integer> queryWorkRole(@RequestParam("label") Integer label) {
        Integer role = adminRoleService.queryWorkRole(label);
        return R.ok(role);
    }

    @PostMapping(value = "/setWorkRole")
    @ApiExplain("设置项目管理角色")
    public Result setWorkRole(@RequestBody JSONObject object) {
        adminRoleService.setWorkRole(object);
        return R.ok();
    }

    @PostMapping(value = "/deleteWorkRole")
    @ApiExplain("删除项目管理角色")
    public Result deleteWorkRole(@RequestParam("roleId") Integer roleId) {
        adminRoleService.deleteWorkRole(roleId);
        return R.ok();
    }

    @PostMapping(value = "/queryProjectRoleList")
    @ApiOperation("查询项目管理角色列表")
    public Result<List<AdminRole>> queryProjectRoleList() {
        List<AdminRole> adminRoles = adminRoleService.queryProjectRoleList();
        return R.ok(adminRoles);
    }

    @PostMapping(value = "/queryWorkRoleList")
    @ApiOperation("查询项目管理角色列表")
    public Result<List<AdminRole>> queryWorkRoleList() {
        List<AdminRole> adminRoles = adminRoleService.queryRoleList();
        return R.ok(adminRoles);
    }
}


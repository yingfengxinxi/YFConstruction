package com.kakarote.authorization.service;

import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.kakarote.authorization.entity.AdminUserStatusBO;
import com.kakarote.authorization.entity.PO.AdminMenu;
import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.UserInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author zhang
 * 向admin模块请求企业信息
 */
@Component
@FeignClient(name = "admin")
public interface AdminUserService {

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 结果信息
     */
    @RequestMapping(value = "/adminUser/findByUsername")
    Result findByUsername(@RequestParam("username") String username);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 结果信息
     */
    @RequestMapping(value = "/adminUser/findByUsernameByTenantID")
    Result findByUsernameByTenantID(@RequestParam("username") String username,@RequestParam("tenantId") Integer tenantId);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 结果信息
     */
    @RequestMapping(value = "/adminUser/findByNameConfig")
    Result<Map<String,Object>> findByNameConfig(@RequestParam("username") String username, @RequestParam("configId") String configId);

    /**
     * 通过用户ID查询所属角色
     * @param userId 用户ID
     * @return data
     */
    @RequestMapping(value = "/adminUser/queryUserRoleIds")
    Result<List<Integer>> queryUserRoleIds(@RequestParam("userId") Long userId);

    @PostMapping("/adminUser/setUserStatus")
    @ApiOperation("禁用启用")
    Result setUserStatus(@RequestBody AdminUserStatusBO adminUserStatusBO);


    /**
    * 通过用户ID查询没有权限的菜单
    * @date 2020/8/20 15:25
    * @param userId
    * @return 结果信息
    **/
    @PostMapping(value = "/adminRole/queryNoAuthMenu")
    Result<List<String>> queryNoAuthMenu(@RequestParam("userId") Long userId);
    /**
     * 通过用户ID查询所有权限的菜单
     * @date 2020/11/23
     * @param userInfo
     * @return 结果信息
     **/
    @PostMapping(value = "/adminMenu/queryMenuListForLogin")
    Result<List<AdminMenu>> queryMenuListForLogin(@RequestBody UserInfo userInfo);

    @GetMapping("/adminDeptConfig/getConfig/{configId}")
    @ApiExplain("获取租户配置")
    Result<JSONObject> queryDeptConfig(@PathVariable("configId") String configId);

    /**
     * 根据部门ID获取上级部门ID
     *
     * @param deptId 部门ID
     * @return 结果信息
     */
    @RequestMapping(value = "/adminDept/getParentId/{deptId}")
    @Cached(cacheType = CacheType.LOCAL)
    public Result<Integer> getParentIdByDeptId(@RequestParam("deptId") Integer deptId);


    /**
     * 切换项目
     */
    @PostMapping(value = "/adminCommon/changeProject/{projectId}")
    public Result changeProject(@PathVariable("projectId") Integer projectId);
}

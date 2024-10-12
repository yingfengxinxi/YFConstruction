package com.kakarote.authorization.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.authorization.entity.AdminUserStatusBO;
import com.kakarote.authorization.entity.PO.AdminMenu;
import com.kakarote.authorization.service.AdminUserService;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.UserInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhang
 * 向admin模块请求企业信息
 */
@Component
public class AdminUserServiceImpl implements AdminUserService {


    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 结果信息
     */
    @Override
    public Result findByUsername(String username) {
        return Result.error(SystemCodeEnum.SYSTEM_NO_FOUND);
    }

    @Override
    public Result findByUsernameByTenantID(String username, Integer tenantId) {
        return Result.error(SystemCodeEnum.SYSTEM_NO_FOUND);
    }

    @Override
    public Result findByNameConfig(String username,String configId) {
        return Result.error(SystemCodeEnum.SYSTEM_NO_FOUND);
    }

    /**
     * 通过用户ID查询所属角色
     *
     * @param userId 用户ID
     * @return data
     */
    @Override
    public Result<List<Integer>> queryUserRoleIds(Long userId) {
        return Result.ok(new ArrayList<>());
    }

    @Override
    public Result<List<String>> queryNoAuthMenu(Long userId) {
        return Result.ok(new ArrayList<>());
    }

    @Override
    public Result<List<AdminMenu>> queryMenuListForLogin(UserInfo userInfo) {
        return Result.ok(new ArrayList<>());
    }

    @Override
    public Result<JSONObject> queryDeptConfig(String configId) {
        return Result.ok(new JSONObject());
    }

    @Override
    public Result<Integer> getParentIdByDeptId(Integer deptId) {
        return Result.ok();
    }

    @Override
    public Result changeProject(Integer projectId) {
        return Result.ok();
    }

    @Override
    public Result setUserStatus(AdminUserStatusBO adminUserStatusBO) {
        return Result.ok();
    }

}

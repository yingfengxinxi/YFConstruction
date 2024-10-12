package com.kakarote.authorization.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.authorization.common.AuthException;
import com.kakarote.authorization.common.AuthorizationCodeEnum;
import com.kakarote.authorization.entity.AdminUserStatusBO;
import com.kakarote.authorization.entity.AuthorizationUser;
import com.kakarote.authorization.entity.VO.LoginVO;
import com.kakarote.authorization.service.AdminUserService;
import com.kakarote.authorization.service.DTalkLoginService;
import com.kakarote.authorization.service.DTalkService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.AdminLoginLog;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.service.impl.AdminLoginLogServiceImpl;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DTalkLoginServiceImpl implements DTalkLoginService {

    @Autowired
    private DTalkService dTalkService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private AdminLoginLogServiceImpl adminLoginLogService;

    @Override
    public Result getCorpId(String configId) {
        Result<JSONObject> result = adminUserService.queryDeptConfig(configId);
        if(result.hasSuccess()){
            //清洗敏感数据
            return Result.ok(result.getData().getString("CorpId"));
        }
        return result;
    }

    @Override
    public Result login(String code,String configId) {
        Integer tenantId = 0;
        String username = code;
        Result result = null;
        try {
            result = dTalkService.getPhone(code,configId);
            if(!result.hasSuccess()){
                return result;
            }
            username = result.getData().toString();
            result = adminUserService.findByNameConfig(username,configId);
            if(!result.hasSuccess()){
                return result;
            }
            //查询用户
            List<Map<String, Object>> users = (List<Map<String, Object>>)result.getData();
            if(users.size() < 1 ){//用户不存在
                result = Result.error(-1,"用户不存在");
                return result;
            }
            //todo 未考虑多租户
            UserInfo userInfo = BeanUtil.copyProperties(users.get(0),UserInfo.class);
            tenantId = userInfo.getTenantId().intValue();
            //生成Token
            String token = IdUtil.simpleUUID();
            if (userInfo.getStatus() == 0) {
                throw new CrmException(AuthorizationCodeEnum.AUTHORIZATION_USER_DISABLE_ERROR);
            }
            Long userId = userInfo.getUserId();
            userInfo.setRoles(adminUserService.queryUserRoleIds(userId).getData());
            userInfo.setNoAuthMenuUrls(adminUserService.queryNoAuthMenu(userId).getData());
            //部门相关
            userInfo.setDeptParentId(adminUserService.getParentIdByDeptId(userInfo.getDeptId()).getData());
            UserUtil.userToken(token, userInfo);
            if (userInfo.getStatus() == 2) {//未激活
                adminUserService.setUserStatus(AdminUserStatusBO.builder().status(1).ids(Collections.singletonList(userInfo.getUserId())).build());
            }
            result = Result.ok(new LoginVO().setAdminToken(token));
        } catch (Exception e) {
            throw e;
        }finally {
            AdminLoginLog adminLoginLog = new AdminLoginLog();
            adminLoginLog.setLoginUserName(username);
            adminLoginLog.setLoginTime(new Date());
            adminLoginLog.setIpAddress(BaseUtil.getRequest().getRemoteAddr());
            adminLoginLog.setDeviceType("APP");
            adminLoginLog.setTenantId(tenantId);
            if (result!=null && result.hasSuccess()) {
                adminLoginLog.setAuthResult(1);
            }else{
                adminLoginLog.setAuthResult(2);
            }
            adminLoginLogService.save(adminLoginLog);
        }
        return result;
    }
}

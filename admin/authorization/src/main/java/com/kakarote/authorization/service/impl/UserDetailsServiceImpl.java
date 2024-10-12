package com.kakarote.authorization.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.kakarote.authorization.common.AuthException;
import com.kakarote.authorization.common.AuthorizationCodeEnum;
import com.kakarote.authorization.entity.AdminUserStatusBO;
import com.kakarote.authorization.entity.AuthorizationUser;
import com.kakarote.authorization.entity.AuthorizationUserInfo;
import com.kakarote.authorization.entity.VO.LoginVO;
import com.kakarote.authorization.service.AdminUserService;
import com.kakarote.authorization.service.LoginService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.AdminLoginLog;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.message.MessageFeignService;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.service.IAdminLoginLogService;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author Administrator
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService, LoginService {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private IAdminLoginLogService adminLoginLogService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private Redis redis;

    @Autowired
    private MessageFeignService messageFeign;

    @Override
    @SuppressWarnings("unchecked")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Result result = adminUserService.findByUsername(username);
        if (result.hasSuccess()) {
            return new AuthorizationUser().setUserInfoList((List<Object>) result.getData());
        }
        throw new UsernameNotFoundException(null);
    }

    /**
     * 登录方法的处理
     *
     * @param user 用户对象
     * @return Result
     */
    @Override
    public Result login(AuthorizationUser user) {
        String token = IdUtil.simpleUUID();
        UserInfo userInfo = user.toUserInfo();
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
        return Result.ok(new LoginVO().setAdminToken(token));
    }


    /**
     * 登录方法的处理
     *
     * @param user 用户对象
     * @return Result
     */
    @Override
    public Result doLogin(AuthorizationUser user) {
        Integer tenantId = 0;
        Result result = null;
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername().trim(), user.getPassword().trim()));
            AuthorizationUserInfo userInfo = (AuthorizationUserInfo) authentication.getDetails();
            if (userInfo.getAuthorizationUserList().size() == 0) {
                result = Result.error(AuthorizationCodeEnum.AUTHORIZATION_LOGIN_NO_USER);
                return result;
            }
            AuthorizationUser authorizationUser = userInfo.getAuthorizationUserList().get(0);
            //用户所属多个租户时自动登录最近一次的租户
            if (userInfo.getAuthorizationUserList().size() > 1) {
                tenantId = adminLoginLogService.queryLast(user.getUsername().trim());
                for(AuthorizationUser temp : userInfo.getAuthorizationUserList()){
                    if(tenantId!=null && temp.getTenantId().intValue() == tenantId.intValue()){
                        authorizationUser = temp;
                        break;
                    }
                }
            }
            tenantId = authorizationUser.getTenantId().intValue();
            result = login(authorizationUser);
            System.out.println("======如手机端登录则需向推送服务注册别名======"+user.getCid());
            try {
                if(!StringUtils.isEmpty(user.getCid())){
                    //如手机端登录则需向推送服务注册别名
                    Map<String,String> map = new HashMap<>();
                    map.put("cid",user.getCid());
                    map.put("userid",authorizationUser.getUserId().toString());
                    messageFeign.registerCid(map);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        } catch (BadCredentialsException e) {
            result = Result.error(AuthorizationCodeEnum.AUTHORIZATION_LOGIN_NO_USER);
        } catch (AuthException e) {
            result = Result.error(e.getResultCode());
        }finally {
            AdminLoginLog adminLoginLog = new AdminLoginLog();
            adminLoginLog.setLoginUserName(user.getUsername());
            adminLoginLog.setLoginTime(new Date());
            adminLoginLog.setIpAddress(BaseUtil.getRequest().getRemoteAddr());
            adminLoginLog.setDeviceType("PC");
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

    @Override
    public Result logout() {
        return Result.ok();
    }

    @Override
    public Result changeTenant(Integer tenantId) {

        String token = IdUtil.simpleUUID();
        UserInfo userInfo =  UserUtil.getUser();
        Result result = adminUserService.findByUsername(userInfo.getUsername());
        if(!result.hasSuccess()){
            return result;
        }
        //查询用户
        List<Map<String, Object>> users = (List<Map<String, Object>>)result.getData();
        for(Map<String, Object> temp : users){
            if(Integer.parseInt(temp.get("tenantId").toString()) == tenantId){
                userInfo = BeanUtil.copyProperties(temp,UserInfo.class);
                break;
            }
        }
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

        AdminLoginLog adminLoginLog = new AdminLoginLog();
        adminLoginLog.setLoginUserName(userInfo.getUsername());
        adminLoginLog.setLoginTime(new Date());
        adminLoginLog.setIpAddress(BaseUtil.getRequest().getRemoteAddr());
        adminLoginLog.setDeviceType("PC");
        adminLoginLog.setTenantId(tenantId);
        adminLoginLog.setAuthResult(1);
        adminLoginLog.setLoginAddress("切换部门");
        adminLoginLogService.save(adminLoginLog);

        return Result.ok(new LoginVO().setAdminToken(token));
    }
}

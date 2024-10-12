package com.kakarote.authorization.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.authorization.entity.AuthorizationUser;
import com.kakarote.authorization.entity.AuthorizationUserInfo;
import com.kakarote.authorization.entity.VO.LoginVO;
import com.kakarote.authorization.service.AdminUserService;
import com.kakarote.authorization.service.WxAppLoginService;
import com.kakarote.authorization.service.WxAppService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.AdminLoginLog;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.service.impl.AdminLoginLogServiceImpl;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class WxAppLoginServiceImpl implements WxAppLoginService {

    @Autowired
    private WxAppService wxAppService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private AdminLoginLogServiceImpl adminLoginLogService;

    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * @param wxBind
     * @return username
     * password
     * name
     * phone
     */
    @Override
    public Result bind(JSONObject wxBind) {
        //校验User
        Result result = getUserInfo(wxBind);
        if (!result.hasSuccess()) {
            return result;
        }
        //生成绑定信息
        AuthorizationUser userInfo = (AuthorizationUser) result.getData();
        wxBind.put("password", userInfo.getPassword());
        wxBind.put("tenantId", userInfo.getTenantId());
        result = wxAppService.onBind(wxBind);
        if (!result.hasSuccess()) {
            return result;
        }
        //生成token
        userInfo.setWxappOpenId(result.getData().toString());
        String token = setToken(userInfo);
        return Result.ok(new LoginVO().setAdminToken(token));
    }

    @Override
    public Result login(String code) {
        Result result = wxAppService.onLogion(code);
        if (!result.hasSuccess()) {//绑定异常
            return result;
        }
        //绑定信息
        Map map = (Map) result.getData();
        JSONObject bindData = new JSONObject();
        BeanUtil.copyProperties(map, bindData);
        //校验被绑定用户状态 密码
        result = checkUserInfo(bindData);
        if (!result.hasSuccess()) {
            return result;
        }
        //生成token
        AuthorizationUser userInfo = (AuthorizationUser) result.getData();
        String token = setToken(userInfo);
        return Result.ok(new LoginVO().setAdminToken(token));
    }

    /**
     * 获取并校验账号信息
     *
     * @param wxBind
     * @return
     */
    private Result getUserInfo(JSONObject wxBind) {
        String username = wxBind.getString("username");
        AuthorizationUserInfo userInfo = null;
        boolean noUser = false;
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username,
                            wxBind.get("password")));
            userInfo = (AuthorizationUserInfo) authentication.getDetails();
            if(userInfo.getAuthorizationUserList().size() == 0){
                noUser = true;
            }
        }catch (Exception e){
            noUser = true;
        }
        if(noUser){
            return Result.error(1, "账号或密码错误");
        }
        AuthorizationUser authorizationUser = userInfo.getAuthorizationUserList().get(0);
        //todo 参考：多租户处理逻辑
        if(userInfo.getAuthorizationUserList().size() > 1){
            Integer tenantId = adminLoginLogService.queryLast(username);//最近一次登录时的tenantId
            for (AuthorizationUser user : userInfo.getAuthorizationUserList()) {
                if (user.getStatus() == 1) {//账号正常
                    if (tenantId != null && user.getTenantId().intValue() == tenantId.intValue()) {
                        authorizationUser = user;
                        break;
                    }
                }
            }
        }
        //如果authorizationUser为null,肯定账号全部被禁用了
        if (authorizationUser == null) {
            return Result.error(30, "绑定账号[" + username + "]已停用");
        }
        return Result.ok(authorizationUser);
    }

    /**
     * 获取并校验账号信息
     *
     * @param wxBind
     * @return
     */
    private Result checkUserInfo(JSONObject wxBind) {
        String username = wxBind.getString("username");
        Result<Map<String, Object>> result = adminUserService.findByUsernameByTenantID(username, wxBind.getInteger("tenantId"));
        if (!result.hasSuccess()) {
            return result;
        }
        Map<String, Object> userInfo = result.getData();
        //密码
        if (!wxBind.getString("password").equals(userInfo.get("password").toString())) {
            return Result.error(1, "账号或密码错误");
        }
        //状态
        if (Integer.valueOf(userInfo.get("status").toString()) != 1) {
            return Result.error(30, "绑定账号[" + username + "]已停用");
        }
        AuthorizationUser authorizationUser = new AuthorizationUser();
        BeanUtil.copyProperties(userInfo, authorizationUser);
        authorizationUser.setWxappOpenId(wxBind.getString("openid"));
        return Result.ok(authorizationUser);
    }

    private String setToken(AuthorizationUser user) {
        //生成Token
        String token = IdUtil.simpleUUID();
        UserInfo userInfo = user.toUserInfo();
        userInfo.setWxappOpenId(user.getWxappOpenId());
        Integer setFlag = 1;
        try {
            Long userId = userInfo.getUserId();
            userInfo.setRoles(adminUserService.queryUserRoleIds(userId).getData());
            userInfo.setNoAuthMenuUrls(adminUserService.queryNoAuthMenu(userId).getData());
            //部门相关
            userInfo.setDeptParentId(adminUserService.getParentIdByDeptId(userInfo.getDeptId()).getData());
            UserUtil.userToken(token, userInfo);
        } catch (Exception e) {
            setFlag = 2;
            throw e;
        } finally {
            AdminLoginLog adminLoginLog = new AdminLoginLog();
            adminLoginLog.setLoginUserName(userInfo.getUsername());
            adminLoginLog.setLoginTime(new Date());
            adminLoginLog.setIpAddress(BaseUtil.getRequest().getRemoteAddr());
            adminLoginLog.setDeviceType("WXAPP");
            adminLoginLog.setTenantId(userInfo.getTenantId().intValue());
            adminLoginLog.setAuthResult(setFlag);
            adminLoginLogService.save(adminLoginLog);
        }
        return token;
    }
}

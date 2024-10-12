package com.kakarote.authorization.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.kakarote.authorization.common.AesUtil;
import com.kakarote.authorization.common.AuthorizationCodeEnum;
import com.kakarote.authorization.entity.AuthorizationUser;
import com.kakarote.authorization.entity.VO.LoginVO;
import com.kakarote.authorization.service.AdminUserService;
import com.kakarote.authorization.service.LoginService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author zhangzhiwei
 * 添加权限的controller
 */
@RestController
@Api(tags = "用户登录相关接口")
public class AuthController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping(value = "/permission")
    public Result permission() {
        return Result.ok();
    }


    /**
     * 获取token 监管 =》 项目端
     */
    @GetMapping(value = "/loginProApp/{proId}")
    @ApiOperation(tags = "获取token", httpMethod = "GET", value = "/login/{proId}")
    public Result loginGotoProApp(@PathVariable Long proId) throws Exception {
        String token = IdUtil.simpleUUID();
        UserInfo userInfo =  UserUtil.getUser();
        //添加临时项目过滤标识
        userInfo.setEmpFilterPro(true);
        userInfo.setProjectId(proId);
        UserUtil.userToken(token, userInfo);
        //切换项目
        adminUserService.changeProject(Integer.parseInt(proId.toString()));
        return Result.ok(new LoginVO().setAdminToken(token));
    }

    /**
     * 登录方法，限流由sentinel处理
     */
    @PostMapping(value = "/login")
    @ApiOperation(tags = "用户登录", httpMethod = "POST", value = "/doLogin")
    public Result doLogin(@Valid @RequestBody AuthorizationUser user, HttpServletResponse response) throws Exception {

        if (StrUtil.trimToNull(user.getUsername()) == null) {
            return Result.error(AuthorizationCodeEnum.AUTHORIZATION_USERNAME_REQUIRED);
        }
        if (StrUtil.trimToNull(user.getPassword()) == null && StrUtil.trimToNull(user.getSmscode()) == null) {
            return Result.error(AuthorizationCodeEnum.AUTHORIZATION_PASSWORD_REQUIRED);
        }
        //用户名+密码传输解密
        user.setUsername(AesUtil.aesDecrypt(user.getUsername(), AesUtil.KEY));
        user.setPassword(AesUtil.aesDecrypt(user.getPassword(), AesUtil.KEY));
        Result result = loginService.doLogin(user);

        return result;
    }

    @PostMapping(value = "/loginWithoutEncrypt")
    @ApiOperation(tags = "用户登录非加密传输", httpMethod = "POST", value = "/doLogin")
    public Result doLoginWithoutEncrypt(@Valid @RequestBody AuthorizationUser user) throws Exception {
        if (StrUtil.trimToNull(user.getUsername()) == null) {
            return Result.error(AuthorizationCodeEnum.AUTHORIZATION_USERNAME_REQUIRED);
        }
        if (StrUtil.trimToNull(user.getPassword()) == null && StrUtil.trimToNull(user.getSmscode()) == null) {
            return Result.error(AuthorizationCodeEnum.AUTHORIZATION_PASSWORD_REQUIRED);
        }
        Result result = loginService.doLogin(user);

        return result;
    }

    @RequestMapping(value = "/logout")
    @ApiOperation(tags = "用户注销", httpMethod = "GET", value = "/logout")
    @ParamAspect
    public Result logout() {
        return loginService.logout();
    }

    @PostMapping(value = "/change")
    @OperateLog(detail = "切换用户", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result changeTenant(@RequestParam Integer tenantId) {
        return loginService.changeTenant(tenantId);
    }

    @PostMapping(value = "/reLogin")
    @OperateLog(detail = "重新登录", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result changeTenan() {
        Long tId = UserUtil.getUser().getTenantId();
        return loginService.changeTenant(Integer.parseInt(String.valueOf(tId)));
    }
}

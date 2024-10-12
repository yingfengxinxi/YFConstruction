package com.kakarote.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.admin.common.AdminCodeEnum;
import com.kakarote.admin.entity.BO.*;
import com.kakarote.admin.entity.PO.AdminConfig;
import com.kakarote.admin.entity.PO.AdminUser;
import com.kakarote.admin.entity.PO.AdminUserConfig;
import com.kakarote.admin.entity.VO.AdminUserVO;
import com.kakarote.admin.entity.VO.HrmSimpleUserVO;
import com.kakarote.admin.service.*;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.NoLoginException;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.build.BuildService;
import com.kakarote.core.feign.crm.service.CrmService;
import com.kakarote.core.feign.email.EmailService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.upload.UploadEntity;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@RestController
@RequestMapping("/adminUser")
@Api(tags = "员工管理相关接口")
@Slf4j
public class AdminUserController {

    @Autowired
    private IAdminUserService adminUserService;

    @Autowired
    private IAdminUserConfigService adminUserConfigService;

    @Autowired
    private IAdminFileService adminFileService;

    @Autowired
    private CrmService crmService;

    @Autowired
    private IAdminUserRoleService adminUserRoleService;

    @Autowired
    private BuildService buildService;

    @RequestMapping("/findByUsername")
    @ApiOperation(value = "通过name查询用户", httpMethod = "POST")
    public Result<List<Map<String, Object>>> findByUsername(String username) {
        List<Map<String, Object>> userInfoList = adminUserService.findByUsername(username);
        return Result.ok(userInfoList);
    }

    @RequestMapping("/findByUsernameByTenantID")
    @ApiOperation(value = "通过name查询用户", httpMethod = "POST")
    public Result<Map<String, Object>> findByUsernameByTenantID(String username, Integer tenantId) {
        List<AdminUser> adminUsers = adminUserService.lambdaQuery().eq(AdminUser::getUsername, username).eq(AdminUser::getTenantId, tenantId).list();
        if (adminUsers.size() < 1) {
            return Result.error(1, "账号或密码错误");
        }
        Map<String, Object> map = BeanUtil.beanToMap(adminUsers.get(0));
        return Result.ok(map);
    }

    @RequestMapping("/findByNameConfig")
    @ApiOperation(value = "通过name及配置Id查询用户", httpMethod = "POST")
    public Result<List<Map<String, Object>>> findByNameConfig(@RequestParam("username") String username, @RequestParam("configId") String configId) {
        List<Map<String, Object>> userInfoList = adminUserService.findByNameConfig(username, configId);
        return Result.ok(userInfoList);
    }

    @ApiOperation("通过条件分页查询员工列表")
    @PostMapping("/queryUserList")
    public Result<BasePage<AdminUserVO>> queryUserList(@RequestBody AdminUserBO adminUserBO) {
        return R.ok(adminUserService.queryUserList(adminUserBO));
    }

    @ApiExplain("通过条件分页查询员工列表")
    @PostMapping("/queryAllUserList")
    public Result<List<Long>> queryAllUserList() {
        List<AdminUserVO> adminUserBOList = adminUserService.queryUserList(null).getList();
        return R.ok(adminUserBOList.stream().map(AdminUserVO::getUserId).collect(Collectors.toList()));
    }

    @PostMapping("/setUser")
    @ApiOperation("修改用户")
    public Result setUser(@RequestBody AdminUserVO adminUserVO) {
        adminUserService.setUser(adminUserVO);
        return R.ok();
    }

    @PostMapping("/addUser")
    @ApiOperation("新增用户")
    @OperateLog(detail = "新增用户", types = LogTypes.SYS, behavior = LogBehavior.INSERT)
    public Result addUser(@RequestBody AdminUserVO adminUserVO) {
        return R.ok(adminUserService.addUser(adminUserVO));
    }

    @GetMapping("/delUser/{userId}")
    @ApiOperation("删除用户")
    @OperateLog(detail = "删除用户/租户管理员--系统管理员用", types = LogTypes.SYS, behavior = LogBehavior.DELETE)
    public Result delUser(@PathVariable("userId") Long userId) {

        adminUserService.delUser(userId);
        return R.ok();
    }

    @PostMapping("/usernameEdit")
    @ApiOperation("重置登录账号")
    public Result<Integer> usernameEdit(@RequestParam("id") Integer id, @RequestParam("username") String username, @RequestParam("password") String password) {
        Integer integer = adminUserService.usernameEdit(id, username, password);
        return R.ok(integer);
    }

    @PostMapping("/excelImport")
    @ApiOperation("excel导入员工")
    public Result<JSONObject> excelImport(@RequestParam("file") MultipartFile file) {
        JSONObject object = adminUserService.excelImport(file);
        return R.ok(object);
    }

    @PostMapping("/downExcel")
    @ApiOperation("excel下载错误数据")
    public void downExcel(@RequestParam("token") String token, HttpServletResponse response) {
        String path = FileUtil.getTmpDirPath() + "/" + token;
        if (FileUtil.exist(path)) {
            File file = FileUtil.file(path);
            final String fileName = file.getName();
            final String contentType = ObjectUtil.defaultIfNull(FileUtil.getMimeType(fileName), "application/octet-stream");
            BufferedInputStream in = null;
            try {
                in = FileUtil.getInputStream(file);
                ServletUtil.write(response, in, contentType, "import_error.xls");
            } finally {
                IoUtil.close(in);
            }
            FileUtil.del(path);
        }
    }

    @PostMapping("/hrmAddUser")
    @ApiOperation("从人力资源添加员工")
    public Result hrmAddUser(@RequestBody HrmAddUserBO hrmAddUserBO) {
        adminUserService.hrmAddUser(hrmAddUserBO);
        return R.ok();
    }

    @PostMapping("/setUserStatus")
    @ApiOperation("禁用启用")
    public Result setUserStatus(@RequestBody AdminUserStatusBO adminUserStatusBO) {
        adminUserService.setUserStatus(adminUserStatusBO);
        return R.ok();
    }

    @PostMapping("/resetPassword")
    @ApiOperation("重置密码")
    public Result resetPassword(@RequestBody AdminUserStatusBO adminUserStatusBO) {
        adminUserService.resetPassword(adminUserStatusBO);
        return R.ok();
    }

    @PostMapping("/updateImg")
    @ApiOperation("修改头像")
    public Result updateImg(@RequestParam("file") MultipartFile file) throws IOException {
        UploadEntity img = adminFileService.upload(file, null, "img", "0");
        AdminUser byId = adminUserService.getById(UserUtil.getUserId());
        byId.setImg(img.getUrl());
        adminUserService.updateById(byId);
        return R.ok();
    }

    @PostMapping("/updatePassword")
    @ApiOperation("修改登录密码")
    public Result updatePassword(@RequestParam("oldPwd") String oldPass, @RequestParam("newPwd") String newPass) {
        AdminUser adminUser = adminUserService.getById(UserUtil.getUserId());
        if (!UserUtil.verify(adminUser.getUsername() + oldPass, adminUser.getSalt(), adminUser.getPassword())) {
            return R.error(AdminCodeEnum.ADMIN_PASSWORD_ERROR);
        }
        adminUser.setPassword(newPass);
        return updateUser(adminUser);
    }

    @PostMapping("/updateUser")
    @ApiOperation("修改用户信息")
    public Result updateUser(@RequestBody AdminUser adminUser) {
        adminUserService.updateUser(adminUser);
        return R.ok();
    }

    @Autowired
    private IAdminDeptService deptService;

    @PostMapping("/queryLoginUser")
    @ApiOperation("查询当前登录用户")
    public Result<AdminUserVO> queryLoginUser(HttpServletRequest request) {
        String name = "readNotice";
        AdminUser user = adminUserService.getById(UserUtil.getUser().getUserId());
        if (user == null) {
            throw new NoLoginException();
        }
        AdminUserVO adminUser = BeanUtil.copyProperties(user, AdminUserVO.class);
        adminUser.setIsAdmin(UserUtil.isAdmin());
        AdminUserConfig userConfig = adminUserConfigService.queryUserConfigByName(name);
        adminUser.setIsReadNotice(userConfig != null ? userConfig.getStatus() : 0);
        adminUser.setPassword(null);
        String deptName = deptService.getNameByDeptId(adminUser.getDeptId());
        adminUser.setDeptName(deptName);
        adminUser.setParentName(UserCacheUtil.getUserName(adminUser.getParentId()));
        AdminConfig config = ApplicationContextHolder.getBean(IAdminConfigService.class).queryConfigByName("email");
        if (config != null && config.getStatus() == 1) {
            Integer data = ApplicationContextHolder.getBean(EmailService.class).getEmailId(adminUser.getUserId()).getData();
            adminUser.setEmailId(data);
        }
        //更新下用户信息
        String token = request.getHeader(Const.TOKEN_NAME);
        Integer projectId = user.getProjectId();
        UserInfo userInfo = UserUtil.getUser();
        if (projectId != null) {
            userInfo.setProjectId(Long.valueOf(projectId.toString()));
        } else {
            userInfo.setProjectId(null);
        }
        UserUtil.userToken(token, userInfo);
        /*UserInfo userInfo = UserUtil.getUser();
        adminUser.setOauth(userInfo.getOauth());
        adminUser.setRouters(userInfo.getRouters());*/
        //20201214:登录后保存CRM自定义字段数据至es
        try {
            //todo 屏蔽ES 22.03.15
            //crmService.initFieldDataToEs();
        } catch (Exception e) {
            //crm未启动时不应报错
            log.warn("登录后保存CRM自定义字段数据至es：失败");
        }

        adminUser.setDeptParentId(userInfo.getDeptParentId());
        adminUser.setWxappOpenId(userInfo.getWxappOpenId());

        // 获取用户角色类型
        List<Integer> roleTypeList = adminUserRoleService.selectRoleTypeByUserId(adminUser.getUserId());
        adminUser.setRoleTypeList(roleTypeList);
        //2022-09-05 邵化振 添加用户工种信息
        try {
            Result res = buildService.getInfocollectionByPhone(user.getMobile());
            Map map = (Map)res.getData();
            if(map != null){
                adminUser.setWorkTypeId((Integer) map.get("workTypeId"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return R.ok(adminUser);
    }

    /**
     * @description
     * @author wnj58 
     * @Params []
     * @updateTime 2022/4/11 10:08
     * @return com.kakarote.core.common.Result
     * @throws 
     */
    @PostMapping("/queryLoginUserFast")
    @ApiOperation("查询当前登录用户信息-快速")
    public Result queryLoginUserFast() {
      String js=  JSON.toJSONString(UserUtil.getUser());
      JSONObject jss=JSON.parseObject(js);
        return R.ok(jss);
    }

    @PostMapping("/queryLoginUserQuick")
    @ApiOperation("查询当前登录用户ID-快速")
    public Result<Long> queryLoginUserQuick() {
        return R.ok(UserUtil.getUserId());
    }

    @RequestMapping("/queryUserRoleIds")
    @ApiExplain("查询用户角色列表")
    public Result<List<Integer>> queryUserRoleIds(@RequestParam("userId") @NotNull Long userId) {
        return R.ok(adminUserService.queryUserRoleIds(userId));
    }

    @RequestMapping("/queryListName")
    @ApiExplain("查询通讯录")
    public Result queryListName(@RequestBody UserBookBO userBookBO) {
        return R.ok(adminUserService.queryListName(userBookBO));
    }

    @RequestMapping("/attention")
    @ApiExplain("切换关注状态")
    public Result attention(@RequestParam("userId") Long userId) {
        adminUserService.attention(userId);
        return R.ok();
    }

    @RequestMapping("/getNameByUserId")
    @ApiExplain("根据用户ID获取用户名称")
    public Result getNameByUserId(@NotNull Long userId) {
        return R.ok(adminUserService.getNameByUserId(userId));
    }

    @RequestMapping("/queryChildUserId")
    @ApiExplain("根据用户ID下的子用户")
    public Result<List<Long>> queryChildUserId(@NotNull Long userId) {
        List<Long> longList = adminUserService.queryChildUserId(userId);
        return R.ok(longList);
    }

    @RequestMapping("/queryUserInfo")
    @ApiOperation("查询用户信息")
    public Result<AdminUser> queryUserInfo(@RequestParam("userId") Long userId) {
        AdminUser byId = adminUserService.getById(userId);
        String nameByDeptId = ApplicationContextHolder.getBean(IAdminDeptService.class).getNameByDeptId(byId.getDeptId());
        byId.setDeptName(nameByDeptId);
        byId.setSalt(null);
        byId.setPassword(null);
        return R.ok(byId);
    }

    @RequestMapping("/queryInfoByUserId")
    @ApiExplain("根据用户ID获取用户")
    public Result<UserInfo> queryInfoByUserId(@NotNull Long userId) {
        AdminUser byId = adminUserService.getById(userId);
        UserInfo userInfo = null;
        if (byId != null && byId.getDeptId() != null) {
            userInfo = BeanUtil.copyProperties(byId, UserInfo.class);
            String nameByDeptId = ApplicationContextHolder.getBean(IAdminDeptService.class).getNameByDeptId(byId.getDeptId());
            userInfo.setDeptName(nameByDeptId);
        }
        return R.ok(userInfo);
    }

    @PostMapping("/queryUserByIds")
    @ApiExplain("根据用户ID获取用户")
    public Result<List<SimpleUser>> queryUserByIds(@RequestBody List<Long> ids) {
        List<SimpleUser> simpleUsers = adminUserService.queryUserByIds(ids);
        return R.ok(simpleUsers);
    }

    @PostMapping("/queryUserById")
    @ApiExplain("根据用户ID获取用户")
    public Result<SimpleUser> queryUserById(@RequestParam("userId") Long userId) {
        AdminUser adminUser = adminUserService.getById(userId);
        return R.ok(BeanUtil.copyProperties(adminUser, SimpleUser.class));
    }

    @PostMapping("/queryUserByDeptIds")
    @ApiExplain("根据部门ID获取用户ids")
    public Result<List<Long>> queryUserByDeptIds(@RequestBody List<Integer> ids) {
        List<Long> userIds = adminUserService.queryUserByDeptIds(ids);
        return R.ok(userIds);
    }

    @PostMapping("/readNotice")
    @ApiOperation("设置更新日志为已读")
    public Result readNotice() {
        Long userId = UserUtil.getUserId();
        String name = "readNotice";
        Integer count = adminUserConfigService.lambdaQuery().eq(AdminUserConfig::getUserId, userId).eq(AdminUserConfig::getName, name).count();
        if (count > 1) {
            adminUserConfigService.lambdaUpdate().set(AdminUserConfig::getStatus, 1).eq(AdminUserConfig::getUserId, userId).eq(AdminUserConfig::getName, name).update();
        } else {
            AdminUserConfig adminUserConfig = new AdminUserConfig();
            adminUserConfig.setValue("");
            adminUserConfig.setName(name);
            adminUserConfig.setUserId(userId);
            adminUserConfig.setStatus(1);
            adminUserConfig.setDescription("升级日志阅读状态");
            adminUserConfigService.save(adminUserConfig);
        }
        return R.ok();
    }


    @PostMapping("/queryAuthUserList")
    @ApiOperation("查询权限下用户")
    public Result<List<SimpleUser>> queryAuthUserList() {
        List<SimpleUser> userList = new ArrayList<>();
        if (UserUtil.isAdmin()) {
            userList.addAll(adminUserService.list().stream().map(user -> BeanUtil.copyProperties(user, SimpleUser.class)).collect(Collectors.toList()));
        } else {
            List<Long> childUserId = adminUserService.queryChildUserId(UserUtil.getUserId());
            userList.addAll(adminUserService.queryUserByIds(childUserId));
        }
        return R.ok(userList);
    }

    @PostMapping("/queryDeptUserList/{deptId}")
    @ApiOperation("查询部门用户列表(表单使用)")
    public Result<DeptUserListVO> queryDeptUserList(@PathVariable Integer deptId) {
        DeptUserListVO deptUserListVO = adminUserService.queryDeptUserList(deptId);
        return Result.ok(deptUserListVO);
    }

    @PostMapping("/queryDeptUserListByHrm")
    @ApiOperation("查询部门用户列表(hrm添加员工使用)")
    public Result<Set<HrmSimpleUserVO>> queryDeptUserListByHrm(@RequestBody DeptUserListByHrmBO deptUserListByHrmBO) {
        Set<HrmSimpleUserVO> userList = adminUserService.queryDeptUserListByHrm(deptUserListByHrmBO);
        return Result.ok(userList);
    }

    @PostMapping("/queryUserIdByRealName")
    @ApiOperation("查询用户id根据真实姓名")
    public Result<List<Long>> queryUserIdByRealName(@RequestParam("realNames") List<String> realNames) {
        List<Long> userIdList = adminUserService.queryUserIdByRealName(realNames);
        return Result.ok(userIdList);
    }

    @PostMapping("/queryLoginUserInfo")
    @ApiExplain("模拟查询登陆用户信息")
    public Result<UserInfo> queryLoginUserInfo(@RequestParam("userId") Long userId) {
        UserInfo userInfo = adminUserService.queryLoginUserInfo(userId);
        return Result.ok(userInfo);
    }

    @PostMapping("/querySystemStatus")
    @ApiOperation("查询当前系统初始状态")
    @ParamAspect
    public Result<Integer> querySystemStatus() {
        Integer status = adminUserService.querySystemStatus();
        return R.ok(status);
    }

    @PostMapping("/initUser")
    @ApiOperation("初始化系统用户")
    @ParamAspect
    public Result initUser(@Validated @RequestBody SystemUserBO systemUserBO) {
        adminUserService.initUser(systemUserBO);
        return R.ok();
    }

    @RequestMapping("/findTenantList")
    @ApiOperation(value = "通过name查询用户", httpMethod = "POST")
    public Result<List<Map<String, Object>>> findTenantList(String username) {
        List<Map<String, Object>> userInfoList = adminUserService.findTenantList(username);
        return Result.ok(userInfoList);
    }

    /**
     * 只修改密码，不做其他操作
     *
     * @param oldPass
     * @param newPass
     * @return
     */
    @PostMapping("/newUpdatePassword")
    @ApiOperation("修改登录密码")
    public Result newUpdatePassword(@RequestParam("oldPwd") String oldPass, @RequestParam("newPwd") String newPass) {
        AdminUser adminUser = adminUserService.getById(UserUtil.getUserId());
        if (!UserUtil.verify(adminUser.getUsername() + oldPass, adminUser.getSalt(), adminUser.getPassword())) {
            return R.error(AdminCodeEnum.ADMIN_PASSWORD_ERROR);
        }
        adminUser.setPassword(newPass);
        adminUserService.newUpdateUser(adminUser);
        return R.ok();
    }

    @PostMapping("/addUserMap")
    @ApiOperation("同步账号:新增用户")
    @OperateLog(detail = "同步账号:新增用户", types = LogTypes.SYS, behavior = LogBehavior.INSERT)
    public Integer addUserMap(@RequestBody Map map) {
        AdminUserVO adminUserVO = new AdminUserVO();
        BeanUtil.copyProperties(map, adminUserVO);
        Long userId = adminUserService.addUser(adminUserVO);
        return Integer.valueOf(userId.toString());
    }

}


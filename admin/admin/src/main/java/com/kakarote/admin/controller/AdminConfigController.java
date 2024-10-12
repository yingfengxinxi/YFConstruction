package com.kakarote.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.admin.common.AdminCodeEnum;
import com.kakarote.admin.common.AdminConst;
import com.kakarote.admin.common.AdminModuleEnum;
import com.kakarote.admin.entity.BO.AdminCompanyBO;
import com.kakarote.admin.entity.BO.LogWelcomeSpeechBO;
import com.kakarote.admin.entity.BO.ModuleSettingBO;
import com.kakarote.admin.entity.PO.AdminConfig;
import com.kakarote.admin.entity.PO.AdminModelSort;
import com.kakarote.admin.entity.PO.AdminUserConfig;
import com.kakarote.admin.entity.VO.ModuleSettingVO;
import com.kakarote.admin.service.IAdminConfigService;
import com.kakarote.admin.service.IAdminFileService;
import com.kakarote.admin.service.IAdminModelSortService;
import com.kakarote.admin.service.IAdminUserConfigService;
import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.servlet.upload.UploadEntity;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 客户规则 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@RestController
@RequestMapping("/adminConfig")
@Api(tags = "系统配置接口")
@Slf4j
public class AdminConfigController {

    @Autowired
    private IAdminConfigService adminConfigService;

    @Autowired
    private IAdminFileService adminFileService;

    @Autowired
    private IAdminUserConfigService adminUserConfigService;

    @Autowired
    private IAdminModelSortService adminModelSortService;


    /**
     * 设置系统配置
     */
    @ApiOperation(value = "设置企业配置")
    @PostMapping("/setAdminConfig")
    public Result setAdminConfig(@RequestParam(value = "file",required = false)
                                 @ApiParam("文件") MultipartFile file,
                                 @ApiParam("name") String name) {
        AdminCompanyBO adminCompanyBO = new AdminCompanyBO();
        adminCompanyBO.setCompanyName(name);
        try {
            if (file != null && file.getSize() > 1) {
                UploadEntity img = adminFileService.upload(file, null, "img","0");
                adminCompanyBO.setCompanyLogo(img.getUrl());
            }
        } catch (IOException ignored) {
        }
        adminConfigService.setAdminConfig(adminCompanyBO);
        return Result.ok();
    }

    /**
     * 查询企业配置
     *
     * @return Result
     * @author zhangzhiwei
     */
    @ApiOperation(value = "查询企业配置")
    @PostMapping("/queryAdminConfig")
    public Result<AdminCompanyBO> queryAdminConfig() {
        return R.ok(adminConfigService.queryAdminConfig());
    }

    @ApiOperation(value = "头部设置")
    @PostMapping("/queryHeaderModelSort")
    public Result<List<String>> queryHeaderModelSort() {
        List<AdminModelSort> list = adminModelSortService.lambdaQuery().select(AdminModelSort::getModel)
                .eq(AdminModelSort::getType, 1)
                .eq(AdminModelSort::getUserId, UserUtil.getUserId())
                .list();
        return Result.ok(list.stream().map(AdminModelSort::getModel).collect(Collectors.toList()));
    }

    @ApiOperation(value = "头部设置")
    @PostMapping("/setHeaderModelSort")
    public Result setHeaderModelSort(@RequestBody List<String> list) {
        List<AdminModelSort> modelSortList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            AdminModelSort adminModelSort = new AdminModelSort();
            adminModelSort.setType(1).setModel(list.get(i)).setSort(i).setIsHidden(0).setUserId(UserUtil.getUserId());
            modelSortList.add(adminModelSort);
        }
        LambdaQueryWrapper<AdminModelSort> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminModelSort::getType, 1).eq(AdminModelSort::getUserId, UserUtil.getUserId());
        adminModelSortService.remove(wrapper);
        adminModelSortService.saveBatch(modelSortList, Const.BATCH_SAVE_SIZE);
        return R.ok();
    }

    @ApiOperation(value = "设置活动咨询状态")
    @PostMapping("/setMarketing")
    public Result setMarketing(@RequestParam("status") Integer status) {
       adminConfigService.setMarketing(status);
       return R.ok();
    }


    @ApiOperation(value = "查询活动咨询状态")
    @PostMapping("/queryMarketing")
    public Result queryMarketing() {
        return R.ok(adminConfigService.queryMarketing());
    }

    /**
     * 查询企业模块配置
     *
     * @author zhangzhiwei
     */
    @ApiOperation(value = "查询企业模块配置")
    @PostMapping("/queryModuleSetting")
    public Result<List<ModuleSettingVO>> queryModuleSetting() {
        return R.ok(adminConfigService.queryModuleSetting());
    }

    /**
     * 设置企业模块
     *
     * @param moduleSetting data
     */
    @ApiOperation(value = "设置企业模块")
    @PostMapping("/setModuleSetting")
    public Result setModuleSetting(@Valid @RequestBody ModuleSettingBO moduleSetting) {
        AdminConfig adminConfig = adminConfigService.getById(moduleSetting.getSettingId());
        if (AdminModuleEnum.CRM.getValue().equals(adminConfig.getName())) {
            return R.error(AdminCodeEnum.ADMIN_MODULE_CLOSE_ERROR);
        }
        adminConfig.setStatus(moduleSetting.getStatus());
        adminConfigService.setModuleSetting(adminConfig);
        return Result.ok();
    }

    @ApiOperation(value = "设置日志欢迎语")
    @PostMapping("/setLogWelcomeSpeech")
    public Result setLogWelcomeSpeech(@Valid @RequestBody List<String> stringList) {
        adminConfigService.setLogWelcomeSpeech(stringList);
        return Result.ok();
    }

    /**
     * 获取日志欢迎语列表
     */
    @ApiOperation(value = "获取日志欢迎语")
    @PostMapping("/getLogWelcomeSpeechList")
    public Result<List<LogWelcomeSpeechBO>> getLogWelcomeSpeechList() {
        List<LogWelcomeSpeechBO> adminConfigs = adminConfigService.getLogWelcomeSpeechList();
        return R.ok(adminConfigs);
    }

    /**
     * 删除配置数据
     */
    @ApiOperation(value = "删除配置数据")
    @PostMapping("/deleteConfigById")
    public Result deleteConfigById(@RequestBody @ApiParam(name = "settingId", value = "主键ID", required = true) Integer settingId) {
        if (settingId == null) {
            return R.error(AdminCodeEnum.ADMIN_DATA_EXIST_ERROR);
        }
        adminConfigService.removeById(settingId);
        return R.ok();
    }

    /**
     * 查询呼叫中心设置
     */
    @ApiOperation(value = "查询手机端模块设置")
    @PostMapping("/queryCallModuleSetting")
    public Result<ModuleSettingVO> queryCallModuleSetting() {
        ModuleSettingVO moduleSettingVO = adminConfigService.queryCallModuleSetting();
        return R.ok(moduleSettingVO);
    }

    @ApiOperation(value = "查询手机端模块配置")
    @PostMapping("/queryMobileModuleSetting")
    public Result<JSONArray> queryMobileModuleSetting() {
        String name = "MobileModuleSetting";
        AdminUserConfig userConfig = adminUserConfigService.queryUserConfigByName(name);
        if (userConfig == null) {
            return Result.ok(new JSONArray());
        }
        return Result.ok(JSON.parseArray(userConfig.getValue()));
    }

    /**
     * 修改手机端模块设置
     */

    @ApiOperation(value = "修改手机端模块配置")
    @PostMapping("/setMobileModuleSetting")
    public Result setMobileModuleSetting(@RequestBody JSONArray json) {
        String name = "MobileModuleSetting";
        AdminUserConfig userConfig = adminUserConfigService.queryUserConfigByName(name);
        if (userConfig != null) {
            userConfig.setValue(json.toJSONString());
            adminUserConfigService.updateById(userConfig);
        } else {
            userConfig = new AdminUserConfig();
            userConfig.setStatus(1);
            userConfig.setName("MobileModuleSetting");
            userConfig.setValue(json.toJSONString());
            userConfig.setUserId(UserUtil.getUserId());
            userConfig.setDescription("手机端模块设置");
            adminUserConfigService.save(userConfig);
        }
        return R.ok();
    }

    @ApiOperation(value = "设置跟进记录常用语")
    @PostMapping("/setActivityPhrase")
    public Result setActivityPhrase(@RequestBody List<String> stringList) {
        String name = "ActivityPhrase";
        Long userId = UserUtil.getUserId();
        String description = "跟进记录常用语";
        adminUserConfigService.deleteUserConfigByName(name);
        List<AdminUserConfig> adminUserConfigList = new ArrayList<>(stringList.size());
        stringList.forEach(str -> {
            AdminUserConfig userConfig = new AdminUserConfig();
            userConfig.setStatus(1);
            userConfig.setName(name);
            userConfig.setValue(str);
            userConfig.setUserId(userId);
            userConfig.setDescription(description);
            adminUserConfigList.add(userConfig);
        });
        adminUserConfigService.saveBatch(adminUserConfigList, AdminConst.BATCH_SAVE_SIZE);
        return R.ok();
    }

    @ApiOperation(value = "设置跟进记录类型")
    @PostMapping("/setRecordOptions")
    public Result setRecordOptions(@RequestBody List<String> stringList) {
        String name = "followRecordOption";
        String description = "跟进记录选项";
        adminConfigService.removeByMap(new JSONObject().fluentPut("name", name));
        List<AdminConfig> adminUserConfigList = new ArrayList<>(stringList.size());
        stringList.forEach(str -> {
            AdminConfig userConfig = new AdminConfig();
            userConfig.setStatus(1);
            userConfig.setName(name);
            userConfig.setValue(str);
            userConfig.setDescription(description);
            adminUserConfigList.add(userConfig);
        });
        adminConfigService.saveBatch(adminUserConfigList, AdminConst.BATCH_SAVE_SIZE);
        return R.ok();
    }

    @ApiOperation(value = "设置报销费用类型")
    @PostMapping("/setExamineExpenseType")
    public Result setExamineExpenseType(@RequestBody List<String> stringList) {
        String name = "examineExpenseType";
        String description = "报销费用类型";
        adminConfigService.removeByMap(new JSONObject().fluentPut("name", name));
        List<AdminConfig> adminUserConfigList = new ArrayList<>(stringList.size());
        stringList.forEach(str -> {
            AdminConfig userConfig = new AdminConfig();
            userConfig.setStatus(1);
            userConfig.setName(name);
            userConfig.setValue(str);
            userConfig.setDescription(description);
            adminUserConfigList.add(userConfig);
        });
        adminConfigService.saveBatch(adminUserConfigList, AdminConst.BATCH_SAVE_SIZE);
        return R.ok();
    }


    @PostMapping("/queryExamineExpenseType")
    @ApiOperation("查询报销费用列表")
    public Result<List<String>> queryRecordOptions() {
        List<String> strings = adminConfigService.queryRecordOptions();
        return Result.ok(strings);
    }


    /**
     * 查询跟进记录常用语
     */
    @ApiOperation(value = "查询跟进记录常用语")
    @PostMapping("/queryActivityPhrase")
    public Result<List<String>> queryActivityPhrase() {
        String name = "ActivityPhrase";
        List<AdminUserConfig> adminConfigList = adminUserConfigService.queryUserConfigListByName(name);
        return Result.ok(adminConfigList.stream().map(AdminUserConfig::getValue).collect(Collectors.toList()));
    }

    @ApiExplain(value = "查询config配置")
    @RequestMapping("/queryConfigByName")
    public Result<List<com.kakarote.core.feign.admin.entity.AdminConfig>> queryConfigByName(@RequestParam("name") String name) {
        List<AdminConfig> adminConfigs = adminConfigService.queryConfigListByName(name);
        return Result.ok(adminConfigs.stream().map(config -> BeanUtil.copyProperties(config, com.kakarote.core.feign.admin.entity.AdminConfig.class)).collect(Collectors.toList()));
    }
    @Autowired
    private Redis redis;
    @ApiExplain(value = "查询config配置")
    @RequestMapping("/queryFirstConfigByName")
    public Result<com.kakarote.core.feign.admin.entity.AdminConfig> queryFirstConfigByName(@RequestParam("name") String name, HttpServletRequest request) {
        String token = request.getHeader("ADMIN-TOKEN");
        log.info("ADMIN-TOKEN:{}",token);
        Object data = redis.get(token);
        log.info("userInfo:{}",JSON.toJSONString(data));
        AdminConfig config = adminConfigService.queryConfigByName(name);
        return Result.ok(BeanUtil.copyProperties(config, com.kakarote.core.feign.admin.entity.AdminConfig.class));
    }

    @ApiExplain(value = "修改config配置")
    @PostMapping("/updateAdminConfig")
    public Result updateAdminConfig(@RequestBody AdminConfig adminConfig) {
        adminConfigService.updateAdminConfig(adminConfig);
        return R.ok();
    }
    @ApiExplain(value = "修改config配置")
    @PostMapping("/updateAdminConfigNoTenantId")
    public Result updateAdminConfigNoTenantId(@RequestBody AdminConfig adminConfig) {
        adminConfigService.updateAdminConfigNoTenantId(adminConfig);
        return R.ok();
    }
    @ApiExplain(value = "查询config配置")
    @RequestMapping("/queryFirstConfigByNameAndValue")
    public Result<com.kakarote.core.feign.admin.entity.AdminConfig> queryFirstConfigByNameAndValue(@RequestParam("name") String name,
                                                                                                   @RequestParam("value") String value) {
        AdminConfig config = adminConfigService.queryFirstConfigByNameAndValue(name, value);
        return Result.ok(BeanUtil.copyProperties(config, com.kakarote.core.feign.admin.entity.AdminConfig.class));
    }

    @ApiExplain(value = "查询config配置")
    @RequestMapping("/queryConfigByNameAndTenantId")
    public Result<List<com.kakarote.core.feign.admin.entity.AdminConfig>> queryConfigByNameAndTenantId(@RequestParam("name") String name) {
        List<AdminConfig> adminConfigs = adminConfigService.queryConfigByNameAndTenantId(name);
        return Result.ok(adminConfigs.stream().map(config -> BeanUtil.copyProperties(config, com.kakarote.core.feign.admin.entity.AdminConfig.class)).collect(Collectors.toList()));
    }
}


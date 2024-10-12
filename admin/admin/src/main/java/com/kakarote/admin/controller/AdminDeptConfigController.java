package com.kakarote.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.admin.entity.BO.AdminDeptConfigBO;
import com.kakarote.admin.entity.PO.AdminDeptConfig;
import com.kakarote.admin.service.IAdminDeptConfigService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author author
 * @since 2020-12-16
 */
@RestController
@RequestMapping("/adminDeptConfig")
public class AdminDeptConfigController {
    @Autowired
    IAdminDeptConfigService adminDeptConfigService;

    @PostMapping("/queryConfigList")
    @ApiOperation("分页查询对接配置列表")
    @OperateLog(detail = "新增部门（租户）--系统管理员用", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<BasePage<AdminDeptConfig>> queryConfigList(@RequestBody AdminDeptConfigBO adminConfigBO) {
        return R.ok(adminDeptConfigService.queryConfigList(adminConfigBO));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation("获取对接配置")
    @OperateLog(detail = "获取对接配置--系统管理员用", types = LogTypes.SYS, behavior = LogBehavior.DELETE)
    public Result<AdminDeptConfig> getInfo(@PathVariable("id") String id) {
        return R.ok(adminDeptConfigService.getById(id));
    }

    @PostMapping("/addOrUpdateConfig")
    @ApiOperation("新增/修改对接配置")
    @OperateLog(detail = "新增/修改对接配置--系统管理员用", types = LogTypes.SYS, behavior = LogBehavior.INSERT)
    public Result addOrUpdateConfig(@RequestBody AdminDeptConfig adminDeptConfig) {
        return adminDeptConfigService.addOrUpdateConfig(adminDeptConfig);
    }

//    @PostMapping("/updateConfig")
//    @ApiOperation("修改对接配置")
//    @OperateLog(detail="修改对接配置--系统管理员用",types= LogTypes.SYS,behavior=LogBehavior.UPDATE)
//    public Result updateConfig(@RequestBody AdminDeptConfig adminDeptConfig) {
//        adminDeptConfigService.updateById(adminDeptConfig);
//        return R.ok();
//    }

    @PostMapping("/delConfig")
    @ApiOperation("删除对接配置")
    @OperateLog(detail = "删除对接配置--系统管理员用", types = LogTypes.SYS, behavior = LogBehavior.DELETE)
    public Result delConfig(@RequestBody Map<String, Object> param) {
        adminDeptConfigService.removeByMap(param);
        return R.ok();
    }


    @GetMapping("/getConfig/{configId}")
    @ApiOperation("获取租户配置")
    public Result<JSONObject> getConfig(@PathVariable String configId) {
        AdminDeptConfig adminDeptConfig = adminDeptConfigService.getById(configId);
        if (adminDeptConfig == null) {
            return Result.error(-1, "参数出现异常");
        }
        String config = adminDeptConfig.getParams();
        return Result.ok(JSONObject.parseObject(config));
    }

    @GetMapping("/queryDeptConfigForMessage/{type}/{messageType}/{tenantId}")
    @ApiOperation("消息：获取配置")
    public Result<JSONObject> queryDeptConfigForMessage(@PathVariable("type") String type, @PathVariable("messageType") String messageType,
                                                        @PathVariable("tenantId") Long tenantId) {
        AdminDeptConfig adminDeptConfig = adminDeptConfigService.lambdaQuery()
                .eq(AdminDeptConfig::getType, type)
                .like(AdminDeptConfig::getMessageType, "%" + messageType + "%")
                .eq(AdminDeptConfig::getTenantId, tenantId).one();
        if (adminDeptConfig == null) {
            return Result.error(-1, "参数出现异常");
        }
        return Result.ok(JSONObject.parseObject(JSON.toJSONString(adminDeptConfig)));
    }
}


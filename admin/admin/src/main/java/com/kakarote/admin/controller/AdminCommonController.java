package com.kakarote.admin.controller;

import com.kakarote.admin.entity.PO.AdminUser;
import com.kakarote.admin.service.IAdminCommonService;
import com.kakarote.admin.service.IAdminUserService;
import com.kakarote.core.common.BusinessTypeEntity;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 通用 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-03-12
 */
@RestController
@RequestMapping("/adminCommon")
@Api(tags = "系统配置接口")
@Slf4j
public class AdminCommonController {

    @Autowired
    private IAdminCommonService commonService;

    @Autowired
    private IAdminUserService adminUserService;

    /**
     * 业务类型
     */
    @ApiOperation(value = "获取业务类型")
    @PostMapping("/getBType")
    public Result<List<BusinessTypeEntity>> setAdminConfig() {
        List<BusinessTypeEntity> list = commonService.getAllBusinessType();
        return R.ok(list);
    }

    /**
     * 自定义字典查询
     */
    @ApiOperation(value = "获取自定义字典")
    @PostMapping("/getDict")
    public Result<List<Map<String,Object>>> setAdminConfig(@RequestBody Map<String,String> params) {
        List<Map<String,Object>> list = commonService.selectCustomList(params);
        return R.ok(list);
    }


    /**
     * 切换项目
     */
    @ApiOperation(value = "切换项目")
    @PostMapping("/changeProject/{projectId}")
    public Result changeProjectr(@PathVariable Integer projectId) {
        UserInfo userInfo = UserUtil.getUser();
        AdminUser adminUser = adminUserService.getById(userInfo.getUserId());
        adminUser.setProjectId(projectId);
        adminUserService.updateById(adminUser);
        return Result.ok();
    }

}

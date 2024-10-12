package com.kakarote.build.safety.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.safety.entity.BO.SafetyEmergencyHandlingBO;
import com.kakarote.build.safety.entity.PO.SafetyEmergencyHandling;
import com.kakarote.build.safety.entity.PO.SafetyEmergencyHandlingDetail;
import com.kakarote.build.safety.service.ISafetyEmergencyHandlingDetailService;
import com.kakarote.build.safety.service.ISafetyEmergencyHandlingService;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.utils.TagUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 应急事件处置 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-04-06
 */
@RestController
@RequestMapping("/buildSafetyEmergencyHandling")
public class SafetyEmergencyHandlingController {

    @Autowired
    private ISafetyEmergencyHandlingService iSafetyEmergencyHandlingService;
    @Autowired
    private ISafetyEmergencyHandlingDetailService iSafetyEmergencyHandlingDetailService;
    @Autowired
    private AdminService adminService;

    @PostMapping("/list")
    @ApiOperation("查询信息列表")
    public Result<BasePage<SafetyEmergencyHandling>> list(@RequestBody SafetyEmergencyHandlingBO safetyEmergencyHandlingBO) {
        BasePage<SafetyEmergencyHandling> list = iSafetyEmergencyHandlingService.selectList(safetyEmergencyHandlingBO);
        return Result.ok(list);
    }

    @PostMapping("/queryById/{valveId}")
    @ApiOperation("根据主键查询信息")
    public Result queryById(@PathVariable @NotNull String valveId){
        SafetyEmergencyHandling byId = iSafetyEmergencyHandlingService.getById(valveId);
        if(byId != null){
            // 查询用户对象
            List<SimpleUser> userList = adminService.queryUserByIds(TagUtil.toLongSet(byId.getToUserIds())).getData();
            byId.setOwnerUserList(userList);
        }

        return R.ok(byId);
    }

    @PostMapping("/save")
    @ApiOperation("新增信息")
    @Transactional
    public Result edit(@Validated @RequestBody SafetyEmergencyHandling safetyEmergencyHandling) {
        return Result.ok(iSafetyEmergencyHandlingService.saveData(safetyEmergencyHandling));
    }

    @PostMapping("/updateById")
    @ApiOperation("修改信息")
    @Transactional
    public Result updateById(@Validated @RequestBody SafetyEmergencyHandling safetyEmergencyHandling) {
        String id = iSafetyEmergencyHandlingService.updateData(safetyEmergencyHandling);
        return Result.ok(id);
    }

    @PostMapping("/delTrueByIds")
    @ApiOperation("删除信息且删除详情信息(根据pid)")
    @Transactional
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            iSafetyEmergencyHandlingService.removeById(id);

            //详情表删除
            QueryWrapper<SafetyEmergencyHandlingDetail> wrapper = new QueryWrapper<>();
            wrapper.eq("pid", id);
            iSafetyEmergencyHandlingDetailService.remove(wrapper);
        }
        return Result.ok();
    }

    @PostMapping("/updateByZHIdMap")
    @ApiOperation("修改信息--APP端")
    @Transactional
    public Result updateByZHIdMap(@RequestBody Map<String, Object> map)
    {
        Result result = iSafetyEmergencyHandlingService.updateByZHIdMap(map);
        return Result.ok();
    }

}


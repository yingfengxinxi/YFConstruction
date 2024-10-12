package com.kakarote.admin.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.admin.entity.PO.AdminInstrumentInfo;
import com.kakarote.admin.entity.PO.AdminMenu;
import com.kakarote.admin.service.IAdminInstrumentInfoService;
import com.kakarote.admin.service.IAdminInstrumentRoleService;
import com.kakarote.core.common.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 仪表盘名称表 前端控制器
 * </p>
 *
 * @author author
 * @since 2020-12-08
 */
@RestController
@RequestMapping("/adminInstrument")
public class AdminInstrumentInfoController {

    @Autowired
    private IAdminInstrumentInfoService adminInstrumentInfoService;

//    @GetMapping("/getNameByModelId/{modelId}")
//    @ApiExplain("根据模块ID获取名称")
//    @OperateLog(detail="根据模块ID获取名称--首页展示用",types= LogTypes.SYS,behavior=LogBehavior.SELECT)
//    public String getNameByModelId(@PathVariable("modelId") Integer modelId) {
//
//        return adminInstrumentInfoService.getNameByModelId(modelId);
//    }

    @PostMapping("/update")
    @ApiOperation("更新仪表盘模块名称")
    @OperateLog(detail="更新仪表盘模块名称--租户管理员用",types= LogTypes.SYS,behavior=LogBehavior.UPDATE)
    public Result<Integer> updateMenu(@RequestBody AdminInstrumentInfo adminInstrumentInfo) {
        Integer i = adminInstrumentInfoService.update(adminInstrumentInfo);
        return Result.ok(i);
    }
}


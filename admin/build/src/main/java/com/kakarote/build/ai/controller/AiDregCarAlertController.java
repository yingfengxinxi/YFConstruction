package com.kakarote.build.ai.controller;


import com.kakarote.build.ai.entity.BO.AiDregCarAlertBO;
import com.kakarote.build.ai.entity.PO.AiDregCarAlert;
import com.kakarote.build.ai.service.IAiDregCarAlertService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 渣土车辆智能分析 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-06-04
 */
@RestController
@RequestMapping("/aiDregCarAlert")
public class AiDregCarAlertController {

    @Autowired
    private IAiDregCarAlertService dregCarAlertService;

    @PostMapping("/selectByPage")
    @ApiOperation("查询渣土车辆智能分析列表")
    @OperateLog(detail = "查询渣土车辆智能分析列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<AiDregCarAlert>> selectByPage(@RequestBody AiDregCarAlertBO aiDregCarAlertBO){
        return Result.ok(dregCarAlertService.selectByPage(aiDregCarAlertBO));
    }



    @PostMapping("/selectDregAlertList")
    @ApiOperation("查询渣土车辆智能分析列表")
    @OperateLog(detail = "查询渣土车辆智能分析列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String, Object>>> selectDregAlertList(@RequestBody AiDregCarAlertBO aiDregCarAlertBO){
        return Result.ok(dregCarAlertService.selectDregAlertList(aiDregCarAlertBO));
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("根据id查询渣土车辆智能分析数据")
    @OperateLog(detail = "根据id查询渣土车辆智能分析数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<AiDregCarAlert> getById(@PathVariable Integer id){
        return Result.ok(dregCarAlertService.getById(id));
    }

    @PostMapping("/update")
    @ApiOperation("更新渣土车辆智能分析数据")
    @OperateLog(detail = "更新渣土车辆智能分析数据", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result update(@RequestBody AiDregCarAlert aiDregCarAlert){
        UserInfo user = UserUtil.getUser();
        aiDregCarAlert.setHandleBy(user.getRealname());
        aiDregCarAlert.setHandleTime(new Date());
        dregCarAlertService.updateById(aiDregCarAlert);
        return Result.ok();
    }

    @PostMapping("/selectByPagePro")
    @ApiOperation("监管平台-查询渣土车辆智能分析列表")
    @OperateLog(detail = "监管平台-查询渣土车辆智能分析列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<AiDregCarAlert>> selectByPagePro(@RequestBody AiDregCarAlertBO aiDregCarAlertBO){
        return Result.ok(dregCarAlertService.selectByPagePro(aiDregCarAlertBO));
    }
}


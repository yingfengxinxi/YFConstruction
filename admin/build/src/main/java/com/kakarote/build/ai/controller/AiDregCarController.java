package com.kakarote.build.ai.controller;


import com.kakarote.build.ai.entity.BO.AiDregCarBO;
import com.kakarote.build.ai.entity.PO.AiDregCar;
import com.kakarote.build.ai.service.IAiDregCarService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 渣土车记录 前端控制器
 * </p>
 *
 * @author shao
 * @since 2021-05-24
 */
@RestController
@RequestMapping("/aiDregCar")
public class AiDregCarController {

    @Autowired
    private IAiDregCarService dregCarService;

    @PostMapping("/selectByPage")
    @ApiOperation("查询渣土车记录列表")
    @OperateLog(detail = "查询渣土车记录列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<AiDregCar>> selectByPage(@RequestBody AiDregCarBO aiDregCarBO){
        return Result.ok(dregCarService.selectByPage(aiDregCarBO));
    }

    @PostMapping("/saveData")
    @ApiOperation("车记录实时数据上传接口")
    @OperateLog(detail = "车记录实时数据上传接口", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result saveData(@RequestBody List list) {
        return dregCarService.saveData(list);
    }
}


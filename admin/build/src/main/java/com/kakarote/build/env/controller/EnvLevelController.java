package com.kakarote.build.env.controller;


import com.kakarote.build.env.entity.BO.EnvLevelBO;
import com.kakarote.build.env.entity.PO.EnvLevel;
import com.kakarote.build.env.service.IEnvLevelService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 环境数据标准设置表 前端控制器
 * </p>
 *
 * @author pz
 * @since 2021-09-17
 */
@RestController
@RequestMapping("/envLevel")
public class EnvLevelController {
    @Autowired
    private IEnvLevelService envLevelService;

    @PostMapping("/list")
    @ApiOperation("查询环境标准列表")
    @OperateLog(detail = "查询环境标准列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<EnvLevel>> list(@RequestBody EnvLevelBO envLevelBO) {
        return Result.ok(envLevelService.selectByPage(envLevelBO));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存环境标准信息")
    @OperateLog(detail = "保存环境标准信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result saveOrUpdate(@RequestBody EnvLevel envLevel) {
        return Result.ok(envLevelService.saveOrUpdate(envLevel));
    }
    /**
     * 删除环境标准信息
     */
    @PostMapping("/delTrueByIds")
    @ApiOperation("删除环境标准信息")
    @OperateLog(detail = "删除环境标准信息", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String checkId : ids) {
            // 删除基本信息
            envLevelService.removeById(checkId);
        }
        return Result.ok();
    }

}


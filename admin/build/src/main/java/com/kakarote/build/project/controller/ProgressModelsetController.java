package com.kakarote.build.project.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.project.entity.PO.ProgressModelset;
import com.kakarote.build.project.service.IProgressModelsetDetailsService;
import com.kakarote.build.project.service.IProgressModelsetService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 进度管理--模板设置--主表 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-04-28
 */
@RestController
@RequestMapping("/progressModelset")
public class ProgressModelsetController {
    @Autowired
    private IProgressModelsetService iProgressModelsetService;
    @Autowired
    private IProgressModelsetDetailsService iProgressModelsetDetailsService;

    @PostMapping("/save")
    @ApiOperation("新增信息")
    @Transactional
    @OperateLog(detail = "新增信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result edit(@Validated @RequestBody ProgressModelset progressModelset) {
        iProgressModelsetService.save(progressModelset);
        return Result.ok(progressModelset.getId());
    }

    @PostMapping("/updateById")
    @ApiOperation("修改信息")
    @Transactional
    @OperateLog(detail = "修改信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result updateById(@Validated @RequestBody ProgressModelset progressModelset) {
        boolean b = iProgressModelsetService.updateById(progressModelset);
        return Result.ok(progressModelset.getId());
    }

    @PostMapping("/selectModels")
    @ApiOperation("查询模板")
    @OperateLog(detail = "查询全部模板",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result selectModels(){
        List<ProgressModelset> list = iProgressModelsetService.query().select("id", "name")
                .ne("tenant_id",0).ne("project_id",0) //不做租户及项目数据隔离
                .orderByDesc("create_time").list();
        return Result.ok(list);
    }

    @PostMapping("/delById/{id}")
    @ApiOperation("根据主键删除信息")
    @Transactional
    @OperateLog(detail = "根据主键删除信息",types = LogTypes.BUILD,behavior = LogBehavior.DELETE)
    public Result delById(@PathVariable Integer id){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.ne("project_id",0);
        queryWrapper.eq("mid",id);
        iProgressModelsetDetailsService.remove(queryWrapper);

        iProgressModelsetService.removeById(id);

        return Result.ok();
    }



}


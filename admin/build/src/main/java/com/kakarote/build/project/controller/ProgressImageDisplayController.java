package com.kakarote.build.project.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.equipment.entity.BO.EquipmentPlanBO;
import com.kakarote.build.equipment.entity.PO.EquipmentPlan;
import com.kakarote.build.project.entity.BO.ProgressImageDisplayBO;
import com.kakarote.build.project.entity.PO.ProgressImageDisplay;
import com.kakarote.build.project.service.IProgressImageDisplayService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 形象进度展示 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-07-05
 */
@RestController
@RequestMapping("/progressImageDisplay")
public class ProgressImageDisplayController {

    @Autowired
    private IProgressImageDisplayService imageDisplayService;

    @PostMapping("/list")
    @ApiOperation("查询形象进度数据列表")
    @OperateLog(detail = "查询形象进度数据列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<ProgressImageDisplay>> list(@RequestBody ProgressImageDisplayBO progressImageDisplayBO) {
        return Result.ok(imageDisplayService.selectByPage(progressImageDisplayBO));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新形象进度展示")
    @OperateLog(detail = "保存或更新形象进度展示", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result saveOrUpdate(@RequestBody ProgressImageDisplay progressImageDisplay) {
        imageDisplayService.saveOrUpdate(progressImageDisplay);
        return Result.ok();
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("获取形象进度详情")
    @OperateLog(detail = "获取形象进度详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Object> getById(@PathVariable Integer id) {
        return Result.ok(imageDisplayService.getById(id));
    }

    @PostMapping("/delById/{id}")
    @ApiOperation("根据主键删除形象进度")
    @OperateLog(detail = "根据主键删除形象进度",types = LogTypes.BUILD,behavior = LogBehavior.DELETE)
    public Result delById(@PathVariable Integer id){

        imageDisplayService.removeById(id);

        return Result.ok();
    }
}


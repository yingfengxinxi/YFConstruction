package com.kakarote.work.controller;


import com.kakarote.core.common.*;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.work.entity.PO.WorkTask;
import com.kakarote.work.entity.PO.WorkTaskMilestone;
import com.kakarote.work.service.IWorkTaskMilestoneService;
import com.kakarote.work.service.IWorkTaskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * <p>
 * 任务里程碑表 前端控制器
 * </p>
 *
 * @author author
 * @since 2020-12-16
 */
@RestController
@RequestMapping("/workTaskMilestone")
public class WorkTaskMilestoneController {

    @Autowired
    private IWorkTaskMilestoneService workTaskMilestoneService;

    @PostMapping("/setTaskMilestone")
    @ApiOperation("设置任务里程碑")
    @OperateLog(detail="设置任务里程碑",types= LogTypes.WORK,behavior= LogBehavior.INSERT)
    public Result<WorkTaskMilestone> setTaskMilestone(@RequestBody WorkTaskMilestone workTaskMilestone){

        return workTaskMilestoneService.setTaskMilestone(workTaskMilestone);
    }

}


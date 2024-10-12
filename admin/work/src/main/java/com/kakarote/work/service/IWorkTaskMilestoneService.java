package com.kakarote.work.service;

import com.kakarote.core.common.Result;
import com.kakarote.work.entity.PO.WorkTaskMilestone;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 任务里程碑表 服务类
 * </p>
 *
 * @author author
 * @since 2020-12-16
 */
public interface IWorkTaskMilestoneService extends BaseService<WorkTaskMilestone> {

    Result<WorkTaskMilestone> setTaskMilestone(WorkTaskMilestone workTaskMilestone);
}

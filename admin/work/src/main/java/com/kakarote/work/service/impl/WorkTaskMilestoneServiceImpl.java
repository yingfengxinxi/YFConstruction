package com.kakarote.work.service.impl;

import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.work.entity.PO.WorkTaskMilestone;
import com.kakarote.work.mapper.WorkTaskMilestoneMapper;
import com.kakarote.work.service.IWorkTaskMilestoneService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 任务里程碑表 服务实现类
 * </p>
 *
 * @author author
 * @since 2020-12-16
 */
@Service
public class WorkTaskMilestoneServiceImpl extends BaseServiceImpl<WorkTaskMilestoneMapper, WorkTaskMilestone> implements IWorkTaskMilestoneService {

    @Override
    public Result<WorkTaskMilestone> setTaskMilestone(WorkTaskMilestone workTaskMilestone) {
        List<WorkTaskMilestone> list = lambdaQuery().eq(WorkTaskMilestone::getTaskId,workTaskMilestone.getTaskId()).eq(WorkTaskMilestone::getUserId,UserUtil.getUserId()).list();
        removeByIds(list.stream().map(WorkTaskMilestone::getMilestoneId).collect(Collectors.toList()));

        if(workTaskMilestone.getStatus() == 1){
            workTaskMilestone.setUserId(UserUtil.getUserId());
            workTaskMilestone.setCreateTime(new Date());
            save(workTaskMilestone);
        }
        return R.ok();
    }
}

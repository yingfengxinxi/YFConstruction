package com.kakarote.build.project.service.impl;

import com.kakarote.build.project.entity.BO.ProjectUserBO;
import com.kakarote.build.project.entity.PO.ProjectUser;
import com.kakarote.build.project.mapper.ProjectUserMapper;
import com.kakarote.build.project.service.IProjectUserService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-04-12
 */
@Service
public class ProjectUserServiceImpl extends BaseServiceImpl<ProjectUserMapper, ProjectUser> implements IProjectUserService {

    @Override
    public BasePage<Map> queryPageList(ProjectUserBO projectUserBO) {
        return this.getBaseMapper().queryPageListGrp(projectUserBO.parse(),projectUserBO);
    }

    @Override
    public List<ProjectUser> userProList(Long userId) {
        return this.getBaseMapper().userProListGrp(userId);
    }

    @Transactional
    @Override
    public Result removeUserProject(String id) {
        //
        ProjectUser projectUser = getBaseMapper().getByIdGrp(id);
        if(projectUser == null){
            return Result.error(-1,"数据不存在");
        }
        getBaseMapper().removeUserProjectGrp(projectUser.getUserId(),projectUser.getProjectId());
        getBaseMapper().removeByIdGrp(id);
        return Result.ok();
    }

    @Override
    public BasePage<Map> queryProPageList(ProjectUserBO projectUserBO) {
        return this.getBaseMapper().queryProPageListGrp(projectUserBO.parse(),projectUserBO);
    }
}

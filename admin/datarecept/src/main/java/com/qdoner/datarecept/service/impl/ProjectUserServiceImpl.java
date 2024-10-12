package com.qdoner.datarecept.service.impl;

import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.qdoner.datarecept.entity.BO.ProjectUserBO;
import com.qdoner.datarecept.entity.PO.ProjectUser;
import com.qdoner.datarecept.mapper.ProjectUserMapper;
import com.qdoner.datarecept.service.IProjectUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

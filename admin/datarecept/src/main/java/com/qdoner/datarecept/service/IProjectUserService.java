package com.qdoner.datarecept.service;

import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import com.qdoner.datarecept.entity.BO.ProjectUserBO;
import com.qdoner.datarecept.entity.PO.ProjectUser;

import java.util.List;
import java.util.Map;

public interface IProjectUserService extends BaseService<ProjectUser> {

    BasePage<Map> queryPageList(ProjectUserBO projectInfo);

    /**
     * 当前用户项目列表
     * @param userId
     * @return
     */
    List<ProjectUser> userProList(Long userId);

    Result removeUserProject(String id);

    BasePage<Map> queryProPageList(ProjectUserBO projectUserBO);
}

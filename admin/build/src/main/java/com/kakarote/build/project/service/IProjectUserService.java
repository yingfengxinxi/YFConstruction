package com.kakarote.build.project.service;

import com.kakarote.build.project.entity.BO.ProjectUserBO;
import com.kakarote.build.project.entity.PO.ProjectUser;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wudw
 * @since 2021-04-12
 */
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

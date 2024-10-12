package com.kakarote.build.project.service;

import com.kakarote.build.project.entity.BO.ProjectPublicInfoBO;
import com.kakarote.build.project.entity.PO.ProjectPublicInfo;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 公示信息 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-06-15
 */
public interface IProjectPublicInfoService extends BaseService<ProjectPublicInfo> {

    BasePage<ProjectPublicInfo> selectByPage(ProjectPublicInfoBO publicInfoBO);
}

package com.kakarote.build.project.service.impl;

import com.kakarote.build.project.entity.BO.ProjectPublicInfoBO;
import com.kakarote.build.project.entity.PO.ProjectPublicInfo;
import com.kakarote.build.project.mapper.ProjectPublicInfoMapper;
import com.kakarote.build.project.service.IProjectPublicInfoService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 公示信息 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-06-15
 */
@Service
public class ProjectPublicInfoServiceImpl extends BaseServiceImpl<ProjectPublicInfoMapper, ProjectPublicInfo> implements IProjectPublicInfoService {

    @Override
    public BasePage<ProjectPublicInfo> selectByPage(ProjectPublicInfoBO publicInfoBO) {
        BasePage<ProjectPublicInfo> list = this.getBaseMapper().selectByPage(publicInfoBO.parse(), publicInfoBO);
        return list;
    }
}

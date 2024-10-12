package com.kakarote.build.project.mapper;

import com.kakarote.build.project.entity.BO.ProjectPublicInfoBO;
import com.kakarote.build.project.entity.PO.ProjectPublicInfo;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 公示信息 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-06-15
 */
public interface ProjectPublicInfoMapper extends BaseMapper<ProjectPublicInfo> {

    BasePage<ProjectPublicInfo> selectByPage(BasePage<Object> parse, @Param("data") ProjectPublicInfoBO publicInfoBO);
}

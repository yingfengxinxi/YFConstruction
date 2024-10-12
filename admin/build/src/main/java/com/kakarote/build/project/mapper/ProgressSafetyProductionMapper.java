package com.kakarote.build.project.mapper;

import com.kakarote.build.project.entity.BO.ProgressSafetyProductionBO;
import com.kakarote.build.project.entity.PO.ProgressSafetyProduction;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2022-06-14
 */
public interface ProgressSafetyProductionMapper extends BaseMapper<ProgressSafetyProduction> {

    BasePage<ProgressSafetyProductionBO> selectDataList(BasePage<AdminMessage> parse, @Param("data") ProgressSafetyProductionBO bProgressSecurityBO);
}

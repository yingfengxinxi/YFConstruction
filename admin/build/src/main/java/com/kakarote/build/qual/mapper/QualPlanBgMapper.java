package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.QualPlanBgBO;
import com.kakarote.build.qual.entity.PO.QualPlanBg;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 质量方案变更 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2021-03-23
 */
public interface QualPlanBgMapper extends BaseMapper<QualPlanBg> {

    BasePage<QualPlanBg> selectByPage(BasePage<QualPlanBg> parse, @Param("data") QualPlanBgBO planBgBO);
}

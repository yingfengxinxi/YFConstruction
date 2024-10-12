package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.QualPlanBO;
import com.kakarote.build.qual.entity.PO.QualPlan;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 质量方案 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2021-03-22
 */
public interface QualPlanMapper extends BaseMapper<QualPlan> {

    BasePage<QualPlan> selectByPage(BasePage<QualPlan> parse, @Param("data") QualPlanBO planBO);
}

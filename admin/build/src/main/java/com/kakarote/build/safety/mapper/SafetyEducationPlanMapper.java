package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.SafetyEducationPlanBO;
import com.kakarote.build.safety.entity.PO.SafetyBlockAlarm;
import com.kakarote.build.safety.entity.PO.SafetyEducationPlan;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 安全教育计划 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-08-09
 */
public interface SafetyEducationPlanMapper extends BaseMapper<SafetyEducationPlan> {

    BasePage<SafetyEducationPlan> pageList(BasePage<SafetyEducationPlan> page, @Param("data") SafetyEducationPlanBO sepbo);
}

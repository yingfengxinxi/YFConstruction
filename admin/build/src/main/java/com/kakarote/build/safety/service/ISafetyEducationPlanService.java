package com.kakarote.build.safety.service;

import com.kakarote.build.safety.entity.BO.SafetyEducationPlanBO;
import com.kakarote.build.safety.entity.PO.SafetyEducationPlan;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 安全教育计划 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-08-09
 */
public interface ISafetyEducationPlanService extends BaseService<SafetyEducationPlan> {

    BasePage<SafetyEducationPlan> selectByPage(SafetyEducationPlanBO sepbo);
}

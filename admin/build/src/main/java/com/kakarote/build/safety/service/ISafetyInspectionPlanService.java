package com.kakarote.build.safety.service;

import com.kakarote.build.safety.entity.BO.SafetyInspectionPlanBO;
import com.kakarote.build.safety.entity.PO.SafetyInspectionPlan;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzy
 * @since 2021-04-07
 */
public interface ISafetyInspectionPlanService extends BaseService<SafetyInspectionPlan> {

    BasePage<SafetyInspectionPlan> selectByPage(SafetyInspectionPlanBO inspectionPlanBO);

    List<SafetyInspectionPlan> selectOptions();
}

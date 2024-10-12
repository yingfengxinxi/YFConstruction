package com.kakarote.build.safety.service.impl;

import com.kakarote.build.safety.entity.BO.SafetyInspectionPlanBO;
import com.kakarote.build.safety.entity.PO.SafetyInspectionPlan;
import com.kakarote.build.safety.mapper.SafetyInspectionPlanMapper;
import com.kakarote.build.safety.service.ISafetyInspectionPlanService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-04-07
 */
@Service
public class SafetyInspectionPlanServiceImpl extends BaseServiceImpl<SafetyInspectionPlanMapper, SafetyInspectionPlan> implements ISafetyInspectionPlanService {

    @Override
    public BasePage<SafetyInspectionPlan> selectByPage(SafetyInspectionPlanBO inspectionPlanBO) {
        BasePage<SafetyInspectionPlan> list = this.getBaseMapper().selectByPage(inspectionPlanBO.parse(), inspectionPlanBO);
        return list;
    }

    @Override
    public List<SafetyInspectionPlan> selectOptions() {
        return this.getBaseMapper().selectOptions();
    }
}

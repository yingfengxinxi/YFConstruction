package com.kakarote.build.safety.service.impl;

import com.kakarote.build.safety.entity.BO.SafetyEducationPlanBO;
import com.kakarote.build.safety.entity.PO.SafetyEducationPlan;
import com.kakarote.build.safety.mapper.SafetyEducationPlanMapper;
import com.kakarote.build.safety.service.ISafetyEducationPlanService;
import com.kakarote.core.config.MybatisPlusConfig;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 安全教育计划 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-08-09
 */
@Service
public class SafetyEducationPlanServiceImpl extends BaseServiceImpl<SafetyEducationPlanMapper, SafetyEducationPlan> implements ISafetyEducationPlanService {

    @Override
    public BasePage<SafetyEducationPlan> selectByPage(SafetyEducationPlanBO sepbo) {
        sepbo.setOrdersBd("create_time",false);
        return getBaseMapper().pageList(sepbo.parse(),sepbo);
    }

    public static void main(String[] args) {
        System.out.println(MybatisPlusConfig.getNextId());
    }
}

package com.kakarote.build.qual.service.impl;

import com.kakarote.build.qual.entity.BO.QualPlanBO;
import com.kakarote.build.qual.entity.PO.QualPlan;
import com.kakarote.build.qual.mapper.QualPlanMapper;
import com.kakarote.build.qual.service.IQualPlanService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 质量方案 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-03-22
 */
@Service
public class QualPlanServiceImpl extends BaseServiceImpl<QualPlanMapper, QualPlan> implements IQualPlanService {

    @Override
    public BasePage<QualPlan> selectByPage(QualPlanBO planBO){
        planBO.setOrdersBd("a.create_time",false);
        return this.getBaseMapper().selectByPage(planBO.parse(),planBO);
    }
}

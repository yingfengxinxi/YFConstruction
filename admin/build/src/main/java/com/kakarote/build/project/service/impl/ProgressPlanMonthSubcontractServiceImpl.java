package com.kakarote.build.project.service.impl;

import com.kakarote.build.project.entity.BO.ProgressPlanMonthBO;
import com.kakarote.build.project.entity.PO.ProgressPlanMonthSubcontract;
import com.kakarote.build.project.mapper.ProgressPlanMonthSubcontractMapper;
import com.kakarote.build.project.service.IProgressPlanMonthSubcontractService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 计划管理--月计划--分包明细 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-06-29
 */
@Service
public class ProgressPlanMonthSubcontractServiceImpl extends BaseServiceImpl<ProgressPlanMonthSubcontractMapper, ProgressPlanMonthSubcontract> implements IProgressPlanMonthSubcontractService {

    @Override
    public BasePage<Map<String, Object>> selectList(ProgressPlanMonthBO progressPlanMonthBO) {
        BasePage<Map<String, Object>> mapBasePage = getBaseMapper().selectList(progressPlanMonthBO.parse(), progressPlanMonthBO);
        return mapBasePage;
    }
}

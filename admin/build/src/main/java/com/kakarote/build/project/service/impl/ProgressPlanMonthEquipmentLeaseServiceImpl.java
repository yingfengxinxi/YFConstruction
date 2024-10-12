package com.kakarote.build.project.service.impl;

import com.kakarote.build.project.entity.BO.ProgressPlanMonthBO;
import com.kakarote.build.project.entity.PO.ProgressPlanMonthEquipmentLease;
import com.kakarote.build.project.mapper.ProgressPlanMonthEquipmentLeaseMapper;
import com.kakarote.build.project.service.IProgressPlanMonthEquipmentLeaseService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 计划管理--月计划--设备租赁明细 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-06-29
 */
@Service
public class ProgressPlanMonthEquipmentLeaseServiceImpl extends BaseServiceImpl<ProgressPlanMonthEquipmentLeaseMapper, ProgressPlanMonthEquipmentLease> implements IProgressPlanMonthEquipmentLeaseService {

    @Override
    public BasePage<Map<String, Object>> selectList(ProgressPlanMonthBO progressPlanMonthBO) {
        BasePage<Map<String, Object>> mapBasePage = getBaseMapper().selectList(progressPlanMonthBO.parse(), progressPlanMonthBO);
        return mapBasePage;
    }
}

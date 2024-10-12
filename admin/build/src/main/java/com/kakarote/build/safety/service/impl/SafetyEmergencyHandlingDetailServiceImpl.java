package com.kakarote.build.safety.service.impl;

import com.kakarote.build.safety.entity.BO.SafetyEmergencyHandlingDetailBO;
import com.kakarote.build.safety.entity.PO.SafetyEmergencyHandlingDetail;
import com.kakarote.build.safety.mapper.SafetyEmergencyHandlingDetailMapper;
import com.kakarote.build.safety.service.ISafetyEmergencyHandlingDetailService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应急事件处置明细 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-04-06
 */
@Service
public class SafetyEmergencyHandlingDetailServiceImpl extends BaseServiceImpl<SafetyEmergencyHandlingDetailMapper, SafetyEmergencyHandlingDetail> implements ISafetyEmergencyHandlingDetailService {

    @Override
    public BasePage<SafetyEmergencyHandlingDetail> selectList(SafetyEmergencyHandlingDetailBO safetyEmergencyHandlingDetailBO) {
        return getBaseMapper().selectByPage(safetyEmergencyHandlingDetailBO.parse(),safetyEmergencyHandlingDetailBO);
    }
}

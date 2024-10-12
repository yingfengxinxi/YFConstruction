package com.kakarote.build.safety.service;

import com.kakarote.build.qual.entity.BO.QualMonitsetBO;
import com.kakarote.build.qual.entity.PO.QualMonitset;
import com.kakarote.build.safety.entity.BO.SafetyEmergencyHandlingDetailBO;
import com.kakarote.build.safety.entity.PO.SafetyEmergencyHandlingDetail;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 应急事件处置明细 服务类
 * </p>
 *
 * @author zy
 * @since 2021-04-06
 */
public interface ISafetyEmergencyHandlingDetailService extends BaseService<SafetyEmergencyHandlingDetail> {

    public BasePage<SafetyEmergencyHandlingDetail> selectList(SafetyEmergencyHandlingDetailBO safetyEmergencyHandlingDetailBO);

}

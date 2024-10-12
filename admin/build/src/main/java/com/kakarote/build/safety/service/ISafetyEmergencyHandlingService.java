package com.kakarote.build.safety.service;

import com.kakarote.build.safety.entity.BO.SafetyEmergencyHandlingBO;
import com.kakarote.build.safety.entity.PO.SafetyEmergencyHandling;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * 应急事件处置 服务类
 * </p>
 *
 * @author zy
 * @since 2021-04-06
 */
public interface ISafetyEmergencyHandlingService extends BaseService<SafetyEmergencyHandling> {

    public BasePage<SafetyEmergencyHandling> selectList(SafetyEmergencyHandlingBO safetyEmergencyHandlingBO);

    public String saveData(SafetyEmergencyHandling safetyEmergencyHandling);

    public String updateData(SafetyEmergencyHandling safetyEmergencyHandling);

    public Result updateByZHIdMap(Map<String,Object>map);
}

package com.kakarote.build.safety.service;

import com.kakarote.build.safety.entity.BO.BSafetySafetyDataBO;
import com.kakarote.build.safety.entity.BO.BSafetySafetyEmergencyBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyData;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEmergency;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 安全应急管理 服务类
 * </p>
 *
 * @author author
 * @since 2021-03-22
 */
public interface IBSafetySafetyEmergencyService extends BaseService<BSafetySafetyEmergency> {
    /**
     * 查询数据列表
     *
     */
    public BasePage<BSafetySafetyEmergency> selectList(BSafetySafetyEmergencyBO bSafetySafetyEmergencyBO);
}

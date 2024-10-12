package com.kakarote.build.safety.service.impl;

import com.kakarote.build.safety.entity.BO.BSafetySafetyDataBO;
import com.kakarote.build.safety.entity.BO.BSafetySafetyEmergencyBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyData;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEmergency;
import com.kakarote.build.safety.mapper.BSafetySafetyEmergencyMapper;
import com.kakarote.build.safety.service.IBSafetySafetyEmergencyService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 安全应急管理 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-03-22
 */
@Service
public class BSafetySafetyEmergencyServiceImpl extends BaseServiceImpl<BSafetySafetyEmergencyMapper, BSafetySafetyEmergency> implements IBSafetySafetyEmergencyService {

    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BSafetySafetyEmergency> selectList(BSafetySafetyEmergencyBO bSafetySafetyEmergencyBO)
    {
        BasePage<BSafetySafetyEmergency> adminMessageBasePage = getBaseMapper().selectDataList(bSafetySafetyEmergencyBO.parse(),bSafetySafetyEmergencyBO);
        return adminMessageBasePage;
    }
}

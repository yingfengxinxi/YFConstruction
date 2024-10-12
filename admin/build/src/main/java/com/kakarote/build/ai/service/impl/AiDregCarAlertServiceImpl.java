package com.kakarote.build.ai.service.impl;

import com.kakarote.build.ai.entity.BO.AiDregCarAlertBO;
import com.kakarote.build.ai.entity.PO.AiDregCarAlert;
import com.kakarote.build.ai.mapper.AiDregCarAlertMapper;
import com.kakarote.build.ai.service.IAiDregCarAlertService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 渣土车辆智能分析 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-06-04
 */
@Service
public class AiDregCarAlertServiceImpl extends BaseServiceImpl<AiDregCarAlertMapper, AiDregCarAlert> implements IAiDregCarAlertService {

    @Override
    public BasePage<AiDregCarAlert> selectByPage(AiDregCarAlertBO aiDregCarAlertBO) {
        aiDregCarAlertBO.setOrdersBd("a.alert_time",false);
        BasePage<AiDregCarAlert> list = getBaseMapper().selectByPage(aiDregCarAlertBO.parse(), aiDregCarAlertBO);
        return list;
    }

    @Override
    public BasePage<Map<String, Object>> selectDregAlertList(AiDregCarAlertBO aiDregCarAlertBO) {
        BasePage<Map<String, Object>> list = getBaseMapper().selectDregAlertList(aiDregCarAlertBO.parse(), aiDregCarAlertBO);
        return list;
    }

    @Override
    public BasePage<AiDregCarAlert> selectByPagePro(AiDregCarAlertBO aiDregCarAlertBO) {
        aiDregCarAlertBO.setOrdersBd("a.alert_time",false);
        BasePage<AiDregCarAlert> list = getBaseMapper().selectByPagePro(aiDregCarAlertBO.parse(), aiDregCarAlertBO);
        return list;
    }
}

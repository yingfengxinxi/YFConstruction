package com.kakarote.build.safety.service.impl;

import com.kakarote.build.safety.entity.BO.SafetyStaticRiskBO;
import com.kakarote.build.safety.entity.PO.SafetyStaticRisk;
import com.kakarote.build.safety.mapper.SafetyStaticRiskMapper;
import com.kakarote.build.safety.service.ISafetyStaticRiskService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 静态风险数据 服务实现类
 * </p>
 *
 * @author shz
 * @since 2021-11-10
 */
@Service
public class SafetyStaticRiskServiceImpl extends BaseServiceImpl<SafetyStaticRiskMapper, SafetyStaticRisk> implements ISafetyStaticRiskService {

    @Override
    public BasePage<SafetyStaticRisk> selectList(SafetyStaticRiskBO safetyStaticRiskBO) {
        safetyStaticRiskBO.setOrdersBd("publish_date", false);
        return getBaseMapper().selectPageList(safetyStaticRiskBO.parse(),safetyStaticRiskBO);
    }
}

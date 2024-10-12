package com.kakarote.build.safety.service;

import com.kakarote.build.safety.entity.BO.SafetyDynamicRiskBO;
import com.kakarote.build.safety.entity.BO.SafetyStaticRiskBO;
import com.kakarote.build.safety.entity.PO.SafetyDynamicRisk;
import com.kakarote.build.safety.entity.PO.SafetyStaticRisk;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 静态风险数据 服务类
 * </p>
 *
 * @author shz
 * @since 2021-11-10
 */
public interface ISafetyStaticRiskService extends BaseService<SafetyStaticRisk> {

    BasePage<SafetyStaticRisk> selectList(SafetyStaticRiskBO safetyStaticRiskBO);

}

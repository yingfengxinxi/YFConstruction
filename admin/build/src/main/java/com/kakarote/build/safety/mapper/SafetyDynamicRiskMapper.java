package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.SafetyDynamicRiskBO;
import com.kakarote.build.safety.entity.PO.SafetyDynamicRisk;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 动态风险数据 Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2021-11-10
 */
public interface SafetyDynamicRiskMapper extends BaseMapper<SafetyDynamicRisk> {

    BasePage<SafetyDynamicRisk> selectPageList(BasePage<AdminMessage> parse, @Param("data") SafetyDynamicRiskBO safetyDynamicRisk);

}

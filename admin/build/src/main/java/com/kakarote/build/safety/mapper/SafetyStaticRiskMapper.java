package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.SafetyStaticRiskBO;
import com.kakarote.build.safety.entity.PO.SafetyStaticRisk;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 静态风险数据 Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2021-11-10
 */
public interface SafetyStaticRiskMapper extends BaseMapper<SafetyStaticRisk> {

    BasePage<SafetyStaticRisk> selectPageList(BasePage<AdminMessage> parse, @Param("data") SafetyStaticRiskBO safetyStaticRiskBO);

}

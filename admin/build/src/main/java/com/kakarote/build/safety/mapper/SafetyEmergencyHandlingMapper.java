package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.SafetyEmergencyHandlingBO;
import com.kakarote.build.safety.entity.PO.SafetyEmergencyHandling;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 应急事件处置 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-04-06
 */
public interface SafetyEmergencyHandlingMapper extends BaseMapper<SafetyEmergencyHandling> {

    BasePage<SafetyEmergencyHandling> selectByPage(BasePage<SafetyEmergencyHandling> parse, @Param("data") SafetyEmergencyHandlingBO safetyEmergencyHandlingBO);

}

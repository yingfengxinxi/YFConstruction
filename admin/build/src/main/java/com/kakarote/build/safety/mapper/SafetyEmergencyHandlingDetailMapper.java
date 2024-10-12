package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.SafetyEmergencyHandlingDetailBO;
import com.kakarote.build.safety.entity.PO.SafetyEmergencyHandlingDetail;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 应急事件处置明细 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-04-06
 */
public interface SafetyEmergencyHandlingDetailMapper extends BaseMapper<SafetyEmergencyHandlingDetail> {
    BasePage<SafetyEmergencyHandlingDetail> selectByPage(BasePage<SafetyEmergencyHandlingDetail> parse, @Param("data") SafetyEmergencyHandlingDetailBO safetyEmergencyHandlingDetailBO);

}

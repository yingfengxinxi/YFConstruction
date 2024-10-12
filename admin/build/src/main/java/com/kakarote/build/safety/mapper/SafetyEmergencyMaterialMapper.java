package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.BSafetySafetyDataBO;
import com.kakarote.build.safety.entity.BO.SafetyEmergencyMaterialBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyData;
import com.kakarote.build.safety.entity.PO.SafetyEmergencyMaterial;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 应急物资管理 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-04-06
 */
public interface SafetyEmergencyMaterialMapper extends BaseMapper<SafetyEmergencyMaterial> {
    BasePage<SafetyEmergencyMaterial> selectDataList(BasePage<AdminMessage> parse,@Param("data") SafetyEmergencyMaterialBO SafetyEmergencyMaterialBO);
}

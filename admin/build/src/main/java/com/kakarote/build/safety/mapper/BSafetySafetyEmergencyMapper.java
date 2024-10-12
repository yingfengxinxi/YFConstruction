package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.BSafetySafetyDataBO;
import com.kakarote.build.safety.entity.BO.BSafetySafetyEmergencyBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyData;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEmergency;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 安全应急管理 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-03-22
 */
public interface BSafetySafetyEmergencyMapper extends BaseMapper<BSafetySafetyEmergency> {

    BasePage<BSafetySafetyEmergency> selectDataList(BasePage<AdminMessage> parse, @Param("data") BSafetySafetyEmergencyBO bSafetySafetyEmergencyBO);

}

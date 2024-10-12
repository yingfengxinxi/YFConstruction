package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.BSafetySafetyDataBO;
import com.kakarote.build.safety.entity.BO.BSafetySafetyEducationBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyData;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEducation;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 安全资料管理 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-03-22
 */
public interface BSafetySafetyDataMapper extends BaseMapper<BSafetySafetyData> {

    BasePage<BSafetySafetyData> selectDataList(BasePage<AdminMessage> parse, @Param("data") BSafetySafetyDataBO bSafetySafetyDataBO);

}

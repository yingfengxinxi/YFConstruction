package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.BSafetySafetyEducationBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEducation;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 安全教育培训管理 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-03-22
 */
public interface BSafetySafetyEducationMapper extends BaseMapper<BSafetySafetyEducation> {

    BasePage<BSafetySafetyEducation> selectDataList(BasePage<AdminMessage> parse, @Param("data") BSafetySafetyEducationBO bSafetySafetyEducationBO);

}

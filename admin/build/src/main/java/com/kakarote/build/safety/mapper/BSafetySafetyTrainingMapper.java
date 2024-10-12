package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.BSafetySafetyDataBO;
import com.kakarote.build.safety.entity.BO.BSafetySafetyTrainingBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyData;
import com.kakarote.build.safety.entity.PO.BSafetySafetyTraining;
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
 * @since 2021-04-08
 */
public interface BSafetySafetyTrainingMapper extends BaseMapper<BSafetySafetyTraining> {

    BasePage<BSafetySafetyTraining> selectDataList(BasePage<AdminMessage> parse, @Param("data") BSafetySafetyTrainingBO bSafetySafetyTrainingBO);

}

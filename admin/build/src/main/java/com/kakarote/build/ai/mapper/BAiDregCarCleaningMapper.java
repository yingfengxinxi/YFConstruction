package com.kakarote.build.ai.mapper;

import com.kakarote.build.ai.entity.BO.AiDregCarBO;
import com.kakarote.build.ai.entity.BO.BAiDregCarCleaningBO;
import com.kakarote.build.ai.entity.PO.AiDregCar;
import com.kakarote.build.ai.entity.PO.BAiDregCarCleaning;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车辆清洗记录 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-09-22
 */
public interface BAiDregCarCleaningMapper extends BaseMapper<BAiDregCarCleaning> {

    BasePage<BAiDregCarCleaning> selectByPage(BasePage<BAiDregCarCleaningBO> parse, @Param("data") BAiDregCarCleaningBO bAiDregCarCleaningBO);

    List<Map<String, Object>> selectReportData(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> selectTypeProportion();

    List<Map<String, Object>> selectTop10List();
}

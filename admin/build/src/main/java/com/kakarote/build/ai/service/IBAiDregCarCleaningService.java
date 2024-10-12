package com.kakarote.build.ai.service;

import com.kakarote.build.ai.entity.BO.AiDregCarBO;
import com.kakarote.build.ai.entity.BO.BAiDregCarCleaningBO;
import com.kakarote.build.ai.entity.PO.AiDregCar;
import com.kakarote.build.ai.entity.PO.BAiDregCarCleaning;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车辆清洗记录 服务类
 * </p>
 *
 * @author author
 * @since 2021-09-22
 */
public interface IBAiDregCarCleaningService extends BaseService<BAiDregCarCleaning> {

    BasePage<BAiDregCarCleaning> selectByPage(BAiDregCarCleaningBO bAiDregCarCleaningBO);

    List<Map<String, Object>> selectReportData(Map<String, Object> params);

    List<Map<String, Object>> selectTypeProportion();

    List<Map<String, Object>> selectTop10List();
}

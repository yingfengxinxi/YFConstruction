package com.kakarote.build.ai.service;

import com.kakarote.build.ai.entity.BO.AiBareSoilDataBO;
import com.kakarote.build.ai.entity.PO.AiBareSoilData;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 裸土报警数据 服务类
 * </p>
 *
 * @author lzy
 * @since 2022-01-06
 */
public interface IAiBareSoilDataService extends BaseService<AiBareSoilData> {

    BasePage<AiBareSoilData> selectByPage(AiBareSoilDataBO aiBareSoilDataBO);

    List<Map<String, Object>> selectReportData(Map<String, Object> params);

    Map<String, Object> selectAlertNumber();

    List<Map<String, Object>> selectAlertTrend(Map<String, Object> params);

    List<Map<String, Object>> selectProjectAlertNumber();

    BasePage<Map<String, Object>> selectPageNj(AiBareSoilDataBO aiBareSoilDataBO);

    List<Map<String, Object>> selectTypeProportion();
}

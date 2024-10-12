package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.SafetyRiskDetectionBO;
import com.kakarote.build.safety.entity.PO.SafetyRiskDetection;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 安全隐患排查 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2022-06-27
 */
public interface SafetyRiskDetectionMapper extends BaseMapper<SafetyRiskDetection> {

    BasePage<SafetyRiskDetection> selectByPage(BasePage<Object> parse, @Param("data") SafetyRiskDetectionBO riskDetectionBO);

    List<Long> getQueuesUserList(@Param("id") Integer id);

    Map<String, Object> selectQuestionNumber();

    List<Map<String, Object>> selectSafetyHiddenDanger(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> selectSafetyLevelAnalysis();

    List<Map<String, Object>> selectNumByType(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> selectSafetyDataByPrinipal();

    List<Map<String, Object>> selectPieData();

    Map<String, Object> selectSafetyQuestionsQuantity();

    List<Map<String, Object>> selectBarData();

    List<Map<String, Object>> selectCurveData();

    BasePage<SafetyRiskDetection> selectTimeoutList(BasePage<Object> parse, @Param("data") SafetyRiskDetectionBO riskDetectionBO);

    BasePage<Map<String, Object>> selectQuestionList(BasePage<Object> parse, @Param("data") Map<String, Object> params);

    BasePage<SafetyRiskDetection> selectListByPro(BasePage<Object> parse, @Param("data") SafetyRiskDetectionBO riskDetectionBO);

    Map<String, Object> selectProDangerNumber();

    Map<String, Object> selectAvgRectifyTime();

    Map<String, Object> selectQuantityCompletion();

    List<Map<String, Object>> selectDangerNumberProportion(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> selectTrendAnalyses(@Param("data") Map<String, Object> params);

    Map<String, Object> selectProDangerLevelProportion();

    Map<String, Object> selectProDangerTypeProportion();

    List<Map<String, Object>> selectProDangerRank();

    List<Map<String, Object>> selectProAgingAnalysis();
}

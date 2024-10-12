package com.kakarote.build.safety.service;

import com.kakarote.build.safety.entity.BO.SafetyRiskDetectionBO;
import com.kakarote.build.safety.entity.PO.SafetyRiskDetection;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 安全隐患排查 服务类
 * </p>
 *
 * @author lzy
 * @since 2022-06-27
 */
public interface ISafetyRiskDetectionService extends BaseService<SafetyRiskDetection> {

    BasePage<SafetyRiskDetection> selectByPage(SafetyRiskDetectionBO riskDetectionBO);

    Result saveInfo(SafetyRiskDetection riskDetection);

    boolean updateInfo(SafetyRiskDetection riskDetection);

    Map<String, Object> selectQuestionNumber();

    List<Map<String, Object>> selectSafetyHiddenDanger(Map<String, Object> params);

    List<Map<String, Object>> selectSafetyLevelAnalysis();

    List<Map<String, Object>> selectNumByType(Map<String, Object> params);

    List<Map<String, Object>> selectSafetyDataByPrinipal();

    List<Map<String, Object>> selectPieData();

    Map<String, Object> selectSafetyQuestionsQuantity();

    List<Map<String, Object>> selectBarData();

    Map<String, Object> selectCurveData();

    List<Map<String, Object>> selectRectifyDateWarning();

    BasePage<SafetyRiskDetection> selectTimeoutList(SafetyRiskDetectionBO riskDetectionBO);

    Result dangeTimeoutPush(Integer id);

    BasePage<Map<String, Object>> selectQuestionList(Map<String, Object> params);

    BasePage<SafetyRiskDetection> selectPhotoList(SafetyRiskDetectionBO riskDetectionBO);

    BasePage<SafetyRiskDetection> selectListByPro(SafetyRiskDetectionBO riskDetectionBO);

    Map<String, Object> selectProDangerNumber();

    Map<String, Object> selectProDangerRectifyEfficiency();

    List<Map<String, Object>> selectDangerNumberProportion(Map<String, Object> params);

    List<Map<String, Object>> selectTrendAnalyses(String type);

    Map<String, Object> selectProDangerLevelProportion();

    Map<String, Object> selectProDangerTypeProportion();

    List<Map<String, Object>> selectProDangerRank();

    List<Map<String, Object>> selectProAgingAnalysis();
}

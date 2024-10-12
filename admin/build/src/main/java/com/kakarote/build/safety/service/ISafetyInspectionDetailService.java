package com.kakarote.build.safety.service;

import com.kakarote.build.safety.entity.BO.SafetyInspectionDetailBO;
import com.kakarote.build.safety.entity.PO.SafetyInspectionDetail;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工安全--安全巡检信息--子表巡检明细 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-04-13
 */
public interface ISafetyInspectionDetailService extends BaseService<SafetyInspectionDetail> {

    List<Map<String, Object>> selectPieData();

    List<Map<String, Object>> selectBarData();

    Map<String, Object> selectCurveData();

    BasePage<SafetyInspectionDetail> selectDanger(SafetyInspectionDetailBO safetyInspectionDetailBO);

    List<Map<String, Object>> selectRectifyDateWarning();

    List<Map<String, Object>> selectSafetyDataByPrinipal();

    Map<String, Object> selectSafetyQuestionsQuantity();

    List<Map<String, Object>> selectNumByType(Map<String, Object> params);

    BasePage<SafetyInspectionDetail> selectListByPro(SafetyInspectionDetailBO inspectionDetailBO);

    SafetyInspectionDetail selectDetailByIdPro(Integer id);

    Map<String, Object> selectProDangerNumber();

    Map<String, Object> selectProDangerLevelProportion();

    Map<String, Object> selectProDangerTypeProportion();

    Map<String, Object> selectProDangerRectifyEfficiency();

    List<Map<String, Object>> selectTrendAnalyses(String type);

    List<Map<String, Object>> selectProDangerRank();

    List<Map<String, Object>> selectDangerNumberProportion(Map<String, Object> params);

    List<Map<String, Object>> selectProAgingAnalysis();

    List<Map<String, Object>> selectSafetyLevelAnalysis();

    Map<String, Object> selectQuestionNumber();

    List<Map<String, Object>> selectHiddenDangerInfo(String iconType);

    BasePage<Map<String,Object>> selectQuestionList(Map<String,Object> params);

    /**
     * 查询项目的安全隐患总数和今日安全隐患数量
     * @return
     */
    List<Map<String, Object>> proDangerNumList();
}

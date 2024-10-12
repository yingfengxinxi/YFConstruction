package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.SafetyInspectionDetailBO;
import com.kakarote.build.safety.entity.PO.SafetyInspectionDetail;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工安全--安全巡检信息--子表巡检明细 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-04-13
 */
public interface SafetyInspectionDetailMapper extends BaseMapper<SafetyInspectionDetail> {

    List<Map<String, Object>> selectPieData();

    List<Map<String, Object>> selectBarData();

    List<Map<String, Object>> selectCurveData(Integer id);

    BasePage<SafetyInspectionDetail> selectDanger(BasePage<SafetyInspectionDetail> parse,
                                                  @Param("data")SafetyInspectionDetailBO inspectionInfoBO);

    Map<String, Object> selectRectifyDateWarning();

    List<Map<String, Object>> selectSafetyDataByPrinipal();

    Map<String, Object> selectSafetyQuestionsQuantity();

    List<Map<String, Object>> selectNumByType(@Param("data") Map<String, Object> params);

    BasePage<SafetyInspectionDetail> selectListByPro(BasePage<Object> parse, @Param("data") SafetyInspectionDetailBO inspectionDetailBO);

    SafetyInspectionDetail selectDetailByIdPro(@Param("id") Integer id);

    Map<String, Object> selectProDangerNumber();

    Map<String, Object> selectProDangerLevelProportion();

    Map<String, Object> selectProDangerTypeProportion();

    Map<String, Object> selectAvgRectifyTime();

    Map<String, Object> selectQuantityCompletion();

    List<Map<String, Object>> selectTrendAnalyses(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> selectProDangerRank();

    List<Map<String, Object>> selectDangerNumberProportion(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> selectProAgingAnalysis();

    List<Map<String, Object>> selectSafetyLevelAnalysis();

    List<Map<String, Object>> selectHiddenDangerInfo(@Param("iconType") String iconType);

    BasePage<Map<String,Object>> selectQuestionList(BasePage<Object> parse,@Param("data") Map<String,Object> params);

    List<Map<String, Object>> proDangerNumList();
}

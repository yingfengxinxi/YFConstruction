package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.QualRiskDetectionBO;
import com.kakarote.build.qual.entity.PO.QualRiskDetection;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzy
 * @since 2022-07-11
 */
public interface IQualRiskDetectionService extends BaseService<QualRiskDetection> {

    BasePage<QualRiskDetection> selectByPage(QualRiskDetectionBO riskDetectionBO);

    Result saveInfo(QualRiskDetection riskDetection);

    boolean updateInfo(QualRiskDetection riskDetection);

    List<Map<String, Object>> selectPieData();

    List<Map<String, Object>> selectBarData(Map<String, Object> params);

    List<Map<String, Object>> selectRectifyDateWarning();

    List<Map<String, Object>> selectQualDataByPrinipal();

    Map<String, Object> selectQualQuestionsQuantity();
}

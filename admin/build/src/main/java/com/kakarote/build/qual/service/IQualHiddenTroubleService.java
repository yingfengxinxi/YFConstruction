package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.QualCheckBO;
import com.kakarote.build.qual.entity.PO.QualCheck;
import com.kakarote.build.qual.entity.PO.QualHiddenTrouble;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工质量--质量检查管理--安全隐患 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-03-23
 */
public interface IQualHiddenTroubleService extends BaseService<QualHiddenTrouble> {

    List<Map<String, Object>> selectPieData();

    List<Map<String, Object>> selectBarData(Map<String, Object> params);

    List<Map<String, Object>> selectRectifyDateWarning();

    List<Map<String, Object>> selectQualDataByPrinipal();

    Map<String, Object> selectQualQuestionsQuantity();
}

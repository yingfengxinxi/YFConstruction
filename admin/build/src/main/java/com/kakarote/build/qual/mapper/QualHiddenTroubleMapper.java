package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.PO.QualHiddenTrouble;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工质量--质量检查管理--安全隐患 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-03-23
 */
public interface QualHiddenTroubleMapper extends BaseMapper<QualHiddenTrouble> {

    List<Map<String, Object>> selectPieData();

    List<Map<String, Object>> selectBarData(@Param("data") Map<String, Object> params);

    Map<String, Object> selectRectifyDateWarning();

    List<Map<String, Object>> selectQualDataByPrinipal();

    Map<String, Object> selectQualQuestionsQuantity();
}

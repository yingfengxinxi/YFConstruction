package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.QualRiskDetectionBO;
import com.kakarote.build.qual.entity.PO.QualRiskDetection;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2022-07-11
 */
public interface QualRiskDetectionMapper extends BaseMapper<QualRiskDetection> {

    BasePage<QualRiskDetection> selectByPage(BasePage<Object> parse, @Param("data") QualRiskDetectionBO riskDetectionBO);

    List<Long> getQueuesUserList(@Param("id") Integer id);

    List<Map<String, Object>> selectPieData();

    List<Map<String, Object>> selectBarData(@Param("data") Map<String, Object> params);

    Map<String, Object> selectRectifyDateWarning();

    List<Map<String, Object>> selectQualDataByPrinipal();

    Map<String, Object> selectQualQuestionsQuantity();
}

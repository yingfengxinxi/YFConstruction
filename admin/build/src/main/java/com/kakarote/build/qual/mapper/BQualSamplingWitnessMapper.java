package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.BQualSamplingWitnessBO;
import com.kakarote.build.qual.entity.PO.BQualSamplingWitness;
import com.kakarote.build.wx.entity.BO.BWxFeedbackBO;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 见证取样表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-07-30
 */
public interface BQualSamplingWitnessMapper extends BaseMapper<BQualSamplingWitness> {

    BasePage<BQualSamplingWitnessBO> selectDataList(BasePage<AdminMessage> parse, @Param("data") BQualSamplingWitnessBO bQualSamplingWitnessBO);

    BasePage<BQualSamplingWitnessBO> selectListNew(BasePage<Object> parse, @Param("data") BQualSamplingWitnessBO bQualSamplingWitnessBO);
}

package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.BQualSamplingProcessBO;
import com.kakarote.build.qual.entity.PO.BQualSamplingProcess;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEmergencyMembers;
import com.kakarote.build.wx.entity.BO.BWxFeedbackBO;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 见证取样过程表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-07-30
 */
public interface BQualSamplingProcessMapper extends BaseMapper<BQualSamplingProcess> {

    BasePage<BQualSamplingProcessBO> selectDataList(BasePage<AdminMessage> parse, @Param("data") BQualSamplingProcessBO bQualSamplingProcessBO);
    public Integer  deletePid(String pid);

    List<BQualSamplingProcess> selectDataListPid(String pid);
}

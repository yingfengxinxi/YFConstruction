package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.QualAppraisingdeclareMainGrpBO;
import com.kakarote.build.qual.entity.PO.QualAppraisingdeclareMainGrp;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 施工质量--创优评优申报--主表 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-04-20
 */
public interface QualAppraisingdeclareMainGrpMapper extends BaseMapper<QualAppraisingdeclareMainGrp> {

    BasePage<QualAppraisingdeclareMainGrp> selectDataList(BasePage<Object> parse,@Param("data") QualAppraisingdeclareMainGrpBO qualAppraisingdeclareMainGrpBO);
}

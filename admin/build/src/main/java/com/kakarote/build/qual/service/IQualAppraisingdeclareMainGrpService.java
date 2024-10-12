package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.QualAppraisingdeclareMainGrpBO;
import com.kakarote.build.qual.entity.PO.QualAppraisingdeclareMainGrp;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * 施工质量--创优评优申报--主表 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-04-20
 */
public interface IQualAppraisingdeclareMainGrpService extends BaseService<QualAppraisingdeclareMainGrp> {

    BasePage<QualAppraisingdeclareMainGrp> selectList(QualAppraisingdeclareMainGrpBO qualAppraisingdeclareMainGrpBO);

    int add(Map<String,Object> params);

    int edit(Map<String,Object> params);

    int delete(String id);

    Map<String,Object>  selectById(String id);
}

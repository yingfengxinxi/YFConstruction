package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.QualCheckBO;
import com.kakarote.build.qual.entity.PO.QualCheck;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * 施工质量--质量检查管理 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-03-23
 */
public interface IQualCheckService extends BaseService<QualCheck> {

    BasePage<QualCheck> selectByPage(QualCheckBO qualCheckBO);

    Integer saveOrUpdateInfo(Map<String, Object> params);

    Integer receiptInfo(Map<String, Object> params);

    Map<String, Object> seletcCheckInfoById(Integer checkId);

    Map<String, Object> getPrintData(Integer id);
}

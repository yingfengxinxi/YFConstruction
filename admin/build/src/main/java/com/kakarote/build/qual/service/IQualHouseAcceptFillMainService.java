package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.QualHouseAcceptFillMainBO;
import com.kakarote.build.qual.entity.PO.QualHouseAcceptFillMain;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * 施工质量--分户验收--填报--主表 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-06-29
 */
public interface IQualHouseAcceptFillMainService extends BaseService<QualHouseAcceptFillMain> {

    BasePage<QualHouseAcceptFillMain> selectByPage(QualHouseAcceptFillMainBO qualHouseAcceptFillMainBO);

    BasePage<QualHouseAcceptFillMainBO> wxselectByPage(QualHouseAcceptFillMainBO qualHouseAcceptFillMainBO);

    Map<String, Object> selectById(Integer id);

    QualHouseAcceptFillMainBO wxselectById(Integer id);

    boolean SaveOrUpdate(Map<String,Object> map);

    boolean wxSaveOrUpdate(Map<String,Object> map);

    boolean deleteById(Integer id);
}

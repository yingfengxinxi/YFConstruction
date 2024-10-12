package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.QualAcceptTemplateMainSetBO;
import com.kakarote.build.qual.entity.PO.QualAcceptTemplateMainSet;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * 施工质量--分户验收--模板设置--主表 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-06-28
 */
public interface IQualAcceptTemplateMainSetService extends BaseService<QualAcceptTemplateMainSet> {

    BasePage<QualAcceptTemplateMainSet> selectByPage(QualAcceptTemplateMainSetBO qualAcceptTemplateMainSetBO);

    boolean SaveOrUpdate(Map<String,Object> map);

    Map<String,Object>  selectById(Integer id);

    boolean deleteById(Integer id);
}

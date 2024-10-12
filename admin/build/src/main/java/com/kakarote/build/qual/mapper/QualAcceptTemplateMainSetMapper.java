package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.QualAcceptTemplateMainSetBO;
import com.kakarote.build.qual.entity.PO.QualAcceptTemplateMainSet;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 施工质量--分户验收--模板设置--主表 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-06-28
 */
public interface QualAcceptTemplateMainSetMapper extends BaseMapper<QualAcceptTemplateMainSet> {

    BasePage<QualAcceptTemplateMainSet> selectByPage(BasePage<Object> parse, @Param("data") QualAcceptTemplateMainSetBO qualAcceptTemplateMainSetBO);
}

package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.QualCheckPersonnelBO;
import com.kakarote.build.qual.entity.PO.QualCheckPersonnel;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 验收人员管理 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-07-08
 */
public interface IQualCheckPersonnelService extends BaseService<QualCheckPersonnel> {

    BasePage<QualCheckPersonnel> selectByPage(QualCheckPersonnelBO qualCheckPersonnelBO);

    List<Map> selectMap();

    List<QualCheckPersonnel> selectWxList(QualCheckPersonnelBO qualCheckPersonnelBO);

}

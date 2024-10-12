package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.QualHouseTypeMangeBO;
import com.kakarote.build.qual.entity.PO.QualHouseName;
import com.kakarote.build.qual.entity.PO.QualHouseTypeMange;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工质量--分户验收--户型管理 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-06-24
 */
public interface IQualHouseTypeMangeService extends BaseService<QualHouseTypeMange> {

    BasePage<QualHouseTypeMange> selectByPage(QualHouseTypeMangeBO qualHouseTypeMangeBO);

    boolean saveOrUpdateDiy(QualHouseTypeMange qualHouseTypeMange, List<QualHouseName> qualHouseName,List<QualHouseName> qualHouseName2);

    boolean CopyHouseType(QualHouseTypeMange qualHouseTypeMange);

    QualHouseTypeMange getByIdDiy(Integer id);

    List<Map> wxGetByIdDiy(QualHouseTypeMange qualHouseTypeMange);
}

package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.PO.QualBuildMange;
import com.kakarote.build.qual.entity.PO.QualQuestions;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工质量--分户验收--楼栋管理 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-06-22
 */
public interface IQualBuildMangeService extends BaseService<QualBuildMange> {


    List<QualBuildMange> queryByParentId(Integer parentId);

    List<Map<String, Object>> queryAllList(Integer pId);

    boolean removeListById(Integer dangerSourceId);

    int Save(QualBuildMange qualBuildMange);

    int CopyMenu(QualBuildMange qualBuildMange);

    boolean SaveDistrib(Integer houseTypeId, List<QualBuildMange> qualBuildMange);

    List<Map<String,Object>> queryTreeAllList(Integer pid);

    List<Map<String, Object>> getAllHouseList();

    List<QualBuildMange> wXqueryByParentId(QualBuildMange qualBuildMange);

}

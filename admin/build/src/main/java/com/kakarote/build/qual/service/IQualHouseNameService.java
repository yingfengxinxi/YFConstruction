package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.PO.QualHouseName;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工质量--分户验收--户型管理--子表--房间名称 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-06-24
 */
public interface IQualHouseNameService extends BaseService<QualHouseName> {

    List<QualHouseName> getByHouseTypeId(Integer id);
    Map<String,Object> getByHouseTypeId2(Integer id);
    boolean delByHouseTypeId(Integer id);

    //根据户型id和验收工序查询模板信息--分户验收填报使用
    Map getInfoByMap(Map map);

}

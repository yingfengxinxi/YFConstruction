package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.PO.QualHouseName;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工质量--分户验收--户型管理--子表--房间名称 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-06-24
 */
public interface QualHouseNameMapper extends BaseMapper<QualHouseName> {

    List<QualHouseName> getByHouseTypeId(@Param("id") Integer id);

    boolean delByHouseTypeId(@Param("id") Integer id);

    Map getInfoByMap(@Param("data") Map map);
}

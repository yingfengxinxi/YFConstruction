package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.PO.QualHouseAcceptFillSon;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工质量--分户验收--填报--子表 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-06-29
 */
public interface QualHouseAcceptFillSonMapper extends BaseMapper<QualHouseAcceptFillSon> {

    int wxSave(@Param("data")Map map);

    List<QualHouseAcceptFillSon> wxSelectByPid(Integer id);

}

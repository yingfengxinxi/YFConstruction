package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.QualMonitSTbZbBO;
import com.kakarote.build.qual.entity.PO.QualMonitSTbZb;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 监测数据填报--子表 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-03-24
 */
public interface QualMonitSTbZbMapper extends BaseMapper<QualMonitSTbZb> {

    BasePage<QualMonitSTbZb> selectList(BasePage<QualMonitSTbZb> page, @Param("qualMonitSTbZbBO") QualMonitSTbZbBO qualMonitSTbZbBO);

    List<QualMonitSTbZb> selectListNoPage(@Param("qualMonitSTbZbBO") QualMonitSTbZbBO qualMonitSTbZbBO);

    List<Map>selectT(@Param("pid") Integer pid);

    Integer deletByZHid(@Param("pid") Integer pid);

    List<Map>selectByOutRangeStaticsAll(@Param("data") Map map);

    List<Map>selectByOutRangeStaticsSelect(@Param("data") Map map);
}

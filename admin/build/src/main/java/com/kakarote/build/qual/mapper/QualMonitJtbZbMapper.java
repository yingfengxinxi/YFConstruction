package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.PO.QualMonitJtbZb;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 检测检验--监测数据填报--子表 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-03-25
 */
public interface QualMonitJtbZbMapper extends BaseMapper<QualMonitJtbZb> {

    List<Map> selectT(@Param("pid") Integer pid);

    Integer deletByZHid(@Param("pid") Integer pid);

}

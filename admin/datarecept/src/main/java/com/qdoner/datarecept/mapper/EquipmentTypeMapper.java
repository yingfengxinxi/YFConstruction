package com.qdoner.datarecept.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.qdoner.datarecept.entity.PO.EquipmentType;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备类型信息 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
public interface EquipmentTypeMapper extends BaseMapper<EquipmentType> {

    List<Map<String, Object>> getByPid(@Param("pid") Integer pid);

    EquipmentType getById(Integer id);

    EquipmentType getPlaneById(@Param("id") Integer id);

    List<Map<String,Object>> queryTreeListByParentId(@Param("parentId") Integer parentId);

    List<Map<String, Object>> getGroupByPid(@Param("pid") Integer pid);
}

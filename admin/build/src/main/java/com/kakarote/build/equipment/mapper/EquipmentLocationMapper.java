package com.kakarote.build.equipment.mapper;

import com.kakarote.build.equipment.entity.BO.EquipmentLocationBO;
import com.kakarote.build.equipment.entity.PO.EquipmentLocation;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备位置信息 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
public interface EquipmentLocationMapper extends BaseMapper<EquipmentLocation> {


    List<Map<String, Object>> getByPid(@Param("pid") Integer pid);

    EquipmentLocation getById(@Param("id") Integer id);

    List<Map<String,Object>> queryTreeListByParentId(@Param("parentId")Integer parentId,@Param("data")Map<String,Object> map);

    List<Map<String, Object>> getGroupByPid(@Param("pid") Integer pid, @Param("data") EquipmentLocationBO equipmentLocationBO);
}

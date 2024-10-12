package com.kakarote.build.equipment.mapper;

import com.kakarote.build.equipment.entity.PO.EquipmentMonitorDataAt;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-监测项实时数据表 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2021-11-23
 */
public interface EquipmentMonitorDataAtMapper extends BaseMapper<EquipmentMonitorDataAt> {
    List<Map<String, Object>> listByPro(@Param("proId") Integer proId);

    List<Map<String, Object>> selectEnvDataAtList();

    List<Map<String, Object>> listByEquipment(@Param("equipmentId") Integer equipmentId);
}

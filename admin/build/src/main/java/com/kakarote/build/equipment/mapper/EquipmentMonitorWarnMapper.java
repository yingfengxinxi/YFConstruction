package com.kakarote.build.equipment.mapper;

import com.kakarote.build.equipment.entity.PO.EquipmentMonitorWarn;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备监测-监测类型通用设置 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2021-10-27
 */
public interface EquipmentMonitorWarnMapper extends BaseMapper<EquipmentMonitorWarn> {
    EquipmentMonitorWarn getByTypeId(@Param("typeId") String typeId, @Param("tenantId")String tenantId, @Param("projectId") String projectId);
}

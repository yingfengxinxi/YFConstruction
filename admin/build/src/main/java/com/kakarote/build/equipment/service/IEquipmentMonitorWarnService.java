package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.PO.EquipmentMonitorWarn;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 设备监测-监测类型通用设置 服务类
 * </p>
 *
 * @author wudw
 * @since 2021-10-27
 */
public interface IEquipmentMonitorWarnService extends BaseService<EquipmentMonitorWarn> {

    EquipmentMonitorWarn getByTypeId(String typeId, String tenantId, String projectId);
}

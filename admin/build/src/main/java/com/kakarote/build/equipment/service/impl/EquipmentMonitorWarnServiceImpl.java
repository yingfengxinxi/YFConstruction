package com.kakarote.build.equipment.service.impl;

import com.kakarote.build.equipment.entity.PO.EquipmentMonitorWarn;
import com.kakarote.build.equipment.mapper.EquipmentMonitorWarnMapper;
import com.kakarote.build.equipment.service.IEquipmentMonitorWarnService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备监测-监测类型通用设置 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-10-27
 */
@Service
public class EquipmentMonitorWarnServiceImpl extends BaseServiceImpl<EquipmentMonitorWarnMapper, EquipmentMonitorWarn> implements IEquipmentMonitorWarnService {

    @Override
    public EquipmentMonitorWarn getByTypeId(String typeId, String tenantId, String projectId) {
        return this.baseMapper.getByTypeId(typeId, tenantId, projectId);
    }
}

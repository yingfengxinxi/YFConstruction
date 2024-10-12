package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.PO.EquipmentMonitorDataAt;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-监测项实时数据表 服务类
 * </p>
 *
 * @author wudw
 * @since 2021-11-23
 */
public interface IEquipmentMonitorDataAtService extends BaseService<EquipmentMonitorDataAt> {
    List<Map<String,Object>> listByPro(Integer proId);

    Map<String,Object> selectEnvDataAt();

    List<Map<String,Object>> listByEquipment(Integer equipmentId);
}

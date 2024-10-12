package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.BO.EquipmentLocationBO;
import com.kakarote.build.equipment.entity.PO.EquipmentLocation;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备位置信息 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
public interface IEquipmentLocationService extends BaseService<EquipmentLocation> {

    List<Map<String, Object>> queryAllList(Integer pid);

    EquipmentLocation selectById(Integer id);

    List<Map<String,Object>> queryTreeListByParentId(Integer parentId,Map<String,Object> map);

    List<Map<String, Object>> groupQueryAllList(Integer pid, EquipmentLocationBO equipmentLocationBO);
}

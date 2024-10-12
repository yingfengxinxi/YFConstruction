package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.PO.EquipmentType;
import com.kakarote.core.common.Result;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备类型信息 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
public interface IEquipmentTypeService extends BaseService<EquipmentType> {

    List<Map<String, Object>> queryAllList(Integer pid);

    EquipmentType selectById(Integer id);

    EquipmentType getPlaneById(Integer id);

    List<Map<String,Object>> queryTreeListByParentId(Integer parentId);

    List<Map<String, Object>> queryAllGroupList(Integer pid);

    List<EquipmentType> selectAllMonitorType();
}

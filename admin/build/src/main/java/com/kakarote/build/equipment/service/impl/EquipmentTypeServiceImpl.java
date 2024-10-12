package com.kakarote.build.equipment.service.impl;

import com.kakarote.build.equipment.entity.PO.EquipmentType;
import com.kakarote.build.equipment.mapper.EquipmentTypeMapper;
import com.kakarote.build.equipment.service.IEquipmentTypeService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备类型信息 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
@Service
public class EquipmentTypeServiceImpl extends BaseServiceImpl<EquipmentTypeMapper, EquipmentType> implements IEquipmentTypeService {

    @Override
    public List<Map<String, Object>> queryAllList(Integer pid) {
        List<Map<String, Object>> list = getBaseMapper().getByPid(pid);

        for (Map<String, Object> map : list) {
            List<Map<String, Object>> children = this.queryAllList((Integer) map.get("id"));
            map.put("children", children);
        }

        return list;
    }

    @Override
    public EquipmentType selectById(Integer id) {
        EquipmentType equipmentType = getBaseMapper().getById(id);
        return equipmentType;
    }

    @Override
    public EquipmentType getPlaneById(Integer id) {
        EquipmentType equipmentType = getBaseMapper().getPlaneById(id);
        return equipmentType;
    }

    @Override
    public List<Map<String, Object>> queryTreeListByParentId(Integer parentId) {
        return getBaseMapper().queryTreeListByParentId(parentId);
    }

    @Override
    public List<Map<String, Object>> queryAllGroupList(Integer pid) {
        List<Map<String, Object>> list = getBaseMapper().getGroupByPid(pid);

        for (Map<String, Object> map : list) {
            List<Map<String, Object>> children = this.queryAllList((Integer) map.get("id"));
            map.put("children", children);
        }

        return list;
    }

    @Override
    public List<EquipmentType> selectAllMonitorType() {
        return getBaseMapper().selectAllMonitorType();
    }
}

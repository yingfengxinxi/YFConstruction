package com.kakarote.build.equipment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.equipment.entity.BO.EquipmentLocationBO;
import com.kakarote.build.equipment.entity.PO.EquipmentLocation;
import com.kakarote.build.equipment.mapper.EquipmentLocationMapper;
import com.kakarote.build.equipment.service.IEquipmentLocationService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备位置信息 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
@Service
public class EquipmentLocationServiceImpl extends BaseServiceImpl<EquipmentLocationMapper, EquipmentLocation> implements IEquipmentLocationService {

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
    public EquipmentLocation selectById(Integer id) {
        EquipmentLocation location = getBaseMapper().getById(id);
        return location;
    }

    @Override
    public List<Map<String, Object>> queryTreeListByParentId(Integer parentId,Map<String,Object> map) {
        return getBaseMapper().queryTreeListByParentId(parentId,map);
    }

    @Override
    public List<Map<String, Object>> groupQueryAllList(Integer pid, EquipmentLocationBO equipmentLocationBO) {
        List<Map<String, Object>> list = getBaseMapper().getGroupByPid(pid, equipmentLocationBO);

        for (Map<String, Object> map : list) {
            List<Map<String, Object>> children = this.queryAllList((Integer) map.get("id"));
            map.put("children", children);
        }

        return list;
    }
}

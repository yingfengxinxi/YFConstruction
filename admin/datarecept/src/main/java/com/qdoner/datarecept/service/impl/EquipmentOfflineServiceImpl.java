package com.qdoner.datarecept.service.impl;


import com.kakarote.core.servlet.BaseServiceImpl;
import com.qdoner.datarecept.entity.PO.EquipmentOffline;
import com.qdoner.datarecept.mapper.EquipmentOfflineMapper;
import com.qdoner.datarecept.service.IEquipmentOfflineService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备上下线记录表 服务实现类
 * </p>
 *
 * @author shz
 * @since 2022-08-02
 */
@Service
public class EquipmentOfflineServiceImpl extends BaseServiceImpl<EquipmentOfflineMapper, EquipmentOffline> implements IEquipmentOfflineService {

    @Override
    public List<Map> selectOfflineTower() {
        return getBaseMapper().selectOfflineTower();
    }

    @Override
    public List<Map> selectOfflineElevator() {
        return getBaseMapper().selectOfflineElevator();
    }

    @Override
    public List<Map> getOfflineTower(Integer id) {
        return getBaseMapper().getOfflineTower(id);
    }

    @Override
    public List<Map> getOfflineElevator(Integer id) {
        return getBaseMapper().getOfflineElevator(id);
    }
}

package com.kakarote.build.equipment.service.impl;


import com.kakarote.build.equipment.entity.BO.EquipmentOfflineBO;
import com.kakarote.build.equipment.entity.PO.EquipmentOffline;
import com.kakarote.build.equipment.mapper.EquipmentOfflineMapper;
import com.kakarote.build.equipment.service.IEquipmentOfflineService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
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
    public BasePage<Map<String, Object>> selectPage(EquipmentOfflineBO offlineBO) {
        return getBaseMapper().selectPage(offlineBO.parse(),offlineBO);
    }
}

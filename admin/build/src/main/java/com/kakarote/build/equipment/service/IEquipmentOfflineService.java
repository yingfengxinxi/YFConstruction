package com.kakarote.build.equipment.service;


import com.kakarote.build.equipment.entity.BO.EquipmentOfflineBO;
import com.kakarote.build.equipment.entity.PO.EquipmentOffline;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备上下线记录表 服务类
 * </p>
 *
 * @author shz
 * @since 2022-08-02
 */
public interface IEquipmentOfflineService extends BaseService<EquipmentOffline> {

    List<Map> selectOfflineTower();

    List<Map> selectOfflineElevator();

    BasePage<Map<String,Object>> selectPage(EquipmentOfflineBO offlineBO);
}

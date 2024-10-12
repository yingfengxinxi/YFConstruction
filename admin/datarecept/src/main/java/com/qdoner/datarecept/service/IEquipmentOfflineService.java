package com.qdoner.datarecept.service;

import com.kakarote.core.servlet.BaseService;
import com.qdoner.datarecept.entity.PO.EquipmentOffline;

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

    List<Map> getOfflineTower(Integer id);

    List<Map> getOfflineElevator(Integer id);
}

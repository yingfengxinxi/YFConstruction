package com.qdoner.datarecept.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.qdoner.datarecept.entity.PO.EquipmentOffline;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 设备上下线记录表 Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2022-08-02
 */
public interface EquipmentOfflineMapper extends BaseMapper<EquipmentOffline> {

    List<Map> selectOfflineTower();

    List<Map> selectOfflineElevator();

    List<Map> getOfflineTower(Integer id);

    List<Map> getOfflineElevator(Integer id);
}

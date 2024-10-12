package com.qdoner.datarecept.mapper;

import com.qdoner.datarecept.entity.PO.BEquipmentTower;
import com.kakarote.core.servlet.BaseMapper;

/**
 * <p>
 * 塔机配置表 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2022-07-12
 */
public interface BEquipmentTowerMapper extends BaseMapper<BEquipmentTower> {

    int updateByEquipmentId(BEquipmentTower tower);

}

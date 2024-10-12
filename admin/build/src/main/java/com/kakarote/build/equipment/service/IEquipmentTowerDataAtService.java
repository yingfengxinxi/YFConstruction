package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.BO.EquipmentTowerDataAtBO;
import com.kakarote.build.equipment.entity.PO.EquipmentTowerDataAt;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 塔机设备实时数据表 服务类
 * </p>
 *
 * @author shz
 * @since 2022-03-22
 */
public interface IEquipmentTowerDataAtService extends BaseService<EquipmentTowerDataAt> {

    Result saveData(List<Map<String, Object>> list);

    BasePage<Map<String,Object>> selectPage(EquipmentTowerDataAtBO towerDataAtBO);
}

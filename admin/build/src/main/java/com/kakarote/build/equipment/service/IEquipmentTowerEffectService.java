package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.BO.EquipmentTowerEffectBO;
import com.kakarote.build.equipment.entity.PO.EquipmentTowerEffect;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shz
 * @since 2021-12-22
 */
public interface IEquipmentTowerEffectService extends BaseService<EquipmentTowerEffect> {

    Map selectDZTJ(Integer id);

    Map selectJRGX(Integer id);

    Map selectSJXX(String code);

    Result saveData(List<Map<String,Object>> list);

    BasePage<Map<String,Object>> selectPage(EquipmentTowerEffectBO towerEffectBO);

}

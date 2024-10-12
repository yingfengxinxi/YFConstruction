package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.BO.EquipmentElevatorEffectBO;
import com.kakarote.build.equipment.entity.PO.EquipmentElevatorEffect;
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
 * @since 2021-12-29
 */
public interface IEquipmentElevatorEffectService extends BaseService<EquipmentElevatorEffect> {

    Map selectDZTJ(Integer id);

    Map selectJRGX(Integer id);

    Map selectSJXX(String code);

    Result saveData(List<Map<String, Object>> list);

    BasePage<Map<String,Object>> selectPage(EquipmentElevatorEffectBO elevatorEffectBO);

}

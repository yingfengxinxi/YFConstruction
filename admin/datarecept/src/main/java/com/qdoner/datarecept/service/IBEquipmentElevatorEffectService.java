package com.qdoner.datarecept.service;

import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.qdoner.datarecept.entity.BO.EquipmentElevatorEffectBO;
import com.qdoner.datarecept.entity.PO.BEquipmentElevatorEffect;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 升降机功效数据 服务类
 * </p>
 *
 * @author wudw
 * @since 2022-07-21
 */
public interface IBEquipmentElevatorEffectService extends BaseService<BEquipmentElevatorEffect> {

    Map selectDZTJ(Integer id);

    Map selectJRGX(Integer id);

    Map selectSJXX(String code);

    Result saveData(List<Map<String, Object>> list);

}

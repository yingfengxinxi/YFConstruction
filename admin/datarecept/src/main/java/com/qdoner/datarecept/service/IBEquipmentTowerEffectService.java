package com.qdoner.datarecept.service;

import com.kakarote.core.common.Result;
import com.qdoner.datarecept.entity.PO.BEquipmentTowerEffect;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 塔机功效数据 服务类
 * </p>
 *
 * @author wudw
 * @since 2022-07-12
 */
public interface IBEquipmentTowerEffectService extends BaseService<BEquipmentTowerEffect> {


    Result saveData(List<Map<String,Object>> list);

}

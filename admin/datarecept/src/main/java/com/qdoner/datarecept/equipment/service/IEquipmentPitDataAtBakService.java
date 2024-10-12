package com.qdoner.datarecept.equipment.service;

import com.kakarote.core.common.Result;
import com.kakarote.core.servlet.BaseService;
import com.qdoner.datarecept.equipment.entity.PO.EquipmentPitDataAtBak;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-基坑设备实时数据表 服务类
 * </p>
 *
 * @author shz
 * @since 2022-03-30
 */
public interface IEquipmentPitDataAtBakService extends BaseService<EquipmentPitDataAtBak> {

    Result saveData(List<Map<String, Object>> list);

    Result saveFormworkData(List<Map<String, Object>> list);

}

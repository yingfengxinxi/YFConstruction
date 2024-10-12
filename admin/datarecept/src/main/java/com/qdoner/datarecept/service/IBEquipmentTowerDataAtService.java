package com.qdoner.datarecept.service;

import com.kakarote.core.common.Result;
import com.qdoner.datarecept.entity.PO.BEquipmentTowerDataAt;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 塔机设备实时数据表 服务类
 * </p>
 *
 * @author wudw
 * @since 2022-07-12
 */
public interface IBEquipmentTowerDataAtService extends BaseService<BEquipmentTowerDataAt> {

    Result saveData(List<Map<String, Object>> list);
}

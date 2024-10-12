package com.qdoner.datarecept.service;

import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.qdoner.datarecept.entity.BO.EquipmentElevatorDataAtBO;
import com.qdoner.datarecept.entity.PO.BEquipmentElevatorDataAt;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 升降电梯设备实时数据表 服务类
 * </p>
 *
 * @author wudw
 * @since 2022-07-21
 */
public interface IBEquipmentElevatorDataAtService extends BaseService<BEquipmentElevatorDataAt> {

    Result saveData(List<Map<String, Object>> list);

    BasePage<Map<String,Object>> selectPage(EquipmentElevatorDataAtBO elevatorDataAt);

}

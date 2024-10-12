package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.BO.EquipmentElevatorDataAtBO;
import com.kakarote.build.equipment.entity.PO.EquipmentElevatorDataAt;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 升降电梯设备实时数据表 服务类
 * </p>
 *
 * @author shz
 * @since 2022-03-22
 */
public interface IEquipmentElevatorDataAtService extends BaseService<EquipmentElevatorDataAt> {

    Result saveData(List<Map<String, Object>> list);

    BasePage<Map<String,Object>> selectPage(EquipmentElevatorDataAtBO elevatorDataAt);
}

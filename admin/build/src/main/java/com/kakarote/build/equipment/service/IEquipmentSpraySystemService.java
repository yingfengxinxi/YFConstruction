package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.BO.EquipmentSpraySystemBO;
import com.kakarote.build.equipment.entity.PO.EquipmentSpraySystem;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自动喷淋系统 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-06-01
 */
public interface IEquipmentSpraySystemService extends BaseService<EquipmentSpraySystem> {

    BasePage<EquipmentSpraySystem> selectByPage(EquipmentSpraySystemBO spraySystemBO);

    BasePage<EquipmentSpraySystem> selectByPageOperation(EquipmentSpraySystemBO spraySystemBO);

    Result spraySwitch(Map<String, Object> map);

    Result sprayUnifiedSwitch(String isSwitch);

    List<Map<String, Object>> selectPieData();

    List<Map<String, Object>> selectBarData();

    List<Map<String, Object>> selectKqsdfxData();

    List<Map<String, Object>> selectPlscfxData();

    List<Map<String,Object>> selectNoPage(Map<String,Object> params);

}

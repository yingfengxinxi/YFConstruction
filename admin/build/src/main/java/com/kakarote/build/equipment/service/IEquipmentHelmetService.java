package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.BO.EquipmentHelmetBO;
import com.kakarote.build.equipment.entity.PO.EquipmentHelmet;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 智能安全帽 服务类
 * </p>
 *
 * @author lzy
 * @since 2022-02-14
 */
public interface IEquipmentHelmetService extends BaseService<EquipmentHelmet> {

    BasePage<EquipmentHelmet> selectByPage(EquipmentHelmetBO equipmentHelmetBO);

    BasePage<EquipmentHelmet> selectProList(EquipmentHelmetBO equipmentHelmetBO);

    List<Map<String, Object>> selectLatestLocationList();

    List<Map<String, Object>> selectPeopleTrackList(Map<String, Object> params);

    String getSvg(Integer id);
}

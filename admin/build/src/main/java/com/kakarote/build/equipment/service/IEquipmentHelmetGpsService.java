package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.BO.EquipmentHelmetBO;
import com.kakarote.build.equipment.entity.BO.EquipmentHelmetGpsBO;
import com.kakarote.build.equipment.entity.PO.EquipmentHelmetGps;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 智慧安全帽-定位数据 服务类
 * </p>
 *
 * @author lzy
 * @since 2022-02-14
 */
public interface IEquipmentHelmetGpsService extends BaseService<EquipmentHelmetGps> {

    BasePage<EquipmentHelmetGps> selectByPage(EquipmentHelmetGpsBO equipmentHelmetGpsBO);

    List selectActionTrack(EquipmentHelmetGpsBO equipmentHelmetGpsBO);

    String deletePid(Integer helmetId);
}

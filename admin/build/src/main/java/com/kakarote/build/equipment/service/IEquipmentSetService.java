package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.BO.EquipmentSetBO;
import com.kakarote.build.equipment.entity.PO.EquipmentSet;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
public interface IEquipmentSetService extends BaseService<EquipmentSet> {

    BasePage<EquipmentSet> selectByPage(EquipmentSetBO setBO);

    BasePage<EquipmentSet> selectListByAssetId(EquipmentSetBO setBO);
}

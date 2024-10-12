package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.BO.EquipmentRebarSpotCheckBO;
import com.kakarote.build.equipment.entity.PO.EquipmentRebarSpotCheck;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 钢筋智能点检功能开发 服务类
 * </p>
 *
 * @author lzy
 * @since 2022-02-14
 */
public interface IEquipmentRebarSpotCheckService extends BaseService<EquipmentRebarSpotCheck> {

    BasePage<EquipmentRebarSpotCheck> selectByPage(EquipmentRebarSpotCheckBO equipmentRebarSpotCheckBO);

    BasePage<EquipmentRebarSpotCheck> selectProList(EquipmentRebarSpotCheckBO equipmentRebarSpotCheckBO);
}

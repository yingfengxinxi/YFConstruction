package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.BO.EquipmentPlanAssetBO;
import com.kakarote.build.equipment.entity.PO.EquipmentPlan;
import com.kakarote.build.equipment.entity.PO.EquipmentPlanAsset;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 计划维护设备信息 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
public interface IEquipmentPlanAssetService extends BaseService<EquipmentPlanAsset> {

    void updatePlanAsset(EquipmentPlan plan);

    void removeByPlanId(Integer planId);

    BasePage<EquipmentPlanAsset> selectByPage(EquipmentPlanAssetBO planAssetBO);
}

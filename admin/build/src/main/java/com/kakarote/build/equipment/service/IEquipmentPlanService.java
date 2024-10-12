package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.BO.EquipmentPlanBO;
import com.kakarote.build.equipment.entity.PO.EquipmentPlan;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 设备监测-预防性维护计划-维护计划设置 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
public interface IEquipmentPlanService extends BaseService<EquipmentPlan> {

    BasePage<EquipmentPlan> selectByPage(EquipmentPlanBO equipmentPlanBO);
}

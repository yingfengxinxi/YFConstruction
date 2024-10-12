package com.kakarote.build.equipment.service.impl;

import com.kakarote.build.equipment.entity.BO.EquipmentPlanBO;
import com.kakarote.build.equipment.entity.PO.EquipmentPlan;
import com.kakarote.build.equipment.mapper.EquipmentPlanMapper;
import com.kakarote.build.equipment.service.IEquipmentPlanService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备监测-预防性维护计划-维护计划设置 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
@Service
public class EquipmentPlanServiceImpl extends BaseServiceImpl<EquipmentPlanMapper, EquipmentPlan> implements IEquipmentPlanService {

    @Override
    public BasePage<EquipmentPlan> selectByPage(EquipmentPlanBO equipmentPlanBO) {
        equipmentPlanBO.setOrdersBd("a.enable",false);
        equipmentPlanBO.setOrdersBd("a.update_time",false);

        BasePage<EquipmentPlan> list =getBaseMapper().selectByPage(equipmentPlanBO.parse(), equipmentPlanBO);
        return list;
    }
}

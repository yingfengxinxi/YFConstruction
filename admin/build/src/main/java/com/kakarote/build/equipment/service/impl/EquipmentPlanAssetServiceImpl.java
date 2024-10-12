package com.kakarote.build.equipment.service.impl;

import com.kakarote.build.equipment.entity.BO.EquipmentPlanAssetBO;
import com.kakarote.build.equipment.entity.PO.EquipmentPlan;
import com.kakarote.build.equipment.entity.PO.EquipmentPlanAsset;
import com.kakarote.build.equipment.mapper.EquipmentPlanAssetMapper;
import com.kakarote.build.equipment.service.IEquipmentPlanAssetService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 计划维护设备信息 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
@Service
public class EquipmentPlanAssetServiceImpl extends BaseServiceImpl<EquipmentPlanAssetMapper, EquipmentPlanAsset> implements IEquipmentPlanAssetService {

    @Override
    public void updatePlanAsset(EquipmentPlan plan) {
        // 删除
        this.removeByPlanId(plan.getPlanId());
        //新增
        List<EquipmentPlanAsset> list = plan.getAssetList();
        for (EquipmentPlanAsset asset : list) {
            asset.setPlanId(plan.getPlanId());
        }
        this.saveBatch(list);
    }

    @Override
    public void removeByPlanId(Integer planId) {
        Map<String, Object> map = new HashMap();
        map.put("plan_id", planId);
        this.removeByMap(map);
    }

    @Override
    public BasePage<EquipmentPlanAsset> selectByPage(EquipmentPlanAssetBO planAssetBO) {
        BasePage<EquipmentPlanAsset> list = getBaseMapper().selectByPage(planAssetBO.parse(), planAssetBO);
        return list;
    }
}

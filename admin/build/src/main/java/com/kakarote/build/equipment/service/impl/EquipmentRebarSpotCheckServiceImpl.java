package com.kakarote.build.equipment.service.impl;

import com.kakarote.build.equipment.entity.BO.EquipmentRebarSpotCheckBO;
import com.kakarote.build.equipment.entity.PO.EquipmentRebarSpotCheck;
import com.kakarote.build.equipment.mapper.EquipmentRebarSpotCheckMapper;
import com.kakarote.build.equipment.service.IEquipmentRebarSpotCheckService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 钢筋智能点检功能开发 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2022-02-14
 */
@Service
public class EquipmentRebarSpotCheckServiceImpl extends BaseServiceImpl<EquipmentRebarSpotCheckMapper, EquipmentRebarSpotCheck> implements IEquipmentRebarSpotCheckService {

    @Override
    public BasePage<EquipmentRebarSpotCheck> selectByPage(EquipmentRebarSpotCheckBO equipmentRebarSpotCheckBO) {
        equipmentRebarSpotCheckBO.setOrdersBd("a.spot_check_date",false);
        return getBaseMapper().selectByPage(equipmentRebarSpotCheckBO.parse(), equipmentRebarSpotCheckBO);
    }

    @Override
    public BasePage<EquipmentRebarSpotCheck> selectProList(EquipmentRebarSpotCheckBO equipmentRebarSpotCheckBO) {
        equipmentRebarSpotCheckBO.setOrdersBd("a.spot_check_date",false);
        return getBaseMapper().selectProList(equipmentRebarSpotCheckBO.parse(), equipmentRebarSpotCheckBO);
    }
}

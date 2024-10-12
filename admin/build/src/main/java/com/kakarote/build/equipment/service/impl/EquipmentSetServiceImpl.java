package com.kakarote.build.equipment.service.impl;

import com.kakarote.build.equipment.entity.BO.EquipmentSetBO;
import com.kakarote.build.equipment.entity.PO.EquipmentSet;
import com.kakarote.build.equipment.mapper.EquipmentSetMapper;
import com.kakarote.build.equipment.service.IEquipmentSetService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
@Service
public class EquipmentSetServiceImpl extends BaseServiceImpl<EquipmentSetMapper, EquipmentSet> implements IEquipmentSetService {

    @Override
    public BasePage<EquipmentSet> selectByPage(EquipmentSetBO setBO) {
        setBO.setOrdersBd("a.set_code",true);
        BasePage<EquipmentSet> list = getBaseMapper().selectByPage(setBO.parse(), setBO);
        return list;
    }

    @Override
    public BasePage<EquipmentSet> selectListByAssetId(EquipmentSetBO setBO) {
        BasePage<EquipmentSet> list = getBaseMapper().selectListByAssetId(setBO.parse(), setBO);
        return null;
    }
}

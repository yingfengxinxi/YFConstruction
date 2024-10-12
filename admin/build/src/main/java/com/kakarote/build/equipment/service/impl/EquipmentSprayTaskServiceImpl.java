package com.kakarote.build.equipment.service.impl;

import com.kakarote.build.equipment.entity.BO.EquipmentSprayTaskBO;
import com.kakarote.build.equipment.entity.PO.EquipmentSprayTask;
import com.kakarote.build.equipment.mapper.EquipmentSprayTaskMapper;
import com.kakarote.build.equipment.service.IEquipmentSprayTaskService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 喷淋定期任务表 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-08-05
 */
@Service
public class EquipmentSprayTaskServiceImpl extends BaseServiceImpl<EquipmentSprayTaskMapper, EquipmentSprayTask> implements IEquipmentSprayTaskService {

    @Override
    public BasePage<EquipmentSprayTask> selectByPage(EquipmentSprayTaskBO sprayTaskBO) {
        BasePage<EquipmentSprayTask> list = getBaseMapper().selectByPage(sprayTaskBO.parse(), sprayTaskBO);
        return list;
    }
}

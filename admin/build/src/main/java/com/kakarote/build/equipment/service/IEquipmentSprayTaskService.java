package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.BO.EquipmentSprayTaskBO;
import com.kakarote.build.equipment.entity.PO.EquipmentSprayTask;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 喷淋定期任务表 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-08-05
 */
public interface IEquipmentSprayTaskService extends BaseService<EquipmentSprayTask> {

    BasePage<EquipmentSprayTask> selectByPage(EquipmentSprayTaskBO sprayTaskBO);
}

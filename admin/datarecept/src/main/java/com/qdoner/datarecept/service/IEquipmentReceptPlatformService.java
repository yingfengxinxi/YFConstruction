package com.qdoner.datarecept.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import com.qdoner.datarecept.entity.BO.EquipmentReceptPlatformBO;
import com.qdoner.datarecept.entity.PO.EquipmentReceptPlatform;

/**
 * <p>
 * 设备数据对接平台 服务类
 * </p>
 *
 * @author wudw
 * @since 2021-11-11
 */
public interface IEquipmentReceptPlatformService extends BaseService<EquipmentReceptPlatform> {
    BasePage<EquipmentReceptPlatform> selectByPage(EquipmentReceptPlatformBO inspectionPlanBO);
}

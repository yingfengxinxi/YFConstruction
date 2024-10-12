package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.BO.BEquipmentProBO;
import com.kakarote.build.equipment.entity.PO.BEquipmentPro;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 基坑/高支模工程管理  服务类
 * </p>
 *
 * @author wudw
 * @since 2021-12-23
 */
public interface IBEquipmentProService extends BaseService<BEquipmentPro> {
    BasePage<BEquipmentPro> selectByPage(BEquipmentProBO bEquipmentProBO);

}

package com.kakarote.build.equipment.service.impl;

import com.kakarote.build.equipment.entity.BO.BEquipmentProBO;
import com.kakarote.build.equipment.entity.PO.BEquipmentPro;
import com.kakarote.build.equipment.mapper.BEquipmentProMapper;
import com.kakarote.build.equipment.service.IBEquipmentProService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 基坑/高支模工程管理  服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-12-23
 */
@Service
public class BEquipmentProServiceImpl extends BaseServiceImpl<BEquipmentProMapper, BEquipmentPro> implements IBEquipmentProService {

    @Override
    public BasePage<BEquipmentPro> selectByPage(BEquipmentProBO bEquipmentProBO) {
        BasePage<BEquipmentPro> basePage = getBaseMapper().selectByPage(bEquipmentProBO.parse(), bEquipmentProBO);
        return basePage;
    }

}

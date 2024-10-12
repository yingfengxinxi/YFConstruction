package com.qdoner.datarecept.service.impl;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.qdoner.datarecept.entity.BO.EquipmentReceptPlatformBO;
import com.qdoner.datarecept.entity.PO.EquipmentReceptPlatform;
import com.qdoner.datarecept.mapper.EquipmentReceptPlatformMapper;
import com.qdoner.datarecept.service.IEquipmentReceptPlatformService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备数据对接平台 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-11-11
 */
@Service
public class EquipmentReceptPlatformServiceImpl extends BaseServiceImpl<EquipmentReceptPlatformMapper, EquipmentReceptPlatform> implements IEquipmentReceptPlatformService {

    @Override
    public BasePage<EquipmentReceptPlatform> selectByPage(EquipmentReceptPlatformBO platformBO) {
        BasePage<EquipmentReceptPlatform> list = this.getBaseMapper().selectByPage(platformBO.parse(), platformBO);
        return list;
    }
}

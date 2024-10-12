package com.qdoner.datarecept.equipment.service.impl;

import com.qdoner.datarecept.equipment.entity.PO.EquipmentMonitorPoint;
import com.qdoner.datarecept.equipment.mapper.EquipmentMonitorPointMapper;
import com.qdoner.datarecept.equipment.service.IEquipmentMonitorPointService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备监测-监测点设置 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-05-18
 */
@Service
public class EquipmentMonitorPointServiceImpl extends BaseServiceImpl<EquipmentMonitorPointMapper, EquipmentMonitorPoint> implements IEquipmentMonitorPointService {


    @Override
    public EquipmentMonitorPoint getByIdInApi(Integer id) {
        return getBaseMapper().getByIdInApi(id);
    }

}

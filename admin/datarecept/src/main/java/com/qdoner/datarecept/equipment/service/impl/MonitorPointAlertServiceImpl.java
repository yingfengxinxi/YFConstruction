package com.qdoner.datarecept.equipment.service.impl;

import com.qdoner.datarecept.equipment.entity.PO.MonitorPointAlert;
import com.qdoner.datarecept.equipment.mapper.MonitorPointAlertMapper;
import com.qdoner.datarecept.equipment.service.IMonitorPointAlertService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * <p>
 * 监测点警报 服务实现类
 * </p>
 *
 * @author shao
 * @since 2021-05-20
 */
@Service
public class MonitorPointAlertServiceImpl extends BaseServiceImpl<MonitorPointAlertMapper, MonitorPointAlert> implements IMonitorPointAlertService {

    public int saveAlertByList(List<MonitorPointAlert> list){
        return  getBaseMapper().saveAlertByList(list);
    }

    public MonitorPointAlert copyPastry(MonitorPointAlert copyObj){
        MonitorPointAlert mMonitorPointAlert = new MonitorPointAlert();
        mMonitorPointAlert.setProjectId(copyObj.getProjectId());
        mMonitorPointAlert.setTenantId(copyObj.getTenantId());
        mMonitorPointAlert.setEquipmentId(copyObj.getEquipmentId());
        mMonitorPointAlert.setMonitorPointId(copyObj.getMonitorPointId());
        mMonitorPointAlert.setCreateTime(new Date());
        mMonitorPointAlert.setHandleState("0");
        return mMonitorPointAlert;
    }
}

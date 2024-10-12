package com.kakarote.build.equipment.job;


import com.kakarote.build.equipment.common.EqWarnManager;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorPoint;
import com.kakarote.build.equipment.mapper.EquipmentMonitorPointMapper;
import com.kakarote.build.equipment.service.impl.EquipmentMonitorPointServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
* @Description:检测点定时
* @author shz
* @date 2021-5-20 10:12
*/
@Component
@Slf4j
public class MonitorPointAlertJob {

    @Autowired
    private EquipmentMonitorPointServiceImpl monitorPointService;

    @Autowired
    private EqWarnManager eqWarnManager;
    //@Scheduled(cron = "0 */1 * * * ?")
//    @Scheduled(cron = "*/15 * * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void monitoringData(){
        log.info("==模拟监控数据报警");
        EquipmentMonitorPointMapper equipmentMonitorPointMapper = monitorPointService.getBaseMapper();
        List<EquipmentMonitorPoint> list = equipmentMonitorPointMapper.selectAll();
        //获取监控点数据。此处暂时为模拟随机数
        Random r = new Random();
        int i = r.nextInt(1000);
        BigDecimal data = new BigDecimal(i);
        for(EquipmentMonitorPoint equipmentMonitorPoint:list){
            eqWarnManager.warn(equipmentMonitorPoint,data);
        }
    }
}

package com.kakarote.build.equipment.job;

import com.kakarote.build.equipment.service.impl.MonitorPointAlertServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class AppletMonitorPointAlertJob {

    @Autowired
    private MonitorPointAlertServiceImpl monitorPointAlertService;


    //@Scheduled(cron = "0 */1 * * * ?")
//    @PostConstruct
//    @Scheduled(cron = "0 01 */1 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void monitoringData(){
        log.info("==小程序扬尘超标查询");
        monitorPointAlertService.appletExcessiveDust();
    }

}

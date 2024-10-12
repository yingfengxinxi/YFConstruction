package com.kakarote.build.equipment.common;


import com.kakarote.build.equipment.service.impl.MonitorPointAlertServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
* @Description:小程序报警逻辑
* @author shz
* @date 2022-3-7 16:29
*/
@Component
@Slf4j
public class AppletMonitorPointAlertManager {

    @Autowired
    private MonitorPointAlertServiceImpl monitorPointAlertService;



}

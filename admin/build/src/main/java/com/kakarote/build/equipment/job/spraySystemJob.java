package com.kakarote.build.equipment.job;

import cn.hutool.core.date.DateUtil;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorPoint;
import com.kakarote.build.equipment.entity.PO.EquipmentPlan;
import com.kakarote.build.equipment.entity.PO.EquipmentSpraySystem;
import com.kakarote.build.equipment.service.IEquipmentMonitorPointService;
import com.kakarote.build.equipment.service.IEquipmentSpraySystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 自动喷淋系统定时任务
 */
@Component
@Slf4j
public class spraySystemJob {

    @Autowired
    private IEquipmentMonitorPointService monitorPointService;

    @Autowired
    private IEquipmentSpraySystemService spraySystemService;

    //@Scheduled(cron = "${spray.cron}")
    @Transactional(rollbackFor = Exception.class)
    public void spraySystemMain() {
        String info = "后台定时任务：自动喷淋系统 ==> 循环一次";
        log.info(info);

        // 查询所有监测项类型为环境监测，并且关联喷淋系统的监测点
        List<EquipmentMonitorPoint> pointList = monitorPointService.lambdaQuery().eq(EquipmentMonitorPoint::getMonitorType, "9")
                .eq(EquipmentMonitorPoint::getIsSpraySystem, "1").ne(EquipmentMonitorPoint::getTenantId, "0")
                .ne(EquipmentMonitorPoint::getProjectId, "0").list();

        // 遍历
        List<EquipmentSpraySystem> spraySystemList = new ArrayList<>();

        for (int i = 0; i < pointList.size(); i++) {
            EquipmentSpraySystem spraySystem = new EquipmentSpraySystem();
            // TODO暂时使用随机数代替实时数据接口
            Random random = new Random();
            DecimalFormat df = new DecimalFormat("0.000");
            double r = random.nextDouble() * 150;
            String str = df.format(r);

            EquipmentMonitorPoint monitorPoint = pointList.get(i);
            BigDecimal r2 = new BigDecimal(str);

            if (r2.compareTo(monitorPoint.getStartSprayValue()) > -1) {
                // 如果实时值大于等于启动喷淋值
                spraySystem.setMonitorPointId(monitorPoint.getId());
                spraySystem.setEquipmentId(monitorPoint.getEquipmentId());
                spraySystem.setOperationType("1");
                spraySystem.setOperationReason("PM2.5值达到" + r2 + monitorPoint.getUnit());
                spraySystem.setOperationTime(DateUtil.parseDateTime(DateUtil.now()));
                spraySystem.setTenantId(monitorPoint.getTenantId());
                spraySystem.setProjectId(monitorPoint.getProjectId());
                spraySystemList.add(spraySystem);

            } else if (r2.compareTo(monitorPoint.getCloseSprayValue()) < 1) {
                // 如果实时值小于等于关闭喷淋值
                spraySystem.setMonitorPointId(monitorPoint.getId());
                spraySystem.setEquipmentId(monitorPoint.getEquipmentId());
                spraySystem.setOperationType("0");
                spraySystem.setOperationReason("PM2.5值降至" + r2 + monitorPoint.getUnit());
                spraySystem.setOperationTime(DateUtil.parseDateTime(DateUtil.now()));
                spraySystem.setTenantId(monitorPoint.getTenantId());
                spraySystem.setProjectId(monitorPoint.getProjectId());
                spraySystemList.add(spraySystem);
            }
        }

        // 保存喷淋设备启停记录
        if (spraySystemList.size() > 0) {
            spraySystemService.saveBatch(spraySystemList);
        }
    }

}

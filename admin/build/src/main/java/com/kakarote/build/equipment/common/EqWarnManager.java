package com.kakarote.build.equipment.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.api.service.SendProjectMsgService;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorPoint;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorWarn;
import com.kakarote.build.equipment.entity.PO.MonitorPointAlert;
import com.kakarote.build.equipment.service.IEquipmentMonitorWarnService;
import com.kakarote.build.equipment.service.impl.MonitorPointAlertServiceImpl;
import com.kakarote.build.utils.DateUtil;
import com.kakarote.core.message.MessageEntity;
import com.kakarote.core.message.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 设备警报解析控制器
 * 2021-10-11
 */
@Component
@Slf4j
public class EqWarnManager {
    @Autowired
    private MonitorPointAlertServiceImpl monitorPointAlertService;
    @Autowired
    private IEquipmentMonitorWarnService warnService;

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private SendProjectMsgService sendProjectMsgService;

    /**
     * 报警、预警
     *
     * @param equipmentMonitorPoint
     * @param data
     */
    public void warn(EquipmentMonitorPoint equipmentMonitorPoint, BigDecimal data) {
        log.info("==监测数据判断 检测值："+ data.toString());
        //关联通用配置
        EquipmentMonitorWarn warn = warnService.getByTypeId(equipmentMonitorPoint.getMonitorSonType()
                ,String.valueOf(equipmentMonitorPoint.getTenantId())
                ,String.valueOf(equipmentMonitorPoint.getProjectId()));
        if (warn != null) {
            //预警
            if (equipmentMonitorPoint.getWarnLower() == null) {
                equipmentMonitorPoint.setWarnLower(warn.getWarnLower());
            }
            if (equipmentMonitorPoint.getWarnUpper() == null) {
                equipmentMonitorPoint.setWarnUpper(warn.getWarnUpper());
            }
            //报警
            if (equipmentMonitorPoint.getAlarmLower() == null) {
                equipmentMonitorPoint.setAlarmLower(warn.getAlarmLower());
            }
            if (equipmentMonitorPoint.getAlarmUpper() == null) {
                equipmentMonitorPoint.setAlarmUpper(warn.getAlarmUpper());
            }
            //控制
            if (equipmentMonitorPoint.getContrLower() == null) {
                equipmentMonitorPoint.setContrLower(warn.getContrLower());
            }
            if (equipmentMonitorPoint.getContrUpper() == null) {
                equipmentMonitorPoint.setContrUpper(warn.getContrUpper());
            }
        }

        /*
         * 控制标记
         */
        boolean isContr = false;

        /*
         * 报警标记
         */
        boolean isAlert = false;
        /*
         * 预警标记
         */
        boolean isWarn = false;

        String content = "";

        if (equipmentMonitorPoint.getContrLower() != null) {
            if (data.compareTo(equipmentMonitorPoint.getContrLower()) == -1) {
                content =  "监测值低于控制下限：" + equipmentMonitorPoint.getContrLower() + equipmentMonitorPoint.getUnit();
                isContr = true;
            }
        }
        if (equipmentMonitorPoint.getContrUpper() != null) {
            if (data.compareTo(equipmentMonitorPoint.getContrUpper()) == 1) {
                content =  "监测值超过控制上限：" + equipmentMonitorPoint.getContrUpper() + equipmentMonitorPoint.getUnit();
                isContr = true;
            }
        }
        if (!isContr) {
            if (equipmentMonitorPoint.getAlarmLower() != null) {
                if (data.compareTo(equipmentMonitorPoint.getAlarmLower()) == -1) {
                    content =  "监测值低于报警下限：" + equipmentMonitorPoint.getAlarmLower() + equipmentMonitorPoint.getUnit();
                    isAlert = true;
                }
            }
            if (equipmentMonitorPoint.getAlarmUpper() != null) {
                if (data.compareTo(equipmentMonitorPoint.getAlarmUpper()) == 1) {
                    content =  "监测值超过报警上限：" + equipmentMonitorPoint.getAlarmUpper() + equipmentMonitorPoint.getUnit();
                    isAlert = true;
                }
            }
            if (!isAlert) {
                if (equipmentMonitorPoint.getWarnLower() != null) {
                    if (data.compareTo(equipmentMonitorPoint.getWarnLower()) == -1) {
                        content =  "监测值低于预警下限：" + equipmentMonitorPoint.getWarnLower() + equipmentMonitorPoint.getUnit();
                        isWarn = true;
                    }
                }
                if (equipmentMonitorPoint.getWarnUpper() != null) {
                    if (data.compareTo(equipmentMonitorPoint.getWarnUpper()) == 1) {
                        content =  "监测值超过预警上限：" + equipmentMonitorPoint.getWarnUpper() + equipmentMonitorPoint.getUnit();
                        isWarn = true;
                    }
                }
            }
        }

        //报警类型
        String alertType = "0";
        if (isAlert) {
            alertType = "1";
        }
        if (isContr) {
            alertType = "2";
        }
        //报警标识
        String contrFlag = "_Contr";
        String alertFlag = "_Alert";
        String warnFlag = "_Warn";
        if (equipmentMonitorPoint.getRangeTime() != null && equipmentMonitorPoint.getRangeTime() != 0) {
            if (isContr) {//控制
                handleRange(equipmentMonitorPoint,data,content,alertType,contrFlag);
            } else {
                redisTemplate.delete(equipmentMonitorPoint.getId() + contrFlag);
            }

            if (isAlert) {//需要报警
                handleRange(equipmentMonitorPoint,data,content,alertType,alertFlag);
            } else {
                redisTemplate.delete(equipmentMonitorPoint.getId() + alertFlag);
            }
            if (isWarn) {//需要预警
                handleRange(equipmentMonitorPoint,data,content,alertType,warnFlag);
            } else {
                redisTemplate.delete(equipmentMonitorPoint.getId() + warnFlag);
            }
        } else {
            if (isWarn || isAlert || isContr) {
                startWarn(equipmentMonitorPoint, data, content, alertType);
            }else {

            }
        }
        if(isWarn || isAlert || isContr){

        }else {
            eventManage(equipmentMonitorPoint,data);
        }
    }

    private void handleRange(EquipmentMonitorPoint equipmentMonitorPoint, BigDecimal data,String content,String alertType
    ,String flag){

        boolean hasWarn = redisTemplate.hasKey(String.valueOf(equipmentMonitorPoint.getId() + flag));
        if (hasWarn) {
            Date date = (Date) redisTemplate.boundValueOps(String.valueOf(equipmentMonitorPoint.getId() + flag)).get();
            int ms = (int) (new Date().getTime() - date.getTime()) / 1000;
            if (ms > equipmentMonitorPoint.getRangeTime()) {
                startWarn(equipmentMonitorPoint, data, "连续" + equipmentMonitorPoint.getRangeTime() + "秒" + content,
                        alertType);
                redisTemplate.delete(equipmentMonitorPoint.getId() + flag);
            }
        } else {
            redisTemplate.boundValueOps(equipmentMonitorPoint.getId() + flag).set(new Date());
        }
    }

    /**
     * 封装：预报警操作
     */
    private void startWarn(EquipmentMonitorPoint equipmentMonitorPoint, BigDecimal monitorData,
                           String content, String alertType) {
        log.info("==监测数据判断为报警或预警数据");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("monitor_point_id",equipmentMonitorPoint.getId());
        queryWrapper.isNull("handle_time");
        List<MonitorPointAlert> list = monitorPointAlertService.list(queryWrapper);
        if(list.size()==0){
            log.info("==没有未处理数据进行报警");
            MonitorPointAlert monitorPointAlert = new MonitorPointAlert();
            monitorPointAlert.setAlertType(alertType);
            monitorPointAlert.setContent(content);
            monitorPointAlert.setEquipmentId(equipmentMonitorPoint.getEquipmentId());
            monitorPointAlert.setMonitorPointId(equipmentMonitorPoint.getId());
            monitorPointAlert.setProjectId(equipmentMonitorPoint.getProjectId());
            monitorPointAlert.setMonitorData(monitorData.toString());
            monitorPointAlert.setTenantId(equipmentMonitorPoint.getTenantId());
            monitorPointAlert.setHandleState("0");
            monitorPointAlert.setMonitorAlertType("1"); // 数值报警
            monitorPointAlertService.save(monitorPointAlert);
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setTitle(equipmentMonitorPoint.getPointName() + content);
            messageEntity.setData(String.valueOf(monitorPointAlert.getId()));
            messageEntity.setCode(MessageType.ALARM_EQU_ENVRMT);
            messageEntity.setProjectId(equipmentMonitorPoint.getProjectId().longValue());//获取到登录用户时可不填
            messageEntity.setTenantId(equipmentMonitorPoint.getTenantId());//获取到登录用户时可不填
            sendProjectMsgService.send(messageEntity, true);
        }else {
            log.info("==已有未处理数据不报警");
        }

    }

    private void eventManage(EquipmentMonitorPoint equipmentMonitorPoint,BigDecimal data){
        log.info("==报警数据办理");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("monitor_point_id",equipmentMonitorPoint.getId());
        queryWrapper.isNull("handle_time");
        List<MonitorPointAlert> list = monitorPointAlertService.list(queryWrapper);
        for(MonitorPointAlert monitorPointAlert:list){
            DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            monitorPointAlert.setHandleTime(dft.format(new Date()));
            monitorPointAlert.setRecoveryData(String.valueOf(data));
            monitorPointAlert.setHandleConclu("监测值已恢复");
            monitorPointAlert.setHandleState("1");
            monitorPointAlert.setHandleMeasures("报警解除");
            monitorPointAlertService.updateById(monitorPointAlert);
        }
    }

}
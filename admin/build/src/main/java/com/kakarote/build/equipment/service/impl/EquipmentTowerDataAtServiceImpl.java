package com.kakarote.build.equipment.service.impl;

import cn.hutool.core.date.DateException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.build.api.common.EnvMonitorCodeEnum;
import com.kakarote.build.equipment.entity.BO.EquipmentTowerDataAtBO;
import com.kakarote.build.equipment.entity.PO.EquipmentTowerDataAt;
import com.kakarote.build.equipment.entity.PO.MonitorPointAlert;
import com.kakarote.build.equipment.mapper.EquipmentTowerDataAtMapper;
import com.kakarote.build.equipment.service.IEquipmentTowerDataAtService;
import com.kakarote.build.equipment.service.IMonitorPointAlertService;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.build.utils.ApiUtils;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 塔机设备实时数据表 服务实现类
 * </p>
 *
 * @author shz
 * @since 2022-03-22
 */
@Service
public class EquipmentTowerDataAtServiceImpl extends BaseServiceImpl<EquipmentTowerDataAtMapper, EquipmentTowerDataAt> implements IEquipmentTowerDataAtService {

    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private IMonitorPointAlertService monitorPointAlertService;
    @Override
    public Result saveData(List<Map<String, Object>> list) {
        try {
            List<EquipmentTowerDataAt> dataList = new ArrayList<>();
            List<MonitorPointAlert> alertList = new ArrayList<>();
            for(Map data:list){
                EquipmentTowerDataAt equipmentTowerDataAt = new EquipmentTowerDataAt();
                if(list != null && list.size()>0){
                    if (data.get("projectId") == null || "".equals(data.get("projectId"))) {
                        throw new CrmException(500, "项目id不能为空");
                    }

                    // 判断项目id是否存在

                    Integer projectId = (Integer) data.get("projectId");

                    LambdaQueryWrapper<ProjectInfo> projectQueryWrapper = new LambdaQueryWrapper<>();
                    projectQueryWrapper.eq(ProjectInfo::getId, projectId);
                    projectQueryWrapper.ge(ProjectInfo::getTenantId, "0");
                    List<ProjectInfo> proList = projectInfoService.list(projectQueryWrapper);
                    if (proList == null || proList.size() <= 0) {
                        throw new CrmException(500, "项目id：" + projectId + "不存在,请项目施工单位先在住建委云平台申请后再进行数据推送");
                    }

                    ProjectInfo projectInfo = proList.get(0);
                    equipmentTowerDataAt.setTenantId(projectInfo.getTenantId());
                    equipmentTowerDataAt.setProjectId(projectId);
                    if (data.get("deviceSn") == null || "".equals(data.get("deviceSn"))) {
                        throw new CrmException(500, "设备编号不能为空");
                    }
                    equipmentTowerDataAt.setEquipmentId(Integer.valueOf(data.get("deviceSn") + ""));
                    MonitorPointAlert monitorPointAlert = new MonitorPointAlert();
                    monitorPointAlert.setEquipmentId(equipmentTowerDataAt.getEquipmentId());
                    monitorPointAlert.setProjectId(equipmentTowerDataAt.getProjectId());
                    monitorPointAlert.setTenantId(equipmentTowerDataAt.getTenantId());
                    if (data.get("deviceTime") == null || "".equals(data.get("deviceTime"))) {
                        throw new CrmException(500, "设备数据时间不能为空");
                    }
                    String regdate=(String)data.get("deviceTime");
                    DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date monitorTime = dft.parse(regdate);
                    equipmentTowerDataAt.setMonitorTime(monitorTime);
                    if (data.get("alarm0") == null || "".equals(data.get("alarm0"))) {
                        throw new CrmException(500, "系统预警状态编码不能为空");
                    }
                    Integer alarm0 = Integer.valueOf(String.valueOf(data.get("alarm0")));
                    List<Integer> alarm0List = ApiUtils.orInverseRun(alarm0);
                    if (alarm0List.indexOf(1) != -1) {
                        equipmentTowerDataAt.setAntiCollisionLeftTurnAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("防碰撞左转预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(2) != -1) {
                        equipmentTowerDataAt.setAntiCollisionRightTurnAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("防碰撞右转预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(4) != -1) {
                        equipmentTowerDataAt.setCollisionTrolleyAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("防碰撞小车出预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(8) != -1) {
                        equipmentTowerDataAt.setCollisionTrolleyEntryAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("防碰撞小车入预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(16) != -1) {
                        equipmentTowerDataAt.setLeftTurnEnvironmentalAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("左转环境预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(32) != -1) {
                        equipmentTowerDataAt.setRightTurnEnvironmentalAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("右转环境预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(64) != -1) {
                        equipmentTowerDataAt.setTrolleyExitEnvironmentAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("小车出环境预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(128) != -1) {
                        equipmentTowerDataAt.setTrolleyEntryEnvironmentAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("小车进环境预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(256) != -1) {
                        equipmentTowerDataAt.setMaximumAmplitudeLimitAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("最大幅度限位预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(512) != -1) {
                        equipmentTowerDataAt.setMinimumAmplitudeLimitAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("最小幅度限位预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(1024) != -1) {
                        equipmentTowerDataAt.setAltitudeUpperLimitAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("高度上限位预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(2048) != -1) {
                        equipmentTowerDataAt.setAltitudeLowerLimitAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("高度下限位预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(4096) != -1) {
                        equipmentTowerDataAt.setSlewingLeftLimitAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("回转左限位预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(8192) != -1) {
                        equipmentTowerDataAt.setSlewingRightLimitAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("回转右限位预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(16777216) != -1) {
                        equipmentTowerDataAt.setMomentAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("percent")+"");
                        monitorPointAlertc.setContent("力矩预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(33554432) != -1) {
                        equipmentTowerDataAt.setWeightAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("load")+"");
                        monitorPointAlertc.setContent("吊重预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(536870912) != -1) {
                        equipmentTowerDataAt.setWindSpeedAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("windSpeed")+"");
                        monitorPointAlertc.setContent("风速预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    Integer alarm1 = Integer.valueOf(String.valueOf(data.get("alarm1")));
                    List<Integer> alarm1List = ApiUtils.orInverseRun(alarm1);
                    if (alarm1List.indexOf(1) != -1) {
                        equipmentTowerDataAt.setAntiCollisionLeftTurnAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("防碰撞左转报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(2) != -1 ) {
                        equipmentTowerDataAt.setAntiCollisionRightTurnAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("防碰撞右转报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(4) != -1) {
                        equipmentTowerDataAt.setCollisionTrolleyAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("防碰撞小车出报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(8) != -1) {
                        equipmentTowerDataAt.setCollisionTrolleyEntryAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("防碰撞小车入报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(15) != -1) {
                        equipmentTowerDataAt.setCollisionAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("碰撞报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(16) != -1) {
                        equipmentTowerDataAt.setLeftTurnEnvironmentalAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("左转环境报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(32) != -1) {
                        equipmentTowerDataAt.setRightTurnEnvironmentalAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("右转环境报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(64) != -1) {
                        equipmentTowerDataAt.setTrolleyExitEnvironmentAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("小车出环境报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(128) != -1) {
                        equipmentTowerDataAt.setTrolleyEntryEnvironmentAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("小车进环境报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(134217728) != -1 ) {
                        equipmentTowerDataAt.setForbidEntryAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("禁入区报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(201326832) != -1 ) {
                        equipmentTowerDataAt.setRestrictedAreaAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("限行区报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(256) != -1) {
                        equipmentTowerDataAt.setMaximumAmplitudeLimitAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("radius")+"");
                        monitorPointAlertc.setContent("最大幅度限位报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(512) != -1) {
                        equipmentTowerDataAt.setMinimumAmplitudeLimitAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("radius")+"");
                        monitorPointAlertc.setContent("最小幅度限位报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(1024) != -1) {
                        equipmentTowerDataAt.setAltitudeUpperLimitAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("高度上限位报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(2048) != -1) {
                        equipmentTowerDataAt.setAltitudeLowerLimitAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("高度下限位报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(4096) != -1) {
                        equipmentTowerDataAt.setSlewingLeftLimitAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("angle")+"");
                        monitorPointAlertc.setContent("回转左限位报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(8192) != -1) {
                        equipmentTowerDataAt.setSlewingRightLimitAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("angle")+"");
                        monitorPointAlertc.setContent("回转右限位报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(16777216) != -1) {
                        equipmentTowerDataAt.setMomentAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("percent")+"");
                        monitorPointAlertc.setContent("力矩报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(33554432) != -1) {
                        equipmentTowerDataAt.setWeightAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("load")+"");
                        monitorPointAlertc.setContent("吊重报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(536870912) != -1) {
                        equipmentTowerDataAt.setWindSpeedAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("windSpeed")+"");
                        monitorPointAlertc.setContent("风速报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    Integer alarm2 = Integer.valueOf(String.valueOf(data.get("alarm2")));
                    List<Integer> alarm2List = ApiUtils.orInverseRun(alarm2);
                    if (alarm2List.indexOf(1) != -1) {
                        equipmentTowerDataAt.setMaximumAmplitudeLimitAlarm("3");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("radius")+"");
                        monitorPointAlertc.setContent("最大幅度限位违章");
                        monitorPointAlertc.setAlertType("2");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm2List.indexOf(2) != -1) {
                        equipmentTowerDataAt.setMinimumAmplitudeLimitAlarm("3");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("radius")+"");
                        monitorPointAlertc.setContent("最小幅度限位违章");
                        monitorPointAlertc.setAlertType("2");
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm2List.indexOf(4) != -1) {
                        equipmentTowerDataAt.setAltitudeUpperLimitAlarm("3");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("高度上限位违章");
                        monitorPointAlertc.setAlertType("2");
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm2List.indexOf(8) != -1) {
                        equipmentTowerDataAt.setAltitudeLowerLimitAlarm("3");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("高度下限位违章");
                        monitorPointAlertc.setAlertType("2");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm2List.indexOf(16) != -1) {
                        equipmentTowerDataAt.setMomentAlarm("3");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("percent")+"");
                        monitorPointAlertc.setContent("力矩违章");
                        monitorPointAlertc.setAlertType("2");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm2List.indexOf(32) != -1) {
                        equipmentTowerDataAt.setWeightAlarm("3");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("load")+"");
                        monitorPointAlertc.setContent("吊重违章");
                        monitorPointAlertc.setAlertType("2");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    equipmentTowerDataAt.setWeight(data.get("load")+"");
                    equipmentTowerDataAt.setAmplitude(data.get("radius")+"");
                    equipmentTowerDataAt.setHeight(data.get("height")+"");
                    equipmentTowerDataAt.setMoment(Float.valueOf( data.get("percent")+""));
                    equipmentTowerDataAt.setWindspeed(data.get("windSpeed")+"");
                    Double wind = Double.valueOf(data.get("windSpeed")+"");
                    equipmentTowerDataAt.setWindLevel(Math.round(Math.pow(Math.pow(wind/0.836,2),1.0/3))+"");
                    equipmentTowerDataAt.setRotation(data.get("angle")+"");
                    equipmentTowerDataAt.setObliguity(Float.valueOf(data.get("obliqueAngle")+""));
                    equipmentTowerDataAt.setWeightPercentage(Float.valueOf(data.get("weightPercentage")+""));
                    equipmentTowerDataAt.setWindPercentage(Float.valueOf(data.get("windPercentage")+""));
                    equipmentTowerDataAt.setObliguityPercentage(Float.valueOf(data.get("obliguityPercentage")+""));
                    equipmentTowerDataAt.setBrakingStatus(data.get("brakingStatus")+"");
                    dataList.add(equipmentTowerDataAt);
                }else{
                    throw new CrmException(500, "您未传输数据主体！");
                }
            }
            if(alertList.size()>0){
                monitorPointAlertService.saveAlertByList(alertList);
            }
            getBaseMapper().batchSaveData(dataList);
            return Result.ok();
        }catch (CrmException e) {
            e.printStackTrace();
            log.error("数据接口错误：" + e);
            throw e;
        } catch (DateException e){
            e.printStackTrace();
            log.error("数据接口错误：" + e);
            throw new CrmException(500, "日期时间格式不符合规范!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("数据接口错误：" + e);
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_SYSTEM_ERROR);
        }
    }

    @Override
    public BasePage<Map<String, Object>> selectPage(EquipmentTowerDataAtBO towerDataAtBO) {
        towerDataAtBO.setOrdersBd("a.monitor_time",false);
        return getBaseMapper().selectPage(towerDataAtBO.parse(),towerDataAtBO);
    }

    public static void main(String[] args) {
        System.out.println(20/0.836);
        System.out.println(Math.pow(20/0.836,2));
        System.out.println();
    }
}

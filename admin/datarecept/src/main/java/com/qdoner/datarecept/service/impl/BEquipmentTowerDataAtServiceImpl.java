package com.qdoner.datarecept.service.impl;

import cn.hutool.core.date.DateException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.common.Result;
import com.kakarote.core.exception.CrmException;
import com.qdoner.datarecept.commons.EnvMonitorCodeEnum;
import com.qdoner.datarecept.entity.PO.*;
import com.qdoner.datarecept.mapper.BEquipmentTowerDataAtMapper;
import com.qdoner.datarecept.service.*;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.qdoner.datarecept.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
 * @author wudw
 * @since 2022-07-12
 */
@Service
public class BEquipmentTowerDataAtServiceImpl extends BaseServiceImpl<BEquipmentTowerDataAtMapper, BEquipmentTowerDataAt> implements IBEquipmentTowerDataAtService {

    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private IBMonitorPointAlertService monitorPointAlertService;
    @Autowired
    private IEquipmentOfflineService offlineService;

    @Autowired
    private EqStatusCommonService statusCommonService;

    @Override
    public Result saveData(List<Map<String, Object>> list) {
        try {
            List<BEquipmentTowerDataAt> dataList = new ArrayList<>();

            for(Map data:list){
                List<BMonitorPointAlert> alertList = new ArrayList<>();
                BEquipmentTowerDataAt equipmentTowerDataAt = new BEquipmentTowerDataAt();
                BMonitorPointAlert monitorPointAlert = null;
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
                    monitorPointAlert = new BMonitorPointAlert();
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
                    Integer alarm1 = Integer.valueOf(String.valueOf(data.get("alarm1")));
                    List<Integer> alarm1List = ApiUtils.orInverseRun(alarm1);
                    if (alarm0List.indexOf(1) != -1 &&alarm1List.indexOf(1) == -1) {
                        equipmentTowerDataAt.setAntiCollisionLeftTurnAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("防碰撞左转预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(2) != -1 &&alarm1List.indexOf(2) == -1) {
                        equipmentTowerDataAt.setAntiCollisionRightTurnAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("防碰撞右转预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(4) != -1 &&alarm1List.indexOf(4) == -1) {
                        equipmentTowerDataAt.setCollisionTrolleyAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("防碰撞小车出预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(8) != -1 &&alarm1List.indexOf(8) == -1) {
                        equipmentTowerDataAt.setCollisionTrolleyEntryAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("防碰撞小车入预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(16) != -1 &&alarm1List.indexOf(16) == -1) {
                        equipmentTowerDataAt.setLeftTurnEnvironmentalAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("左转环境预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(32) != -1 &&alarm1List.indexOf(32) == -1) {
                        equipmentTowerDataAt.setRightTurnEnvironmentalAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("右转环境预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(64) != -1 &&alarm1List.indexOf(64) == -1) {
                        equipmentTowerDataAt.setTrolleyExitEnvironmentAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("小车出环境预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(128) != -1 &&alarm1List.indexOf(128) == -1) {
                        equipmentTowerDataAt.setTrolleyEntryEnvironmentAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("小车进环境预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(256) != -1 &&alarm1List.indexOf(256) == -1) {
                        equipmentTowerDataAt.setMaximumAmplitudeLimitAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("radius")+"");
                        monitorPointAlertc.setContent("最大幅度限位预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(512) != -1 &&alarm1List.indexOf(512) == -1) {
                        equipmentTowerDataAt.setMinimumAmplitudeLimitAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("radius")+"");
                        monitorPointAlertc.setContent("最小幅度限位预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(1024) != -1 &&alarm1List.indexOf(1024) == -1) {
                        equipmentTowerDataAt.setAltitudeUpperLimitAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("高度上限位预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(2048) != -1 &&alarm1List.indexOf(2048) == -1) {
                        equipmentTowerDataAt.setAltitudeLowerLimitAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("高度下限位预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(4096) != -1 &&alarm1List.indexOf(4096) == -1) {
                        equipmentTowerDataAt.setSlewingLeftLimitAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("angle")+"");
                        monitorPointAlertc.setContent("回转左限位预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(8192) != -1 &&alarm1List.indexOf(8192) == -1) {
                        equipmentTowerDataAt.setSlewingRightLimitAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("angle")+"");
                        monitorPointAlertc.setContent("回转右限位预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(16777216) != -1 &&alarm1List.indexOf(16777216) == -1) {
                        equipmentTowerDataAt.setMomentAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("percent")+"");
                        monitorPointAlertc.setContent("力矩预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(33554432) != -1 &&alarm1List.indexOf(33554432) == -1) {
                        equipmentTowerDataAt.setWeightAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("load")+"");
                        monitorPointAlertc.setContent("吊重预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm0List.indexOf(536870912) != -1 &&alarm1List.indexOf(536870912) == -1) {
                        equipmentTowerDataAt.setWindSpeedAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("windSpeed")+"");
                        monitorPointAlertc.setContent("风速预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }

                    if (alarm1List.indexOf(1) != -1) {
                        equipmentTowerDataAt.setAntiCollisionLeftTurnAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("防碰撞左转报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(2) != -1 ) {
                        equipmentTowerDataAt.setAntiCollisionRightTurnAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("防碰撞右转报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(4) != -1) {
                        equipmentTowerDataAt.setCollisionTrolleyAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("防碰撞小车出报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(8) != -1) {
                        equipmentTowerDataAt.setCollisionTrolleyEntryAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("防碰撞小车入报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(15) != -1) {
                        equipmentTowerDataAt.setCollisionAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("碰撞报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(16) != -1) {
                        equipmentTowerDataAt.setLeftTurnEnvironmentalAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("左转环境报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(32) != -1) {
                        equipmentTowerDataAt.setRightTurnEnvironmentalAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("右转环境报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(64) != -1) {
                        equipmentTowerDataAt.setTrolleyExitEnvironmentAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("小车出环境报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(128) != -1) {
                        equipmentTowerDataAt.setTrolleyEntryEnvironmentAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("小车进环境报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(134217728) != -1 ) {
                        equipmentTowerDataAt.setForbidEntryAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("禁入区报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(201326832) != -1 ) {
                        equipmentTowerDataAt.setRestrictedAreaAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("限行区报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(256) != -1) {
                        equipmentTowerDataAt.setMaximumAmplitudeLimitAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("radius")+"");
                        monitorPointAlertc.setContent("最大幅度限位报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(512) != -1) {
                        equipmentTowerDataAt.setMinimumAmplitudeLimitAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("radius")+"");
                        monitorPointAlertc.setContent("最小幅度限位报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(1024) != -1) {
                        equipmentTowerDataAt.setAltitudeUpperLimitAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("高度上限位报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(2048) != -1) {
                        equipmentTowerDataAt.setAltitudeLowerLimitAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("高度下限位报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(4096) != -1) {
                        equipmentTowerDataAt.setSlewingLeftLimitAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("angle")+"");
                        monitorPointAlertc.setContent("回转左限位报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(8192) != -1) {
                        equipmentTowerDataAt.setSlewingRightLimitAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("angle")+"");
                        monitorPointAlertc.setContent("回转右限位报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(16777216) != -1) {
                        equipmentTowerDataAt.setMomentAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("percent")+"");
                        monitorPointAlertc.setContent("力矩报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(33554432) != -1) {
                        equipmentTowerDataAt.setWeightAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("load")+"");
                        monitorPointAlertc.setContent("吊重报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm1List.indexOf(536870912) != -1) {
                        equipmentTowerDataAt.setWindSpeedAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
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
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("radius")+"");
                        monitorPointAlertc.setContent("最大幅度限位违章");
                        monitorPointAlertc.setAlertType("2");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm2List.indexOf(2) != -1) {
                        equipmentTowerDataAt.setMinimumAmplitudeLimitAlarm("3");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("radius")+"");
                        monitorPointAlertc.setContent("最小幅度限位违章");
                        monitorPointAlertc.setAlertType("2");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm2List.indexOf(4) != -1) {
                        equipmentTowerDataAt.setAltitudeUpperLimitAlarm("3");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("高度上限位违章");
                        monitorPointAlertc.setAlertType("2");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm2List.indexOf(8) != -1) {
                        equipmentTowerDataAt.setAltitudeLowerLimitAlarm("3");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("高度下限位违章");
                        monitorPointAlertc.setAlertType("2");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm2List.indexOf(16) != -1) {
                        equipmentTowerDataAt.setMomentAlarm("3");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("percent")+"");
                        monitorPointAlertc.setContent("力矩违章");
                        monitorPointAlertc.setAlertType("2");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarm2List.indexOf(32) != -1) {
                        equipmentTowerDataAt.setWeightAlarm("3");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("load")+"");
                        monitorPointAlertc.setContent("吊重违章");
                        monitorPointAlertc.setAlertType("2");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }

                    BigDecimal load = new BigDecimal(Double.valueOf((data.get("load")==null?0:data.get("load"))+""));
                    equipmentTowerDataAt.setWeight(load.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"");
                    BigDecimal radius = new BigDecimal(Double.valueOf((data.get("radius")==null?0:data.get("radius"))+""));
                    equipmentTowerDataAt.setAmplitude(radius.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"");

                    BigDecimal height = new BigDecimal(Double.valueOf((data.get("height")==null?0:data.get("height"))+""));
                    equipmentTowerDataAt.setHeight(height.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"");

                    BigDecimal percent = new BigDecimal(Double.valueOf((data.get("percent")==null?0:data.get("percent"))+""));
                    equipmentTowerDataAt.setMoment(percent.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());

                    BigDecimal windSpeed = new BigDecimal(Double.valueOf((data.get("windSpeed")==null?0:data.get("windSpeed"))+""));
                    equipmentTowerDataAt.setWindspeed(windSpeed.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"");


                    Double wind = Double.valueOf((data.get("windSpeed")==null?0:data.get("windSpeed"))+"");
                    equipmentTowerDataAt.setWindLevel(Math.round(Math.pow(Math.pow(wind/0.836,2),1.0/3))+"");

                    BigDecimal angle = new BigDecimal(Double.valueOf((data.get("angle")==null?0:data.get("angle"))+""));
                    equipmentTowerDataAt.setRotation(angle.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"");

                    BigDecimal obliqueAngle = new BigDecimal(Double.valueOf((data.get("obliqueAngle")==null?0:data.get("obliqueAngle"))+""));
                    equipmentTowerDataAt.setObliguity(obliqueAngle.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());

                    BigDecimal weightPercentage = new BigDecimal(Double.valueOf((data.get("weightPercentage")==null?0:data.get("weightPercentage"))+""));
                    equipmentTowerDataAt.setWeightPercentage(weightPercentage.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());

                    BigDecimal windPercentage = new BigDecimal(Double.valueOf((data.get("windPercentage")==null?0:data.get("windPercentage"))+""));
                    equipmentTowerDataAt.setWindPercentage(windPercentage.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());

                    BigDecimal obliguityPercentage = new BigDecimal(Double.valueOf((data.get("obliguityPercentage")==null?0:data.get("obliguityPercentage"))+""));
                    equipmentTowerDataAt.setObliguityPercentage(obliguityPercentage.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                    equipmentTowerDataAt.setBrakingStatus((data.get("brakingStatus")==null?0:data.get("brakingStatus"))+"");
                    dataList.add(equipmentTowerDataAt);
                    //2023-03-02 邵化振 状态更新改用统一方法
//                    List<Map> lineList =  offlineService.getOfflineElevator(equipmentTowerDataAt.getEquipmentId());
//                    if(lineList.size() == 0){
//                        EquipmentOffline equipmentOffline = new EquipmentOffline();
//                        equipmentOffline.setStatus(1);
//                        equipmentOffline.setEquipmentId(equipmentTowerDataAt.getEquipmentId());
//                        equipmentOffline.setMonitorTime(new Date());
//                        equipmentOffline.setTenantId(equipmentTowerDataAt.getTenantId());
//                        equipmentOffline.setProjectId(equipmentTowerDataAt.getProjectId());
//                        offlineService.save(equipmentOffline);
//                    }
                    statusCommonService.handleOfflineSatus(equipmentTowerDataAt.getEquipmentId(),"1");
                    //2023-03-02 邵化振 状态更新改用统一方法  完
                }else{
                    throw new CrmException(500, "您未传输数据主体！");
                }
                if(alertList.size()>0){
                    monitorPointAlertService.saveAlertByList(alertList);
                }else {
                    if(monitorPointAlert != null){
                        monitorPointAlertService.updataAlert(monitorPointAlert);
                    }

                }
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
}

package com.kakarote.build.equipment.service.impl;

import cn.hutool.core.date.DateException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.build.api.common.EnvMonitorCodeEnum;
import com.kakarote.build.equipment.entity.BO.EquipmentElevatorDataAtBO;
import com.kakarote.build.equipment.entity.PO.EquipmentElevatorDataAt;
import com.kakarote.build.equipment.entity.PO.EquipmentTowerDataAt;
import com.kakarote.build.equipment.entity.PO.MonitorPointAlert;
import com.kakarote.build.equipment.mapper.EquipmentElevatorDataAtMapper;
import com.kakarote.build.equipment.service.IEquipmentElevatorDataAtService;
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
 * 升降电梯设备实时数据表 服务实现类
 * </p>
 *
 * @author shz
 * @since 2022-03-22
 */
@Service
public class EquipmentElevatorDataAtServiceImpl extends BaseServiceImpl<EquipmentElevatorDataAtMapper, EquipmentElevatorDataAt> implements IEquipmentElevatorDataAtService {

    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private IMonitorPointAlertService monitorPointAlertService;
    @Override
    public Result saveData(List<Map<String, Object>> list) {
        try {

            if(list != null && list.size()>0){
                //Map data = (Map<String, Object>) list.get(0);
                List<EquipmentElevatorDataAt> dataList = new ArrayList<>();
                List<MonitorPointAlert> alertList = new ArrayList<>();
                for(Map data:list){
                    EquipmentElevatorDataAt equipmentElevatorDataAt = new EquipmentElevatorDataAt();
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
                    equipmentElevatorDataAt.setTenantId(projectInfo.getTenantId());
                    equipmentElevatorDataAt.setProjectId(projectId);
                    if (data.get("deviceSn") == null || "".equals(data.get("deviceSn"))) {
                        throw new CrmException(500, "设备编号不能为空");
                    }
                    equipmentElevatorDataAt.setEquipmentId(Integer.valueOf(data.get("deviceSn") + ""));
                    MonitorPointAlert monitorPointAlert = new MonitorPointAlert();
                    monitorPointAlert.setEquipmentId(equipmentElevatorDataAt.getEquipmentId());
                    monitorPointAlert.setProjectId(equipmentElevatorDataAt.getProjectId());
                    monitorPointAlert.setTenantId(equipmentElevatorDataAt.getTenantId());
                    if (data.get("deviceTime") == null || "".equals(data.get("deviceTime"))) {
                        throw new CrmException(500, "设备数据时间不能为空");
                    }
                    String regdate=(String)data.get("deviceTime");
                    DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date monitorTime = dft.parse(regdate);
                    equipmentElevatorDataAt.setMonitorTime(monitorTime);
                    if (data.get("earlyAlarm") == null || "".equals(data.get("earlyAlarm"))) {
                        throw new CrmException(500, "系统预警状态编码不能为空");
                    }
                    Integer earlyAlarm = Integer.valueOf(String.valueOf(data.get("earlyAlarm")));
                    List<Integer> earlyAlarmList = ApiUtils.orInverseRun(earlyAlarm);
                    if (earlyAlarmList.indexOf(1) != -1) {
                        equipmentElevatorDataAt.setLoadAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("load")+"");
                        monitorPointAlertc.setContent("载重预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (earlyAlarmList.indexOf(2) != -1) {
                        equipmentElevatorDataAt.setNumberAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("percentRate")+"");
                        monitorPointAlertc.setContent("人数预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (earlyAlarmList.indexOf(4) != -1) {
                        equipmentElevatorDataAt.setWindSpeedAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("wind")+"");
                        monitorPointAlertc.setContent("风速预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (earlyAlarmList.indexOf(8) != -1) {
                        equipmentElevatorDataAt.setSpeedWarning("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("speed")+"");
                        monitorPointAlertc.setContent("速度预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (earlyAlarmList.indexOf(16) != -1) {
                        equipmentElevatorDataAt.setAltitudeAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("高度预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (earlyAlarmList.indexOf(32) != -1) {
                        equipmentElevatorDataAt.setTiltAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("angle")+"");
                        monitorPointAlertc.setContent("倾斜预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (earlyAlarmList.indexOf(64) != -1) {
                        equipmentElevatorDataAt.setRoofFlushingAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("冲顶预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (earlyAlarmList.indexOf(128) != -1) {
                        equipmentElevatorDataAt.setUpperLimitAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("上限位预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (earlyAlarmList.indexOf(256) != -1) {
                        equipmentElevatorDataAt.setLowerLimitAlarm("2");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("下限位预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    Integer alarm = Integer.valueOf(String.valueOf(data.get("alarm")));
                    List<Integer> alarmList = ApiUtils.orInverseRun(alarm);
                    if (alarmList.indexOf(1) != -1) {
                        equipmentElevatorDataAt.setLoadAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("load")+"");
                        monitorPointAlertc.setContent("载重报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarmList.indexOf(2) != -1) {
                        equipmentElevatorDataAt.setNumberAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("numberOfPeople")+"");
                        monitorPointAlertc.setContent("人数报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarmList.indexOf(4) != -1) {
                        equipmentElevatorDataAt.setWindSpeedAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("wind")+"");
                        monitorPointAlertc.setContent("风速报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarmList.indexOf(16) != -1) {
                        equipmentElevatorDataAt.setAltitudeAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("高度报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarmList.indexOf(32) != -1) {
                        equipmentElevatorDataAt.setTiltAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("angle")+"");
                        monitorPointAlertc.setContent("倾斜报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarmList.indexOf(64) != -1) {
                        equipmentElevatorDataAt.setRoofFlushingAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("冲顶报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarmList.indexOf(128) != -1) {
                        equipmentElevatorDataAt.setUpperLimitAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("上限位报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarmList.indexOf(256) != -1) {
                        equipmentElevatorDataAt.setLowerLimitAlarm("1");
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("下限位报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    equipmentElevatorDataAt.setWeight( data.get("load")+"");
                    equipmentElevatorDataAt.setWeightPercent((Double) data.get("percentRate"));
                    Double d = (Double) data.get("height");
                    equipmentElevatorDataAt.setHeight(d.intValue());
                    equipmentElevatorDataAt.setNumberOfPeople(Integer.valueOf(data.get("numberOfPeople")+""));
                    equipmentElevatorDataAt.setSpeed( data.get("speed")+"");
                    equipmentElevatorDataAt.setXAngle(data.get("angle")+"");
                    equipmentElevatorDataAt.setWind(Double.valueOf(data.get("wind")+""));
                    equipmentElevatorDataAt.setLiftingDirection(data.get("runningState")+"");
                    equipmentElevatorDataAt.setBeforeLock(String.valueOf(data.get("beforeLock")));
                    equipmentElevatorDataAt.setAfterLock(String.valueOf(data.get("afterLock")));
                    equipmentElevatorDataAt.setXAngleRatio(String.valueOf(data.get("xAngleRatio")));
                    equipmentElevatorDataAt.setYAngleRatio(String.valueOf(data.get("yAngleRatio")));
                    equipmentElevatorDataAt.setYAngle(String.valueOf(data.get("yAngle")));
                    dataList.add(equipmentElevatorDataAt);
//                    getBaseMapper().insert(equipmentElevatorDataAt);
                }
                monitorPointAlertService.saveAlertByList(alertList);
                getBaseMapper().batchSaveData(dataList);
            }else{
                throw new CrmException(500, "您未传输数据主体！");
            }
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
    public BasePage<Map<String, Object>> selectPage(EquipmentElevatorDataAtBO elevatorDataAt) {
        elevatorDataAt.setOrdersBd("a.monitor_time",false);
        return getBaseMapper().selectPage(elevatorDataAt.parse(),elevatorDataAt);
    }

}

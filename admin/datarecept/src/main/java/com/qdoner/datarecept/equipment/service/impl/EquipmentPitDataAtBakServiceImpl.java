package com.qdoner.datarecept.equipment.service.impl;

import cn.hutool.core.date.DateException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qdoner.datarecept.api.common.EnvMonitorCodeEnum;
import com.qdoner.datarecept.equipment.entity.PO.EquipmentMonitorPoint;
import com.qdoner.datarecept.equipment.entity.PO.EquipmentPitDataAtBak;
import com.qdoner.datarecept.equipment.entity.PO.MonitorPointAlert;
import com.qdoner.datarecept.equipment.mapper.EquipmentPitDataAtBakMapper;
import com.qdoner.datarecept.equipment.service.IEquipmentMonitorPointService;
import com.qdoner.datarecept.equipment.service.IEquipmentPitDataAtBakService;
import com.qdoner.datarecept.equipment.service.IMonitorPointAlertService;
import com.qdoner.datarecept.entity.PO.ProjectInfo;
import com.qdoner.datarecept.service.IProjectInfoService;
import com.kakarote.core.common.Result;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-基坑设备实时数据表 服务实现类
 * </p>
 *
 * @author shz
 * @since 2022-03-30
 */
@Service
public class EquipmentPitDataAtBakServiceImpl extends BaseServiceImpl<EquipmentPitDataAtBakMapper, EquipmentPitDataAtBak> implements IEquipmentPitDataAtBakService {

    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private IEquipmentMonitorPointService equipmentMonitorPointService;
    @Autowired
    private IMonitorPointAlertService monitorPointAlertService;

    @Override
    public Result saveData(List<Map<String,Object>> list) {
        try {

            if(list != null && list.size()>0){
                List<EquipmentPitDataAtBak> dataList = new ArrayList<>();
                List<MonitorPointAlert> alertList = new ArrayList<>();
                for(Map data:list){
                    EquipmentPitDataAtBak equipmentFormworkDataAt = new EquipmentPitDataAtBak();
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
                    equipmentFormworkDataAt.setTenantId(projectInfo.getTenantId());
                    equipmentFormworkDataAt.setProjectId(projectId);

                    if (data.get("deviceSn") == null || "".equals(data.get("deviceSn"))) {
                        throw new CrmException(500, "传感器编号不能为空");
                    }
                    equipmentFormworkDataAt.setPointId((Integer) data.get("deviceSn"));
                    if (data.get("deviceId") == null || "".equals(data.get("deviceId"))) {
                        throw new CrmException(500, "设备编号不能为空");
                    }
                    equipmentFormworkDataAt.setEquipmentId((Integer) data.get("deviceId"));
                    MonitorPointAlert monitorPointAlert = new MonitorPointAlert();
                    monitorPointAlert.setEquipmentId(equipmentFormworkDataAt.getEquipmentId());
                    monitorPointAlert.setProjectId(equipmentFormworkDataAt.getProjectId());
                    monitorPointAlert.setTenantId(equipmentFormworkDataAt.getTenantId());
                    monitorPointAlert.setMonitorPointId(equipmentFormworkDataAt.getPointId());
                    EquipmentMonitorPoint equipmentMonitorPoint= equipmentMonitorPointService.getByIdInApi(equipmentFormworkDataAt.getPointId());
                    if (data.get("deviceTime") == null || "".equals(data.get("deviceTime"))) {
                        throw new CrmException(500, "设备数据时间不能为空");
                    }
                    String regdate=(String)data.get("deviceTime");
                    DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    equipmentFormworkDataAt.setMonitorTime(dft.parse(regdate));
                    if (data.get("deviceType") == null || "".equals(data.get("deviceType"))) {
                        throw new CrmException(500, "传感器类型不能为空");
                    }
                    equipmentFormworkDataAt.setDeviceType( data.get("deviceType")+"") ;
                    if(equipmentFormworkDataAt.getDeviceType().equals("1")){
                        if (data.get("currentWaterLevel") == null || "".equals(data.get("currentWaterLevel"))) {
                            throw new CrmException(500, "本次测量值不能为空");
                        }
                        equipmentFormworkDataAt.setValue(data.get("currentWaterLevel")+"");
                        equipmentFormworkDataAt.setInitValue((Double) data.get("initWaterLevel"));
                        equipmentFormworkDataAt.setVariation((Double) data.get("currentChange"));
                        equipmentFormworkDataAt.setAggregateValue((Double) data.get("cumulativeChange"));
                        equipmentFormworkDataAt.setRateChange((Double) data.get("changeRate"));
                        equipmentFormworkDataAt.setCrack((Double) data.get("crack"));

                    }else if(equipmentFormworkDataAt.getDeviceType().equals("2")){
                        if (data.get("currentMeasure") == null || "".equals(data.get("currentMeasure"))) {
                            throw new CrmException(500, "本次测量值不能为空");
                        }
                        equipmentFormworkDataAt.setValue( data.get("currentMeasure")+"");
                        if (data.get("maxCumulativeDisplacement") == null || "".equals(data.get("maxCumulativeDisplacement"))) {
                            throw new CrmException(500, "累计位移不能为空");
                        }
                        equipmentFormworkDataAt.setAggregateValue((Double) data.get("maxCumulativeDisplacement"));
                        if (data.get("currentMaxDisplacement") == null || "".equals(data.get("currentMaxDisplacement"))) {
                            throw new CrmException(500, "本次变化速率为空");
                        }
                        equipmentFormworkDataAt.setRateChange((Double) data.get("currentMaxDisplacement"));
                        equipmentFormworkDataAt.setMaxCumulativeDisplacementPosition((Double) data.get("maxCumulativeDisplacementPosition"));
                        equipmentFormworkDataAt.setCurrentMaxDisplacementPosition((Double) data.get("currentMaxDisplacementPosition"));
                        equipmentFormworkDataAt.setXangle((Double) data.get("xAngle"));
                        equipmentFormworkDataAt.setYangle((Double) data.get("yAngle"));
                    }else if(equipmentFormworkDataAt.getDeviceType().equals("3")){
                        if (data.get("currentMeasure") == null || "".equals(data.get("currentMeasure"))) {
                            throw new CrmException(500, "本次测量值不能为空");
                        }
                        equipmentFormworkDataAt.setValue( data.get("currentMeasure")+"");
                        equipmentFormworkDataAt.setInitValue((Double) data.get("initElevation"));
                        equipmentFormworkDataAt.setVariation((Double) data.get("currentChange"));
                        equipmentFormworkDataAt.setAggregateValue((Double) data.get("cumulativeChange"));
                        equipmentFormworkDataAt.setRateChange((Double) data.get("changeRate"));
                        equipmentFormworkDataAt.setXangle((Double) data.get("xAngle"));
                        equipmentFormworkDataAt.setYangle((Double) data.get("yAngle"));
                    }else {
                        if (data.get("currentMeasure") == null || "".equals(data.get("currentMeasure"))) {
                            throw new CrmException(500, "本次测量值不能为空");
                        }
                        equipmentFormworkDataAt.setValue( data.get("currentMeasure")+"");
                        if (data.get("initPower") == null || "".equals(data.get("initPower"))) {
                            throw new CrmException(500, "初始值不能为空");
                        }
                        equipmentFormworkDataAt.setInitValue((Double) data.get("initPower"));
                        equipmentFormworkDataAt.setVariation((Double) data.get("currentChange"));
                        equipmentFormworkDataAt.setAggregateValue((Double) data.get("cumulativeChange"));
                        equipmentFormworkDataAt.setRateChange((Double) data.get("changeRate"));
                    }

                    if (data.get("deviceStatus") == null || "".equals(data.get("deviceStatus"))) {
                        throw new CrmException(500, "报警状态不能为空");
                    }
                    equipmentFormworkDataAt.setAlarmStatus((Integer) data.get("deviceStatus"));
                    if (equipmentFormworkDataAt.getAlarmStatus() != 1) {
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(equipmentFormworkDataAt.getValue());
                        if(equipmentFormworkDataAt.getAlarmStatus() == 2){
                            monitorPointAlertc.setContent(equipmentMonitorPoint.getPointName()+"预警");
                            monitorPointAlertc.setAlertType("0");
                            alertList.add(monitorPointAlertc);
                        }
                        if(equipmentFormworkDataAt.getAlarmStatus() == 3){
                            monitorPointAlertc.setContent(equipmentMonitorPoint.getPointName()+"报警");
                            monitorPointAlertc.setAlertType("1");
                            alertList.add(monitorPointAlertc);
                        }

                    }
                    equipmentFormworkDataAt.setBatteryVoltage((Double) data.get("batteryVoltage"));
                    equipmentFormworkDataAt.setAmbientTemperature((Double) data.get("ambientTemperature"));

//                    getBaseMapper().insert(equipmentFormworkDataAt);
                    dataList.add(equipmentFormworkDataAt);
                }
                monitorPointAlertService.saveAlertByList(alertList);
                getBaseMapper().batchSaveData(dataList);
            }else{
                throw new CrmException(500, "您未传输数据主体！");
            }
            return Result.ok(list);
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
    public Result saveFormworkData(List<Map<String,Object>> list) {
        try {
            List<EquipmentPitDataAtBak> dataList = new ArrayList<>();
            List<MonitorPointAlert> alertList = new ArrayList<>();
            if(list != null && list.size()>0){
                for(Map data :list){
                    EquipmentPitDataAtBak equipmentFormworkDataAt = new EquipmentPitDataAtBak();
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
                    equipmentFormworkDataAt.setTenantId(projectInfo.getTenantId());
                    equipmentFormworkDataAt.setProjectId(projectId);

                    if (data.get("deviceSn") == null || "".equals(data.get("deviceSn"))) {
                        throw new CrmException(500, "传感器编号不能为空");
                    }
                    equipmentFormworkDataAt.setPointId((Integer) data.get("deviceSn"));
                    if (data.get("deviceId") == null || "".equals(data.get("deviceId"))) {
                        throw new CrmException(500, "设备编号不能为空");
                    }
                    equipmentFormworkDataAt.setEquipmentId((Integer) data.get("deviceId"));
                    MonitorPointAlert monitorPointAlert = new MonitorPointAlert();
                    monitorPointAlert.setEquipmentId(equipmentFormworkDataAt.getEquipmentId());
                    monitorPointAlert.setProjectId(equipmentFormworkDataAt.getProjectId());
                    monitorPointAlert.setTenantId(equipmentFormworkDataAt.getTenantId());
                    monitorPointAlert.setMonitorPointId(equipmentFormworkDataAt.getPointId());
                    EquipmentMonitorPoint equipmentMonitorPoint= equipmentMonitorPointService.getByIdInApi(equipmentFormworkDataAt.getPointId());
                    if (data.get("deviceTime") == null || "".equals(data.get("deviceTime"))) {
                        throw new CrmException(500, "设备数据时间不能为空");
                    }
                    String regdate=(String)data.get("deviceTime");
                    DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    equipmentFormworkDataAt.setMonitorTime(dft.parse(regdate));
                    String type = "";
                    if(data.get("xAngle") != null){
                        type = "X轴倾角";
                        equipmentFormworkDataAt.setValue(data.get("xAngle")+"");
                    }else if(data.get("yAngle") != null){
                        type = "Y轴倾角";
                        equipmentFormworkDataAt.setValue(data.get("yAngle")+"");
                    }else if(data.get("pressure") != null){
                        type = "压力值";
                        equipmentFormworkDataAt.setValue(data.get("pressure")+"");
                    }else if(data.get("sedimentation") != null){
                        type = "沉降值";
                        equipmentFormworkDataAt.setValue(data.get("sedimentation")+"");
                    }else if(data.get("horizontalDisplacement") != null){
                        type = "水平位移";
                        equipmentFormworkDataAt.setValue(data.get("horizontalDisplacement")+"");
                    }else if(data.get("electricQuantity") != null){
                        type = "设备电量值";
                        equipmentFormworkDataAt.setValue(data.get("electricQuantity")+"");
                    }
                    if (data.get("alarmStatus") == null || "".equals(data.get("alarmStatus"))) {
                        throw new CrmException(500, "报警状态不能为空");
                    }
                    equipmentFormworkDataAt.setAlarmStatus((Integer) data.get("alarmStatus"));
                    if(equipmentFormworkDataAt.getAlarmStatus() == 1){
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(equipmentFormworkDataAt.getValue());
                        monitorPointAlertc.setContent(equipmentMonitorPoint.getPointName()+"预警");
                        monitorPointAlertc.setAlertType("0");
                        alertList.add(monitorPointAlertc);
                    }
                    if(equipmentFormworkDataAt.getAlarmStatus() == 1){
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(equipmentFormworkDataAt.getValue());
                        monitorPointAlertc.setContent(equipmentMonitorPoint.getPointName()+"报警警");
                        monitorPointAlertc.setAlertType("1");
                        alertList.add(monitorPointAlertc);
                    }
                    //equipmentFormworkDataAt.setDeviceStatus( data.get("deviceStatus")+"");
                    dataList.add(equipmentFormworkDataAt);
                }
                if(alertList.size()>0){
                    monitorPointAlertService.saveAlertByList(alertList);
                }
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
}

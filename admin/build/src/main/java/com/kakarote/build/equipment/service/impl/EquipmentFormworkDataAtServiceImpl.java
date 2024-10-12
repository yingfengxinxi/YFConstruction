package com.kakarote.build.equipment.service.impl;

import cn.hutool.core.date.DateException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.build.api.common.EnvMonitorCodeEnum;
import com.kakarote.build.equipment.entity.PO.EquipmentFormworkDataAt;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorPoint;
import com.kakarote.build.equipment.entity.PO.EquipmentTowerDataAt;
import com.kakarote.build.equipment.entity.PO.MonitorPointAlert;
import com.kakarote.build.equipment.mapper.EquipmentFormworkDataAtMapper;
import com.kakarote.build.equipment.service.IEquipmentFormworkDataAtService;
import com.kakarote.build.equipment.service.IEquipmentMonitorPointService;
import com.kakarote.build.equipment.service.IMonitorPointAlertService;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.project.service.IProjectInfoService;
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
 * 高支模实时监测数据 服务实现类
 * </p>
 *
 * @author shz
 * @since 2022-03-30
 */
@Service
public class EquipmentFormworkDataAtServiceImpl extends BaseServiceImpl<EquipmentFormworkDataAtMapper, EquipmentFormworkDataAt> implements IEquipmentFormworkDataAtService {

    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private IEquipmentMonitorPointService equipmentMonitorPointService;
    @Autowired
    private IMonitorPointAlertService monitorPointAlertService;
    @Override
    public Result saveData(List<Map<String,Object>> list) {
        try {
            List<EquipmentFormworkDataAt> dataList = new ArrayList<>();
            List<MonitorPointAlert> alertList = new ArrayList<>();
            if(list != null && list.size()>0){
                for(Map data :list){
                    EquipmentFormworkDataAt equipmentFormworkDataAt = new EquipmentFormworkDataAt();
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
                    DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
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
                    equipmentFormworkDataAt.setAlarmStatus( data.get("alarmStatus")+"");
                    if("1".equals(equipmentFormworkDataAt.getAlarmStatus())){
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(equipmentFormworkDataAt.getValue());
                        monitorPointAlertc.setContent(equipmentMonitorPoint.getPointName()+"预警");
                        monitorPointAlertc.setAlertType("0");
                        alertList.add(monitorPointAlertc);
                    }
                    if("2".equals(equipmentFormworkDataAt.getAlarmStatus())){
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(equipmentFormworkDataAt.getValue());
                        monitorPointAlertc.setContent(equipmentMonitorPoint.getPointName()+"报警");
                        monitorPointAlertc.setAlertType("1");
                        alertList.add(monitorPointAlertc);
                    }
                    equipmentFormworkDataAt.setDeviceStatus( data.get("deviceStatus")+"");
                    dataList.add(equipmentFormworkDataAt);
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
}

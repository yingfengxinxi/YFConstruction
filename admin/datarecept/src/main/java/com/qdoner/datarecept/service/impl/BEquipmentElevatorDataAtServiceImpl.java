package com.qdoner.datarecept.service.impl;

import cn.hutool.core.date.DateException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.qdoner.datarecept.commons.EnvMonitorCodeEnum;
import com.qdoner.datarecept.entity.BO.EquipmentElevatorDataAtBO;
import com.qdoner.datarecept.entity.PO.*;
import com.qdoner.datarecept.mapper.BEquipmentElevatorDataAtMapper;
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
 * 升降电梯设备实时数据表 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2022-07-21
 */
@Service
public class BEquipmentElevatorDataAtServiceImpl extends BaseServiceImpl<BEquipmentElevatorDataAtMapper, BEquipmentElevatorDataAt> implements IBEquipmentElevatorDataAtService {

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

            if(list != null && list.size()>0){
                //Map data = (Map<String, Object>) list.get(0);
                List<BEquipmentElevatorDataAt> dataList = new ArrayList<>();

                for(Map data:list){
                    List<BMonitorPointAlert> alertList = new ArrayList<>();
                    BEquipmentElevatorDataAt equipmentElevatorDataAt = new BEquipmentElevatorDataAt();
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
                    BMonitorPointAlert monitorPointAlert = new BMonitorPointAlert();
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
                    Integer alarm = Integer.valueOf(String.valueOf(data.get("alarm")));
                    List<Integer> alarmList = ApiUtils.orInverseRun(alarm);
                    if (earlyAlarmList.indexOf(1) != -1 &&alarmList.indexOf(1) == -1) {
                        equipmentElevatorDataAt.setLoadAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("load")+"");
                        monitorPointAlertc.setContent("载重预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (earlyAlarmList.indexOf(2) != -1 &&alarmList.indexOf(2) == -1) {
                        equipmentElevatorDataAt.setNumberAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("percentRate")+"");
                        monitorPointAlertc.setContent("人数预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (earlyAlarmList.indexOf(4) != -1 &&alarmList.indexOf(4) == -1) {
                        equipmentElevatorDataAt.setWindSpeedAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("wind")+"");
                        monitorPointAlertc.setContent("风速预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (earlyAlarmList.indexOf(8) != -1 &&alarmList.indexOf(8) == -1) {
                        equipmentElevatorDataAt.setSpeedWarning("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("speed")+"");
                        monitorPointAlertc.setContent("速度预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (earlyAlarmList.indexOf(16) != -1 &&alarmList.indexOf(16) == -1) {
                        equipmentElevatorDataAt.setAltitudeAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("高度预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (earlyAlarmList.indexOf(32) != -1 &&alarmList.indexOf(32) == -1) {
                        equipmentElevatorDataAt.setTiltAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("angle")+"");
                        monitorPointAlertc.setContent("倾斜预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (earlyAlarmList.indexOf(64) != -1 &&alarmList.indexOf(64) == -1) {
                        equipmentElevatorDataAt.setRoofFlushingAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("冲顶预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (earlyAlarmList.indexOf(128) != -1 &&alarmList.indexOf(128) == -1) {
                        equipmentElevatorDataAt.setUpperLimitAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("上限位预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (earlyAlarmList.indexOf(256) != -1 &&alarmList.indexOf(256) == -1) {
                        equipmentElevatorDataAt.setLowerLimitAlarm("2");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("下限位预警");
                        monitorPointAlertc.setAlertType("0");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }

                    if (alarmList.indexOf(1) != -1) {
                        equipmentElevatorDataAt.setLoadAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("load")+"");
                        monitorPointAlertc.setContent("载重报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarmList.indexOf(2) != -1) {
                        equipmentElevatorDataAt.setNumberAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("numberOfPeople")+"");
                        monitorPointAlertc.setContent("人数报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarmList.indexOf(4) != -1) {
                        equipmentElevatorDataAt.setWindSpeedAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("wind")+"");
                        monitorPointAlertc.setContent("风速报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarmList.indexOf(16) != -1) {
                        equipmentElevatorDataAt.setAltitudeAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("高度报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarmList.indexOf(32) != -1) {
                        equipmentElevatorDataAt.setTiltAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("angle")+"");
                        monitorPointAlertc.setContent("倾斜报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarmList.indexOf(64) != -1) {
                        equipmentElevatorDataAt.setRoofFlushingAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("冲顶报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarmList.indexOf(128) != -1) {
                        equipmentElevatorDataAt.setUpperLimitAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("上限位报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    if (alarmList.indexOf(256) != -1) {
                        equipmentElevatorDataAt.setLowerLimitAlarm("1");
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("height")+"");
                        monitorPointAlertc.setContent("下限位报警");
                        monitorPointAlertc.setAlertType("1");
                        monitorPointAlertc.setCreateTime(monitorTime);
                        alertList.add(monitorPointAlertc);
                    }
                    equipmentElevatorDataAt.setWeight( data.get("load")+"");
                    BigDecimal WeightPercent = new BigDecimal(Double.valueOf(data.get("percentRate")+""));
                    equipmentElevatorDataAt.setWeightPercent(WeightPercent.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());

                    BigDecimal Height = new BigDecimal(Double.valueOf(data.get("height")+""));
                    equipmentElevatorDataAt.setHeight(Height.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    equipmentElevatorDataAt.setNumberOfPeople(Integer.valueOf(data.get("numberOfPeople")+""));
                    equipmentElevatorDataAt.setSpeed( data.get("speed")+"");
                    equipmentElevatorDataAt.setXAngle(data.get("angle")==null?null:data.get("angle")+"");
                    BigDecimal Wind = new BigDecimal(Double.valueOf(data.get("wind")+""));
                    equipmentElevatorDataAt.setWind(Wind.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    equipmentElevatorDataAt.setLiftingDirection(data.get("runningState")+"");
                    equipmentElevatorDataAt.setBeforeLock(String.valueOf(data.get("beforeLock")));
                    equipmentElevatorDataAt.setAfterLock(String.valueOf(data.get("afterLock")));
                    equipmentElevatorDataAt.setXAngleRatio(String.valueOf(data.get("xAngleRatio")));
                    equipmentElevatorDataAt.setYAngleRatio(String.valueOf(data.get("yAngleRatio")));
                    equipmentElevatorDataAt.setYAngle(data.get("yAngle")==null?null:data.get("yAngle")+"");
                    dataList.add(equipmentElevatorDataAt);
                    //2023-03-02 邵化振 状态更新改用统一方法
//                    List<Map> lineList =  offlineService.getOfflineElevator(equipmentElevatorDataAt.getEquipmentId());
//                    if(lineList.size() == 0){
//                        EquipmentOffline equipmentOffline = new EquipmentOffline();
//                        equipmentOffline.setStatus(1);
//                        equipmentOffline.setEquipmentId(equipmentElevatorDataAt.getEquipmentId());
//                        equipmentOffline.setMonitorTime(new Date());
//                        equipmentOffline.setTenantId(equipmentElevatorDataAt.getTenantId());
//                        equipmentOffline.setProjectId(equipmentElevatorDataAt.getProjectId());
//                        offlineService.save(equipmentOffline);
//                    }
                    statusCommonService.handleOfflineSatus(equipmentElevatorDataAt.getEquipmentId(),"1");
                    //2023-03-02 邵化振 状态更新改用统一方法  完
//                    getBaseMapper().insert(equipmentElevatorDataAt);
                    if(alertList.size()>0){
                        monitorPointAlertService.saveAlertByList(alertList);
                    }else {
                        if(monitorPointAlert != null){
                            monitorPointAlertService.updataAlert(monitorPointAlert);
                        }

                    }
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

    @Override
    public BasePage<Map<String, Object>> selectPage(EquipmentElevatorDataAtBO elevatorDataAt) {
        return getBaseMapper().selectPage(elevatorDataAt.parse(),elevatorDataAt);
    }
}

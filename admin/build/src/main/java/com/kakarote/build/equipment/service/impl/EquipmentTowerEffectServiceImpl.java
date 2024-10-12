package com.kakarote.build.equipment.service.impl;

import cn.hutool.core.date.DateException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.build.api.common.EnvMonitorCodeEnum;
import com.kakarote.build.equipment.entity.BO.EquipmentTowerEffectBO;
import com.kakarote.build.equipment.entity.PO.EquipmentTowerEffect;
import com.kakarote.build.equipment.entity.PO.MonitorPointAlert;
import com.kakarote.build.equipment.mapper.EquipmentTowerEffectMapper;
import com.kakarote.build.equipment.service.IEquipmentAssetsService;
import com.kakarote.build.equipment.service.IEquipmentTowerEffectService;
import com.kakarote.build.equipment.service.IMonitorPointAlertService;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shz
 * @since 2021-12-22
 */
@Service
public class EquipmentTowerEffectServiceImpl extends BaseServiceImpl<EquipmentTowerEffectMapper, EquipmentTowerEffect> implements IEquipmentTowerEffectService {

    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private IMonitorPointAlertService monitorPointAlertService;
    @Autowired
    private IEquipmentAssetsService assetsService;

    @Override
    public Map selectDZTJ(Integer id) {
        Map tower = assetsService.selectTowerCraneById(id+"");
        Map map = new HashMap();
        List<Map> list = getBaseMapper().selectDZTJ(id);
        List<Map> list2 = getBaseMapper().selectLJTJ(id);
        List<Integer> dates = list.stream().map(x-> (Integer)x.get("date")).collect(Collectors.toList());
        List<Double> dz = list.stream().map(x-> (Double)x.get("val")).collect(Collectors.toList());
        List<Double> lj = list2.stream().map(x-> (Double)x.get("val")).collect(Collectors.toList());
        List<String> dzyj = new ArrayList<>();
        List<String> dzbj = new ArrayList<>();
        List<String> ljyj = new ArrayList<>();
        List<String> ljbj = new ArrayList<>();
        for(Integer i:dates){
            dzyj.add(tower.get("weightWarningValue")+"");
            dzbj.add(tower.get("weightAlarmValue")+"");
            ljyj.add(tower.get("torqueWarningValue")+"");
            ljbj.add(tower.get("torqueAlarmValue")+"");
        }
        map.put("dates",dates);
        map.put("dz",dz);
        map.put("lj",lj);
        map.put("dzyj",dzyj);
        map.put("dzbj",dzbj);
        map.put("ljyj",ljyj);
        map.put("ljbj",ljbj);

        return map;
    }

    @Override
    public Map selectJRGX(Integer id) {
        Map map = new HashMap();
        map.putAll(getBaseMapper().selectJRGX(id,"gzsc","if(sum(UNIX_TIMESTAMP(end_time)-UNIX_TIMESTAMP(start_time)) is null,0,sum(UNIX_TIMESTAMP(end_time)-UNIX_TIMESTAMP(start_time)))"));
        map.putAll(getBaseMapper().selectJRGX(id,"jrdz","if(sum(weight) is null,0,sum(weight))"));
        map.putAll(getBaseMapper().selectJRGX(id,"jrdc","COUNT(0)"));
        map.putAll(getBaseMapper().selectJRBJS(id));

        // 无数据报 nullPointerException
        Map<String, Object> onlineObj = getBaseMapper().selectOnline(id);
        if (onlineObj == null) {
            onlineObj = new HashMap<>();
        }
        map.putAll(onlineObj);
        return map;
    }

    @Override
    public Map selectSJXX(String code) {
        return getBaseMapper().selectSJXX(code);
    }

    @Override
    public Result saveData(List<Map<String,Object>> list) {
        try {

            if(list != null && list.size()>0){
                List<EquipmentTowerEffect> dataList = new ArrayList<EquipmentTowerEffect>();
                List<MonitorPointAlert> alertList = new ArrayList<>();
                for(Map data:list){
                    EquipmentTowerEffect equipmentTowerEffect = new EquipmentTowerEffect();
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
                    equipmentTowerEffect.setTenantId(projectInfo.getTenantId());
                    equipmentTowerEffect.setProjectId(projectId);
                    equipmentTowerEffect.setCreateTime(new Date());
                    if (data.get("deviceSn") == null || "".equals(data.get("deviceSn"))) {
                        throw new CrmException(500, "设备编号不能为空");
                    }
                    equipmentTowerEffect.setAssetId(Integer.valueOf(data.get("deviceSn") + ""));
                    MonitorPointAlert monitorPointAlert = new MonitorPointAlert();
                    monitorPointAlert.setEquipmentId(equipmentTowerEffect.getAssetId());
                    monitorPointAlert.setProjectId(equipmentTowerEffect.getProjectId());
                    monitorPointAlert.setTenantId(equipmentTowerEffect.getTenantId());
                    if (data.get("startRecordTime") == null || "".equals(data.get("startRecordTime"))) {
                        throw new CrmException(500, "起吊点时间不能为空");
                    }
                    String regdate=(String)data.get("startRecordTime");
                    DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    equipmentTowerEffect.setStartTime(dft.parse(regdate));
                    if (data.get("endRecordTime") == null || "".equals(data.get("endRecordTime"))) {
                        throw new CrmException(500, "卸吊点时间不能为空");
                    }
                    String enddate=(String)data.get("endRecordTime");
                    equipmentTowerEffect.setEndTime(dft.parse(enddate));
                    if (data.get("maxMomentEarlyAlarm") == null || "".equals(data.get("maxMomentEarlyAlarm"))) {
                        throw new CrmException(500, "力矩最大点系统预警不能为空");
                    }

                    equipmentTowerEffect.setMaxMomentEarlyAlarm((Integer) data.get("maxMomentEarlyAlarm"));

                    if(equipmentTowerEffect.getMaxMomentEarlyAlarm() !=0){
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("maxPercent")+"");
                        monitorPointAlertc.setContent("力矩最大点系统预警");
                        monitorPointAlertc.setAlertType("0");
                        alertList.add(monitorPointAlertc);
                    }
                    if (data.get("endEarlyAlarm") == null || "".equals(data.get("endEarlyAlarm"))) {
                        throw new CrmException(500, "卸吊点的系统预警状态编码不能为空");
                    }
                    equipmentTowerEffect.setEndEarlyAlarm((Integer) data.get("endEarlyAlarm"));
                    if(equipmentTowerEffect.getEndEarlyAlarm() !=0){
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("卸吊点系统预警");
                        monitorPointAlertc.setAlertType("0");
                        alertList.add(monitorPointAlertc);
                    }
                    if (data.get("startEarlyAlarm") == null || "".equals(data.get("startEarlyAlarm"))) {
                        throw new CrmException(500, "起吊点的系统预警状态编码不能为空");
                    }
                    equipmentTowerEffect.setStartEarlyAlarm((Integer) data.get("startEarlyAlarm"));
                    if(equipmentTowerEffect.getStartEarlyAlarm() !=0){
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("起吊点系统预警");
                        monitorPointAlertc.setAlertType("0");
                        alertList.add(monitorPointAlertc);
                    }
                    if (data.get("maxMomentAlarm") == null || "".equals(data.get("maxMomentAlarm"))) {
                        throw new CrmException(500, "力矩最大点系统报警状态编码不能为空");
                    }
                    equipmentTowerEffect.setMaxMomentAlarm((Integer) data.get("maxMomentAlarm"));
                    if(equipmentTowerEffect.getMaxMomentAlarm() !=0){
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("maxPercent")+"");
                        monitorPointAlertc.setContent("力矩最大点系统报警");
                        monitorPointAlertc.setAlertType("1");
                        alertList.add(monitorPointAlertc);
                    }
                    if (data.get("endAlarm") == null || "".equals(data.get("endAlarm"))) {
                        throw new CrmException(500, "卸吊点的系统报警状态编码不能为空");
                    }
                    equipmentTowerEffect.setEndAlarm((Integer) data.get("endAlarm"));
                    if(equipmentTowerEffect.getEndAlarm() !=0){
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("卸吊点系统报警");
                        monitorPointAlertc.setAlertType("1");
                        alertList.add(monitorPointAlertc);
                    }
                    if (data.get("startAlarm") == null || "".equals(data.get("startAlarm"))) {
                        throw new CrmException(500, "起吊点的系统报警状态编码不能为空");
                    }
                    equipmentTowerEffect.setStartAlarm((Integer) data.get("startAlarm"));
                    if(equipmentTowerEffect.getStartAlarm() !=0){
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("起吊点的系统报警");
                        monitorPointAlertc.setAlertType("1");
                        alertList.add(monitorPointAlertc);
                    }
                    if (data.get("startPeccancy") == null || "".equals(data.get("startPeccancy"))) {
                        throw new CrmException(500, "起吊点违章码不能为空");
                    }
                    equipmentTowerEffect.setStartPeccancy((Integer) data.get("startPeccancy"));
                    if(equipmentTowerEffect.getStartPeccancy() !=0){
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("起吊点系统违章");
                        monitorPointAlertc.setAlertType("2");
                        alertList.add(monitorPointAlertc);
                    }
                    if (data.get("endPeccancy") == null || "".equals(data.get("endPeccancy"))) {
                        throw new CrmException(500, "卸吊点违章码不能为空");
                    }
                    equipmentTowerEffect.setEndPeccancy((Integer) data.get("endPeccancy"));
                    if(equipmentTowerEffect.getEndPeccancy() !=0){
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData("");
                        monitorPointAlertc.setContent("卸吊点系统违章");
                        monitorPointAlertc.setAlertType("2");
                        alertList.add(monitorPointAlertc);
                    }
                    if (data.get("maxMomentPeccancy") == null || "".equals(data.get("maxMomentPeccancy"))) {
                        throw new CrmException(500, "力矩最大点违章码不能为空");
                    }
                    equipmentTowerEffect.setMaxMomentPeccancy((Integer) data.get("maxMomentPeccancy"));
                    if(equipmentTowerEffect.getMaxMomentPeccancy() !=0){
                        MonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("maxPercent")+"");
                        monitorPointAlertc.setContent("力矩最大点违章");
                        monitorPointAlertc.setAlertType("2");
                        alertList.add(monitorPointAlertc);
                    }
                    equipmentTowerEffect.setMaxMomentPeccancy((Integer) data.get("maxMomentPeccancy"));
                    if (data.get("weight") == null || "".equals(data.get("weight"))) {
                        throw new CrmException(500, "平均吊重不能为空");
                    }
                    equipmentTowerEffect.setWeight(Double.valueOf((Double) data.get("weight")));
                    equipmentTowerEffect.setMaxRadius(Double.valueOf((Double) data.get("maxRadius")));
                    equipmentTowerEffect.setStartAngle(Double.valueOf((Double) data.get("startAngle")));
                        equipmentTowerEffect.setMaxPercent(Double.valueOf((Double) data.get("maxPercent")));
                    equipmentTowerEffect.setMaxHeight(Double.valueOf((Double) data.get("maxHeight")));
                    equipmentTowerEffect.setEndAngle(Double.valueOf((Double) data.get("endAngle")));
                    equipmentTowerEffect.setMinRadius(Double.valueOf((Double) data.get("minRadius")));
                    equipmentTowerEffect.setMaxLoad(Double.valueOf((Double) data.get("maxLoad")));
                    equipmentTowerEffect.setMaxWindSpeed(Double.valueOf((Double) data.get("maxWindSpeed")));
                    equipmentTowerEffect.setMinHeight(Double.valueOf((Double) data.get("minHeight")));
                    equipmentTowerEffect.setLiftingPointRadius(Float.valueOf(data.get("liftingPointRadius")+""));
                    equipmentTowerEffect.setLiftingPointHeight(Float.valueOf(data.get("liftingPointHeight")+""));
                    equipmentTowerEffect.setLiftingPointRadiusUnload(Float.valueOf(data.get("liftingPointRadiusUnload")+""));
                    equipmentTowerEffect.setLiftingPointHeightUnload(Float.valueOf(data.get("liftingPointHeightUnload")+""));
                    dataList.add(equipmentTowerEffect);
                }
                getBaseMapper().batchSaveData(dataList);
                if(alertList.size()>0){
                    monitorPointAlertService.saveAlertByList(alertList);
                }
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
    public BasePage<Map<String, Object>> selectPage(EquipmentTowerEffectBO towerEffectBO) {
        towerEffectBO.setOrdersBd("a.start_time",false);
        return getBaseMapper().selectPage(towerEffectBO.parse(),towerEffectBO);
    }


}

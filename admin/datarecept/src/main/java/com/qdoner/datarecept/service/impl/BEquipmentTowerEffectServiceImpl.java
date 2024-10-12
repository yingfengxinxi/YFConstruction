package com.qdoner.datarecept.service.impl;

import cn.hutool.core.date.DateException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.common.Result;
import com.kakarote.core.exception.CrmException;
import com.qdoner.datarecept.commons.EnvMonitorCodeEnum;
import com.qdoner.datarecept.entity.PO.BEquipmentTowerEffect;
import com.qdoner.datarecept.entity.PO.BMonitorPointAlert;
import com.qdoner.datarecept.entity.PO.MonitorPointAlert;
import com.qdoner.datarecept.entity.PO.ProjectInfo;
import com.qdoner.datarecept.mapper.BEquipmentTowerEffectMapper;
import com.qdoner.datarecept.service.IBEquipmentTowerEffectService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.qdoner.datarecept.service.IBMonitorPointAlertService;
import com.qdoner.datarecept.service.IProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 塔机功效数据 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2022-07-12
 */
@Service
public class BEquipmentTowerEffectServiceImpl extends BaseServiceImpl<BEquipmentTowerEffectMapper, BEquipmentTowerEffect> implements IBEquipmentTowerEffectService {

    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private IBMonitorPointAlertService monitorPointAlertService;


    @Override
    public Result saveData(List<Map<String,Object>> list) {
        try {

            if(list != null && list.size()>0){
                List<BEquipmentTowerEffect> dataList = new ArrayList<BEquipmentTowerEffect>();

                for(Map data:list){
                    List<BMonitorPointAlert> alertList = new ArrayList<>();
                    BEquipmentTowerEffect equipmentTowerEffect = new BEquipmentTowerEffect();
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
                    BMonitorPointAlert monitorPointAlert = new BMonitorPointAlert();
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
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
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
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
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
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
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
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
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
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
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
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
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
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
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
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
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
                        BMonitorPointAlert monitorPointAlertc = monitorPointAlertService.copyPastry(monitorPointAlert);
                        monitorPointAlertc.setMonitorData(data.get("maxPercent")+"");
                        monitorPointAlertc.setContent("力矩最大点违章");
                        monitorPointAlertc.setAlertType("2");
                        alertList.add(monitorPointAlertc);
                    }
                    equipmentTowerEffect.setMaxMomentPeccancy((Integer) data.get("maxMomentPeccancy"));
                    if (data.get("weight") == null || "".equals(data.get("weight"))) {
                        throw new CrmException(500, "平均吊重不能为空");
                    }
                    BigDecimal Weight = new BigDecimal(Double.valueOf((Double) (data.get("weight")==null?0.0:data.get("weight"))));
                    equipmentTowerEffect.setWeight(Weight.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    BigDecimal MaxRadius = new BigDecimal(Double.valueOf((Double) (data.get("maxRadius")==null?0.0:data.get("maxRadius"))));
                    equipmentTowerEffect.setMaxRadius(MaxRadius.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    BigDecimal StartAngle = new BigDecimal(Double.valueOf((Double) (data.get("startAngle")==null?0.0:data.get("startAngle"))));
                    equipmentTowerEffect.setStartAngle(StartAngle.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    BigDecimal MaxPercent = new BigDecimal(Double.valueOf((Double) (data.get("maxPercent")==null?0.0:data.get("maxPercent"))));
                    equipmentTowerEffect.setMaxPercent(MaxPercent.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    BigDecimal MaxHeight = new BigDecimal(Double.valueOf((Double) (data.get("maxHeight")==null?0.0:data.get("maxHeight"))));
                    equipmentTowerEffect.setMaxHeight(MaxHeight.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    BigDecimal EndAngle = new BigDecimal(Double.valueOf((Double) (data.get("endAngle")==null?0.0:data.get("endAngle"))));
                    equipmentTowerEffect.setEndAngle(EndAngle.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    BigDecimal MinRadius = new BigDecimal(Double.valueOf((Double) (data.get("minRadius")==null?0.0:data.get("minRadius"))));
                    equipmentTowerEffect.setMinRadius(MinRadius.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    BigDecimal MaxLoad = new BigDecimal(Double.valueOf((Double) (data.get("maxLoad")==null?0.0:data.get("maxLoad"))));
                    equipmentTowerEffect.setMaxLoad(MaxLoad.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    BigDecimal MaxWindSpeed = new BigDecimal(Double.valueOf((Double) (data.get("maxWindSpeed")==null?0.0:data.get("maxWindSpeed"))));
                    equipmentTowerEffect.setMaxWindSpeed(MaxWindSpeed.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    BigDecimal MinHeight = new BigDecimal(Double.valueOf((Double) (data.get("minHeight")==null?0.0:data.get("minHeight"))));
                    equipmentTowerEffect.setMinHeight(MinHeight.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    equipmentTowerEffect.setLiftingPointRadius(Float.valueOf(data.get("liftingPointRadius")+""));
                    equipmentTowerEffect.setLiftingPointHeight(Float.valueOf(data.get("liftingPointHeight")+""));
                    equipmentTowerEffect.setLiftingPointRadiusUnload(Float.valueOf(data.get("liftingPointRadiusUnload")+""));
                    equipmentTowerEffect.setLiftingPointHeightUnload(Float.valueOf(data.get("liftingPointHeightUnload")+""));
                    dataList.add(equipmentTowerEffect);
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
}

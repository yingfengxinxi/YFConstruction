package com.qdoner.datarecept.service.impl;

import cn.hutool.core.date.DateException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.qdoner.datarecept.commons.EnvMonitorCodeEnum;
import com.qdoner.datarecept.entity.BO.EquipmentElevatorEffectBO;
import com.qdoner.datarecept.entity.PO.BEquipmentElevatorEffect;
import com.qdoner.datarecept.entity.PO.MonitorPointAlert;
import com.qdoner.datarecept.entity.PO.ProjectInfo;
import com.qdoner.datarecept.mapper.BEquipmentElevatorEffectMapper;
import com.qdoner.datarecept.service.IBEquipmentAssetsService;
import com.qdoner.datarecept.service.IBEquipmentElevatorEffectService;
import com.kakarote.core.servlet.BaseServiceImpl;
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
 * 升降机功效数据 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2022-07-21
 */
@Service
public class BEquipmentElevatorEffectServiceImpl extends BaseServiceImpl<BEquipmentElevatorEffectMapper, BEquipmentElevatorEffect> implements IBEquipmentElevatorEffectService {

    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private IBEquipmentAssetsService assetsService;

    @Override
    public Map selectDZTJ(Integer id) {
        Map elevator = assetsService.selectElevatorById(id+"");
        Map map = new HashMap();
        List<Map> list = getBaseMapper().selectZZTJ(id);
        List<Map> listSd = getBaseMapper().selectSDTJ(id);
        List<Map> listGd = getBaseMapper().selectGDTJ(id);
        List<Integer> dates = list.stream().map(x-> (Integer)x.get("date")).collect(Collectors.toList());
        List<Double> zz = list.stream().map(x-> (Double)x.get("val")).collect(Collectors.toList());
        List<Double> sd = listSd.stream().map(x-> (Double)x.get("val")).collect(Collectors.toList());
        List<Double> gd = listGd.stream().map(x->((Double)x.get("val"))).collect(Collectors.toList());
        List<String> zzyj = new ArrayList<>();
        List<String> zzBj = new ArrayList<>();
        List<String> sdyj = new ArrayList<>();
        List<String> sdbj = new ArrayList<>();
        List<String> gdzyj = new ArrayList<>();
        List<String> gdzbj = new ArrayList<>();
        for(Integer i :dates){
            zzyj.add(elevator.get("weightWarningValue")+"");
            zzBj.add(elevator.get("weightAlarmValue")+"");
            sdyj.add(elevator.get("speedWarningValue")+"");
            sdbj.add(elevator.get("speedAlarmValue")+"");
            gdzyj.add(elevator.get("heightWarningValue")+"");
            gdzbj.add(elevator.get("heightAlarmValue")+"");
        }
        map.put("dates",dates);
        map.put("zz",zz);
        map.put("sd",sd);
        map.put("gd",gd);
        map.put("zzyj",zzyj);
        map.put("zzbj",zzBj);
        map.put("sdyj",sdyj);
        map.put("sdbj",sdbj);
        map.put("gdyj",gdzyj);
        map.put("gdbj",gdzbj);
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
        Map<String, Object> runStatusObj = getBaseMapper().selectRunStatus(id);
        if (runStatusObj == null) {
            runStatusObj = new HashMap<>();
        }
        map.putAll(runStatusObj);
        return map;
    }

    @Override
    public Map selectSJXX(String code) {
        return getBaseMapper().selectSJXX(code);
    }

    @Override
    public Result saveData(List<Map<String, Object>> list) {
        try {
            List<BEquipmentElevatorEffect> dataList = new ArrayList();
            List<MonitorPointAlert> alertList = new ArrayList<>();
            if(list != null && list.size()>0){
                for(Map data:list){
                    BEquipmentElevatorEffect equipmentTowerEffect = new BEquipmentElevatorEffect();
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

                    if (data.get("deviceSn") == null || "".equals(data.get("deviceSn"))) {
                        throw new CrmException(500, "设备编号不能为空");
                    }
                    equipmentTowerEffect.setAssetId(Integer.valueOf(data.get("deviceSn") + ""));
                    MonitorPointAlert monitorPointAlert = new MonitorPointAlert();
                    monitorPointAlert.setEquipmentId(equipmentTowerEffect.getAssetId());
                    monitorPointAlert.setProjectId(equipmentTowerEffect.getProjectId());
                    monitorPointAlert.setTenantId(equipmentTowerEffect.getTenantId());
                    if (data.get("runStartTime") == null || "".equals(data.get("runStartTime"))) {
                        throw new CrmException(500, "运行开始时间不能为空");
                    }
                    String regdate=(String)data.get("runStartTime");
                    DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    equipmentTowerEffect.setStartTime(dft.parse(regdate));
                    if (data.get("runEndTime") == null || "".equals(data.get("runEndTime"))) {
                        throw new CrmException(500, "运行结束时间不能为空");
                    }
                    String enddate=(String)data.get("runEndTime");
                    equipmentTowerEffect.setEndTime(dft.parse(enddate));
                    if (data.get("earlyAlarm") == null || "".equals(data.get("earlyAlarm"))) {
                        throw new CrmException(500, "预警码不能为空");
                    }
                    equipmentTowerEffect.setEarlyAlarm((Integer) data.get("earlyAlarm"));
                    if (data.get("alarm") == null || "".equals(data.get("alarm"))) {
                        throw new CrmException(500, "报警码不能为空");
                    }

                    equipmentTowerEffect.setCreateTime(new Date());
                    equipmentTowerEffect.setAlarm((Integer) data.get("alarm"));
                    BigDecimal Weight = new BigDecimal(Double.valueOf( data.get("loadValue")+""));
                    equipmentTowerEffect.setWeight(Weight.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    BigDecimal PercentRate = new BigDecimal(Double.valueOf( data.get("loadValue")+""));
                    equipmentTowerEffect.setPercentRate(PercentRate.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    equipmentTowerEffect.setNumberOfPeople((Integer) data.get("numberOfPeople"));
                    equipmentTowerEffect.setStartFloor((Integer) data.get("startFloor"));
                    equipmentTowerEffect.setEndFloor((Integer) data.get("endFloor"));
                    BigDecimal Wind = new BigDecimal(Double.valueOf(data.get("wind")+""));
                    equipmentTowerEffect.setWind(Wind.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    BigDecimal Speed = new BigDecimal(Double.valueOf( data.get("speed")+""));
                    equipmentTowerEffect.setSpeed(Speed.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    BigDecimal Angle = new BigDecimal(Double.valueOf( data.get("angle")+""));
                    equipmentTowerEffect.setAngle(Angle.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    BigDecimal AngleY = new BigDecimal(Double.valueOf( data.get("angleY")+""));
                    equipmentTowerEffect.setAngleY(AngleY.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    BigDecimal StartHeight = new BigDecimal(Double.valueOf( data.get("startHeight")+""));
                    equipmentTowerEffect.setStartHeight(StartHeight.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    BigDecimal EndHeight = new BigDecimal(Double.valueOf(data.get("endHeight")+""));
                    equipmentTowerEffect.setEndHeight(EndHeight.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    BigDecimal Height = new BigDecimal(Double.valueOf( data.get("height")+""));
                    equipmentTowerEffect.setHeight(Height.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    equipmentTowerEffect.setDirection(data.get("direction")+"");
                    equipmentTowerEffect.setBypass(data.get("bypass")+"");
                    dataList.add(equipmentTowerEffect);

                    //getBaseMapper().insert(equipmentTowerEffect);
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

package com.kakarote.build.home.service;

import com.kakarote.build.equipment.entity.BO.EquipmentAssetsBO;
import com.kakarote.core.entity.BasePage;

import java.util.List;
import java.util.Map;

/**
 * @ClassName HomePageService
 * @Author wnj58
 * @Date 2021/9/16 17:12
 * @purpose
 * @Version 1.0
 **/
public interface HomePageService {
    Map<String,Object> EnvMonitPont();

    List<Map<String,Object>> EquipmentNum();

    Map<String,Object> AlarmsNum(Integer type);

    Map<String,Object> ProjectNum();

    List<Map<String,Object>> EarlyWarning();

    List<Map<String,Object>> ProjectAlarmNum();

    Map<String,Object> MonitSituationNoise(Map<String, Object> params);

    Map<String, Object> MonitSituationPM(String type);

    List<Map<String, Object>> projectInfoList();

    List<Map<String, Object>> projectInfoListByProjectState(Map<String, Object> params);

    List<Map<String, Object>> selectProjectStateCount();

    List<Map<String, Object>> projectInfoListNJ();

    List<Map<String, Object>> getLine(Map<String, Object> params);

    Map<String, Object> getLineNew(Map<String, Object> params);

    Map<String, Object> dayAndNightAlarmNumber(Map<String, Object> params);

    //2022-04-19  查询设备的数据列表
    Map<String, Object> selectEquipmentList(Map<String, Object> params);

    Map<String, Object> alarmNumberByDay();

    List<Map<String,Object>> projectAlarmNumNew();

    List<Map<String,Object>> projectInfoListByAlertType(Map<String, Object> params);

    Map<String, Object> machineEquNumber();

    BasePage<Map<String,Object>> selectEquiListByType(EquipmentAssetsBO assetsBO);

    Map<String, Object> getLineByThreeDimensional(Map<String, Object> params);
}

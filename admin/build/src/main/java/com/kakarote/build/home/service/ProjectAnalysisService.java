package com.kakarote.build.home.service;

import com.kakarote.build.equipment.entity.PO.EquipmentSpraySystem;
import com.kakarote.build.project.entity.BO.ProjectInfoBO;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.core.entity.BasePage;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ProjectAnalysisService
 * @Author wnj58
 * @Date 2021/9/18 15:21
 * @purpose
 * @Version 1.0
 **/
public interface ProjectAnalysisService {
    BasePage<Map> projectList(ProjectInfoBO projectInfoBO);

    Map<String,Object> alarmsStatistics(Map<String,Object> params) ;

    List<Map<String,Object>> alarmsTrend(Map<String,Object> params);

    List<Map<String,Object>> realTimeData(Map<String,Object> params);

    List<Map<String,Object>> hisAirQualityAnalysis(Map<String,Object> params);

    List<Map<String,Object>> sprayRecord(Map<String,Object> params);

    Map<String,Object> projectInfoById(Integer id);

    List<Map<String,Object>> latelyAirQualityAnalysis(Map<String,Object> params);

    Map<String,Object> todayAirQualityAnalysis(Map<String,Object> params);

    List<Map<String,Object>> overView(Map<String,Object> params);

    Map<String,Object> alarmsStatisticsNew(Map<String, Object> params);

    Map<String,Object> todayAirQualityAnalysisNew();

    List<Map<String,Object>> hisAirQualityAnalysisNew();

    List<Map<String,Object>> latelyAirQualityAnalysisNew();
}

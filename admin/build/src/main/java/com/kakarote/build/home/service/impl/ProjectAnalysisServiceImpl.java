package com.kakarote.build.home.service.impl;
/**
 * @ClassName ProjectAnalysisServiceImpl
 * @Author wnj58
 * @Date 2021/9/18 15:21
 * @purpose
 * @Version 1.0
 **/

import com.alibaba.csp.sentinel.util.StringUtil;
import com.kakarote.build.env.service.IEnvLevelService;
import com.kakarote.build.equipment.service.IEquipmentSpraySystemService;
import com.kakarote.build.equipment.service.IMonitorPointAlertService;
import com.kakarote.build.home.service.ProjectAnalysisService;
import com.kakarote.build.project.entity.BO.ProjectInfoBO;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName ProjectAnalysisServiceImpl
 * @Author wnj58
 * @Date 2021/9/18 15:21
 * @purpose
 * @Version 1.0
 **/
@Service
public class ProjectAnalysisServiceImpl implements ProjectAnalysisService {

    @Autowired
    private IProjectInfoService iProjectInfoService;
    //设备报警
    @Autowired
    private IMonitorPointAlertService iMonitorPointAlertService;

    @Autowired
    private IEnvLevelService iEnvLevelService;

    @Autowired
    private IEquipmentSpraySystemService spraySystemService;

    @Override
    public BasePage<Map> projectList(ProjectInfoBO projectInfoBO) {
        projectInfoBO.setPageType(0);
        return iProjectInfoService.queryPageListAll(projectInfoBO);
    }

    @Override
    public Map<String, Object> projectInfoById(Integer id) {
        ProjectInfo projectInfo = iProjectInfoService.getByIdGrp(id);
        Map<String, Object> result = new HashMap<>();
        result.put("totalCost", projectInfo.getTotalCost());
        result.put("plannedCompletionDate", projectInfo.getPlannedCompletionDate()==null?"":new SimpleDateFormat("yyyy-MM-dd").format(projectInfo.getPlannedCompletionDate()));
        result.put("totalArea", projectInfo.getTotalArea());
        result.put("projectAbbreviation", projectInfo.getProjectAbbreviation());
        return result;
    }


    @Override
    public Map<String, Object> alarmsStatistics(Map<String, Object> params) {
        if (params.get("projectId") == null || StringUtil.isBlank(String.valueOf(params.get("projectId")))) {
            throw new CrmException(3001, "项目id不可为空");
        }
        if (params.get("time") == null || "".equals(params.get("time")) || "null".equals(params.get("time"))) {
            params.put("time", new Date());
        }
        return iMonitorPointAlertService.alarmsStatistics(params);
    }

    @Override
    public List<Map<String, Object>> alarmsTrend(Map<String, Object> params) {
        if (params.get("projectId") == null) {
            throw new CrmException(3001, "项目id不可为空");
        }
        if (params.get("type") == null) {
            throw new CrmException(3001, "type值不可为空");
        }
        Map<String, Object> map = null;
        if ("1".equals(String.valueOf(params.get("type")))) {//七天
            map = dateMap("1");
        } else if ("2".equals(String.valueOf(params.get("type")))) {//30天
            map = dateMap("2");
        } else {
            throw new CrmException(3001, "type值错误");
        }
        map.put("projectId", String.valueOf(params.get("projectId")));
        List<Map<String, Object>> list = iMonitorPointAlertService.alarmsTrend(map);
        return list;
    }

    @Override
    public List<Map<String, Object>> realTimeData(Map<String, Object> params) {
        List<Map<String, Object>> list = new ArrayList<>();

        if (params.get("projectId") == null) {
            throw new CrmException(3001, "项目id不可为空");
        }
        if (params.get("type") == null) {
            throw new CrmException(3001, "type值不可为空");
        }
        if (params.get("timeType") == null || params.get("timeType").equals("")) {
            throw new CrmException(3001, "type值不可为空");
        }
        // 查询的时间类型
        String timeType = String.valueOf(params.get("timeType"));
        Map<String, Object> map = dateMap(timeType);

        // 根据类型查询天表或月表
        if (timeType.equals("2")) {
            map.put("tableName", "b_equipment_monitor_data_month");
            map.put("formatStyle", "%m.%d");
        } else if (timeType.equals("4")) {
            map.put("tableName", "b_equipment_monitor_data_day");
            map.put("formatStyle", "%H");
        }
        map.put("projectId", String.valueOf(params.get("projectId")));
        map.put("monitorType", "9");
        if ("1".equals(String.valueOf(params.get("type")))) {//噪音
            map.put("monitorSonType", "3");
            list = iMonitorPointAlertService.realTimeData2(map);
//            list = dataMerge(list);
        } else if ("2".equals(String.valueOf(params.get("type")))) {//空气质量
            //PM2.5
            map.put("monitorSonType", "1");
            List<Map<String, Object>> temp1 = iMonitorPointAlertService.realTimeData2(map);
//            temp1 = dataMerge(temp1);
            //PM10
            map.put("monitorSonType", "2");
            List<Map<String, Object>> temp2 = iMonitorPointAlertService.realTimeData2(map);
//            temp2 = dataMerge(temp2);
            for (int i = 0; i < temp1.size(); i++) {
                Map<String, Object> tempMap1 = temp1.get(i);
                Map<String, Object> tempMap2 = temp2.get(i);
                Map<String, Object> mapp = new HashMap<>();
                mapp.put("date", tempMap1.get("date"));
                mapp.put("hourNumber", tempMap1.get("hourNumber"));
                mapp.put("PM2Count", tempMap1.get("dateCount"));
                mapp.put("PM10Count", tempMap2.get("dateCount"));
                list.add(mapp);
            }
        } else if ("3".equals(String.valueOf(params.get("type")))) {//温湿度
            //温度
            map.put("monitorSonType", "4");
            List<Map<String, Object>> temp1 = iMonitorPointAlertService.realTimeData2(map);
//            temp1 = dataMerge(temp1);
            //湿度
            map.put("monitorSonType", "5");
            List<Map<String, Object>> temp2 = iMonitorPointAlertService.realTimeData2(map);
//            temp2 = dataMerge(temp2);
            for (int i = 0; i < temp1.size(); i++) {
                Map<String, Object> tempMap1 = temp1.get(i);
                Map<String, Object> tempMap2 = temp2.get(i);
                Map<String, Object> mapp = new HashMap<>();
                mapp.put("date", tempMap1.get("date"));
                mapp.put("hourNumber", tempMap1.get("hourNumber"));
                mapp.put("temperatureCount", tempMap1.get("dateCount"));//温度
                mapp.put("humidityCount", tempMap2.get("dateCount"));//湿度
                list.add(mapp);
            }
        }
        return list;
    }

    private List<Map<String, Object>> dataMerge(List<Map<String, Object>> list) {
        double tempCount = 0;
        List<Map<String, Object>> MapList = new ArrayList<>();
        int t = 0;
        for (int i = 0; i < list.size(); i++) {
            t++;
            Map<String, Object> map = list.get(i);
            if (t % 2 == 0) {
                Map<String, Object> mapp = new HashMap<>();
                mapp.put("date", map.get("date"));
                mapp.put("hourNumber", map.get("hourNumber"));
//                mapp.put("dateCount", Integer.valueOf(String.valueOf(map.get("dateCount"))) + tempCount);
                double f1 = new BigDecimal((float)(Double.valueOf(String.valueOf(map.get("dateCount")))+tempCount)/2).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                mapp.put("dateCount", f1);
                MapList.add(mapp);
            } else {
                tempCount = Double.valueOf(String.valueOf(map.get("dateCount")));
            }
        }
        return MapList;
    }

    @Override
    public List<Map<String, Object>> hisAirQualityAnalysis(Map<String, Object> params) {
        if (params.get("projectId") == null) {
            throw new CrmException(3001, "项目id不可为空");
        }
        params.put("monitorType", "9");
        //params.put("monitorSonType", "1");
        params.put("monitorSonType", "2");//改为PM10
//        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> list = iMonitorPointAlertService.hisAirQualityAnalysis(params);
//        EnvLevelBO EnvLevelBO = new EnvLevelBO();
//        EnvLevelBO.setPageType(0);
//        List<EnvLevel> envLevelList = iEnvLevelService.selectByPage(EnvLevelBO).getList();
//        for (EnvLevel EL : envLevelList) {
//            if(list.size()==0){
//                Map<String, Object> mapp = new HashMap<>();
//                mapp.put("levelName", EL.getLevelName());
//                mapp.put("num", 0);
//                result.add(mapp);
//            }else{
//                for (Map<String, Object> map : list) {
//                    if (EL.getLevelName().equals(String.valueOf(map.get("levelName")))) {
//                        result.add(map);
//                        break;
//                    } else {
//                        Map<String, Object> mapp = new HashMap<>();
//                        mapp.put("levelName", EL.getLevelName());
//                        mapp.put("num", 0);
//                        result.add(mapp);
//                    }
//                }
//            }
//        }
        return list;
    }

    @Override
    public List<Map<String, Object>> sprayRecord(Map<String, Object> params) {
        if (params.get("projectId") == null) {
            throw new CrmException(3001, "项目id不可为空");
        }
        params.put("monitorType", "9");
        params.put("monitorSonType", "1");
        return spraySystemService.selectNoPage(params);
    }

    @Override
    public List<Map<String, Object>> latelyAirQualityAnalysis(Map<String, Object> params) {
        if (params.get("projectId") == null) {
            throw new CrmException(3001, "项目id不可为空");
        }
        Map<String, Object> map = dateMap("1");
        map.put("monitorType", "9");
        map.put("monitorSonType", "1");
        map.put("projectId", params.get("projectId"));
        return iMonitorPointAlertService.latelyAirQualityAnalysis(map);
    }

    @Override
    public Map<String, Object> todayAirQualityAnalysis(Map<String, Object> params) {
        if (params.get("projectId") == null) {
            throw new CrmException(3001, "项目id不可为空");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("monitorType", "9");
        map.put("monitorSonType", "1");
        map.put("projectId", params.get("projectId"));
        Map<String, Object> temp1 = iMonitorPointAlertService.todayAirQualityAnalysis(map);//PM2.5
        map.put("monitorSonType", "2");
        Map<String, Object> temp2 = iMonitorPointAlertService.todayAirQualityAnalysis(map);//PM10
        map.put("monitorSonType", "9");
        Map<String, Object> temp3 = iMonitorPointAlertService.todayAirQualityAnalysis(map);//TSP

        Map<String, Object> result = new HashMap<>();
        result.put("date", temp1 == null ? new Date() : temp1.get("date"));
        result.put("PM25AVG", temp1 == null ? "0" : temp1.get("average"));
        result.put("PM25Level", temp1 == null ? "--" : temp1.get("levelName"));
        result.put("PM10AVG", temp2 == null ? "0" : temp2.get("average"));
        result.put("PM10Level", temp2 == null ? "--" : temp2.get("levelName"));
        result.put("TSPAVG", temp3 == null ? "0" : temp3.get("average"));
        result.put("TSPLevel", temp3 == null ? "--" : temp3.get("levelName"));
        return result;
    }

    @Override
    public List<Map<String, Object>> overView(Map<String, Object> params) {


        List<Map<String, Object>> result = iMonitorPointAlertService.overView(params);

        return result;
    }

    @Override
    public Map<String, Object> alarmsStatisticsNew(Map<String, Object> params) {
        if (params.get("projectId") == null || StringUtil.isBlank(String.valueOf(params.get("projectId")))) {
            throw new CrmException(3001, "项目id不可为空");
        }
        if (params.get("time") == null || "".equals(params.get("time")) || "null".equals(params.get("time"))) {
            params.put("time", new Date());
        }
        return iMonitorPointAlertService.alarmsStatisticsNew(params);
    }

    @Override
    public Map<String, Object> todayAirQualityAnalysisNew() {

        Map<String, Object> map = new HashMap<>();
        map.put("monitorType", "9");
        map.put("monitorSonType", "1");
        Map<String, Object> temp1 = iMonitorPointAlertService.todayAirQualityAnalysisNew(map);//PM2.5
        map.put("monitorSonType", "2");
        Map<String, Object> temp2 = iMonitorPointAlertService.todayAirQualityAnalysisNew(map);//PM10
        map.put("monitorSonType", "9");
        Map<String, Object> temp3 = iMonitorPointAlertService.todayAirQualityAnalysisNew(map);//TSP

        Map<String, Object> result = new HashMap<>();
        result.put("date", temp1 == null ? new Date() : temp1.get("date"));
        result.put("PM25AVG", temp1 == null ? "0" : temp1.get("average"));
        result.put("PM25Level", temp1 == null ? "--" : temp1.get("levelName"));
        result.put("PM10AVG", temp2 == null ? "0" : temp2.get("average"));
        result.put("PM10Level", temp2 == null ? "--" : temp2.get("levelName"));
        result.put("TSPAVG", temp3 == null ? "0" : temp3.get("average"));
        result.put("TSPLevel", temp3 == null ? "--" : temp3.get("levelName"));
        return result;
    }

    @Override
    public List<Map<String, Object>> hisAirQualityAnalysisNew() {
        Map<String, Object> params = new HashMap<>();
        params.put("monitorType", "9");
//        params.put("monitorSonType", "1");
        params.put("monitorSonType", "2");//PM10
        List<Map<String, Object>> list = iMonitorPointAlertService.hisAirQualityAnalysisNew(params);
        return list;
    }

    @Override
    public List<Map<String, Object>> latelyAirQualityAnalysisNew() {
        Map<String, Object> map = dateMap("1");
        map.put("monitorType", "9");
//        map.put("monitorSonType", "1");//PM2.5
        map.put("monitorSonType", "2");//PM10
        return iMonitorPointAlertService.latelyAirQualityAnalysisNew(map);
    }


    /**
     * @param type type=1 查询前一周的时间（包括今天）type=2 查询前一月的时间（包括今天）type=3 查询前一年的月份（包括当月） type = 4 最近24小时 (包括当前小时)
     * @return
     */
    private Map<String, Object> dateMap(String type) {
        Map<String, Object> params = new HashMap<>();
        if (type.equals("1")) {
            params.put("format", "%Y-%m-%d");
            params.put("num", "7");
            params.put("dateType", "day");
        } else if (type.equals("2")) {
            params.put("format", "%Y-%m-%d");
            params.put("num", "30");
            params.put("dateType", "day");
        } else if (type.equals("3")) {
            params.put("format", "%Y-%m");
            params.put("num", "12");
            params.put("dateType", "month");
        } else  if (type.equals("4")) {
            params.put("format", "%Y-%m-%d %H");
            params.put("num", "24");
            params.put("dateType", "hour");
        }
        return params;
    }
}

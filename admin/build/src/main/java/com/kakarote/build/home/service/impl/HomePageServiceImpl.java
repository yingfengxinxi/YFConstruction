package com.kakarote.build.home.service.impl;
/**
 * @ClassName HomePageServiceImpl
 * @Author wnj58
 * @Date 2021/9/16 17:12
 * @purpose
 * @Version 1.0
 **/

import com.kakarote.build.equipment.entity.BO.EquipmentAssetsBO;
import com.kakarote.build.equipment.service.IEquipmentAssetsService;
import com.kakarote.build.equipment.service.IEquipmentMonitorPointService;
import com.kakarote.build.equipment.service.IMonitorPointAlertService;
import com.kakarote.build.home.service.HomePageService;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.build.safety.service.ISafetyInspectionDetailService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName HomePageServiceImpl
 * @Author wnj58
 * @Date 2021/9/16 17:12
 * @purpose
 * @Version 1.0
 **/
@Service
public class HomePageServiceImpl implements HomePageService {
    //监测点
    @Autowired
    private IEquipmentMonitorPointService iEquipmentMonitorPointService;

    //设备资产台账
    @Autowired
    private IEquipmentAssetsService iEquipmentAssetsService;

    //设备报警
    @Autowired
    private IMonitorPointAlertService iMonitorPointAlertService;

    //项目
    @Autowired
    private IProjectInfoService iProjectInfoService;

    @Autowired
    private ISafetyInspectionDetailService inspectionDetailService;

    @Override
    public Map<String, Object> EnvMonitPont() {
        Map<String, Object> param =new HashMap<>();
        Map<String, Object> map = iEquipmentMonitorPointService.EnvMonitPont(param);
        return map;
    }

    @Override
    public List<Map<String, Object>> EquipmentNum() {
        List<Map<String, Object>> map = iEquipmentAssetsService.EquipmentNum(new HashMap<>());
        return map;
    }

    @Override
    public Map<String, Object> AlarmsNum(Integer type) {
        Map<String, Object> map = null;
        if (type == 1) {
            map = iMonitorPointAlertService.AlarmsNum_project(map);
        } else if (type == 2) {
            map = iMonitorPointAlertService.AlarmsNum(map);
        } else {
            throw new CrmException(4001, "参数值不正确");
        }
        return map;
    }

    @Override
    public Map<String, Object> ProjectNum() {
        Map<String, Object> map = iProjectInfoService.ProjectNum(new HashMap<>());
        return map;
    }

    @Override
    public List<Map<String, Object>> EarlyWarning() {
        Map<String, Object> map = new HashMap<>();
        map.put("dictType","b_equipment_monitor_type_9");
        map.put("alertType","1");//  1：报警  0：预警
        List<Map<String, Object>> list=  iMonitorPointAlertService.EarlyWarning(map);
//        if(list.size()==0){
//        list=iEquipmentMonitorPointService.EarlyWarningType(map);
//        }
        return list;
    }

    @Override
    public List<Map<String, Object>> ProjectAlarmNum() {
        Map<String, Object> map = new HashMap<>();

        // 查询设备报警数量
        List<Map<String, Object>> alarmNumList = iMonitorPointAlertService.ProjectAlarmNum(map);

        // 查询安全隐患数量
        List<Map<String, Object>> dangerNumList = inspectionDetailService.proDangerNumList();
        Map<Object, Object> dangerNumByProjectId = dangerNumList.stream().collect(Collectors.toMap(a->a.get("projectId"), b->b));

        Map<String, Object> alarmMap = null;
        Map<String, Object> dangerMap = null;
        for (int i=0; i<alarmNumList.size();i++) {
            // 查询项目一直，直接添加安全隐患相关信息
            alarmMap = alarmNumList.get(i);
            dangerMap = (Map<String, Object>) dangerNumByProjectId.get(alarmMap.get("projectId"));
            alarmMap.put("todayDangerNum", dangerMap.get("todayDangerNum"));
            alarmMap.put("totalDangerNum", dangerMap.get("totalDangerNum"));
        }

        return alarmNumList;
    }

    @Override
    public Map<String, Object> MonitSituationNoise(Map<String, Object> params) {
        String type = null;
        if (params.get("type") == null || params.get("type").equals("")) {
            type = "1";
        }
        Map<String, Object> param = dateMap(type);
        param.put("alertType", "1");
        List<Map<String, Object>> map = iMonitorPointAlertService.MonitSituation(param);
        Map<String, Object> result = new HashMap<>();
        List<String> monitorDate = new ArrayList<>();
        List<Integer> dayNum = new ArrayList<>();
        List<Integer> nightNum = new ArrayList<>();
        List<Integer> dayNumProp = new ArrayList<>();
        List<Integer> nightNumProp = new ArrayList<>();
        for (Map<String, Object> mmm : map) {
            Integer TempdayNum = Integer.valueOf(String.valueOf(mmm.get("dayNum")));
            Integer TempnightNum = Integer.valueOf(String.valueOf(mmm.get("nightNum")));
            Integer TempTotalNum = Integer.valueOf(String.valueOf(mmm.get("dayNum"))) + Integer.valueOf(String.valueOf(mmm.get("nightNum")));
            monitorDate.add(String.valueOf(mmm.get("monitorDate")).substring(8,10)+"日");
            dayNum.add(TempdayNum);
            nightNum.add(TempnightNum);
            dayNumProp.add(dataTypeConversion(TempdayNum, TempTotalNum));
            nightNumProp.add(dataTypeConversion(TempnightNum, TempTotalNum));
        }
        result.put("monitorDate", monitorDate);
        result.put("dayNum", dayNum);
        result.put("nightNum", nightNum);
        result.put("dayNumProp", dayNumProp);
        result.put("nightNumProp", nightNumProp);
        return result;
    }

    private static Integer dataTypeConversion(Integer a, Integer b) {
        if(b!=0){
            Double d = Double.valueOf(a) / Double.valueOf(b) * 100;
            d = new BigDecimal(d).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
            return d.intValue();
        }else{
            return 0;
        }
    }

    @Override
    public Map<String, Object> MonitSituationPM(String type) {
        if (type == null || type.equals("")) {
            type = "1";
        }
        Map<String, Object> param = dateMap(type);
        param.put("monitorType", "9");
        param.put("monitorSonType", "1");
        param.put("monitorSonType2", "2");
        List<Map<String, Object>> map = iMonitorPointAlertService.MonitSituationPM(param);

        Map<String, Object> result = new HashMap<>();
        List<String> monitorDate = new ArrayList<>();
        List<Double> PM25Num = new ArrayList<>();
        List<Double> PM10Num = new ArrayList<>();

        for (Map<String, Object> mmm : map) {
            monitorDate.add(String.valueOf(mmm.get("monitorDate")).substring(8,10)+"日");
            PM25Num.add(Double.valueOf(String.valueOf(mmm.get("pm2num"))));
            PM10Num.add(Double.valueOf(String.valueOf(mmm.get("pm10num"))));
        }
        result.put("monitorDate", monitorDate);
        result.put("PM25Num", PM25Num);
        result.put("PM10Num", PM10Num);
        return result;
    }

    @Override
    public List<Map<String, Object>> projectInfoList() {
        return iProjectInfoService.projectInfoList();
    }

    @Override
    public List<Map<String, Object>> projectInfoListByProjectState(Map<String, Object> params) {
        return iProjectInfoService.projectInfoListByProjectState(params);
    }

    @Override
    public List<Map<String, Object>> selectProjectStateCount() {

        List<Map<String, Object>> data = iProjectInfoService.selectProjectStateCount();
        return data;
    }

    @Override
    public List<Map<String, Object>> projectInfoListNJ() {
        return iProjectInfoService.projectInfoListNJ();
    }

    @Override
    public List<Map<String, Object>> getLine(Map<String, Object> params) {
        return iEquipmentAssetsService.getLine(params);
    }

    @Override
    public Map<String, Object> getLineNew(Map<String, Object> params) {
        List<Map<String, Object>> list = iEquipmentAssetsService.getLine(params);

        Map<String, Object> result = new HashMap<>();

        result.put("sjk", list.get(0));
        result.put("tj", list.get(1));
        result.put("sjj", list.get(2));
        result.put("gzm", list.get(3));
        result.put("xlpt", list.get(4));
        result.put("spjk", list.get(5));

        return result;
    }

    @Override
    public Map<String, Object> dayAndNightAlarmNumber(Map<String, Object> params) {
        String type = null;
        if (params.get("type") == null || params.get("type").equals("")) {
            type = "1";
        }else{
            type = String.valueOf(params.get("type"));
        }
        Map<String, Object> param = dateMap(type);
        param.put("alertType", "1");
        List<Map<String, Object>> map = iMonitorPointAlertService.dayAndNightAlarmNumber(param);
        Map<String, Object> result = new HashMap<>();
        List<String> monitorDate = new ArrayList<>();
        List<Integer> dayNum = new ArrayList<>();
        List<Integer> nightNum = new ArrayList<>();
        List<Integer> dayNumProp = new ArrayList<>();
        List<Integer> nightNumProp = new ArrayList<>();
        for (Map<String, Object> mmm : map) {
            Integer TempdayNum = Integer.valueOf(String.valueOf(mmm.get("dayNum")));
            Integer TempnightNum = Integer.valueOf(String.valueOf(mmm.get("nightNum")));
            Integer TempTotalNum = Integer.valueOf(String.valueOf(mmm.get("dayNum"))) + Integer.valueOf(String.valueOf(mmm.get("nightNum")));
            monitorDate.add(String.valueOf(mmm.get("monitorDate")).substring(8,10)+"日");
            dayNum.add(TempdayNum);
            nightNum.add(TempnightNum);
            dayNumProp.add(dataTypeConversion(TempdayNum, TempTotalNum));
            nightNumProp.add(dataTypeConversion(TempnightNum, TempTotalNum));
        }
        result.put("monitorDate", monitorDate);
        result.put("dayNum", dayNum);
        result.put("nightNum", nightNum);
        result.put("dayNumProp", dayNumProp);
        result.put("nightNumProp", nightNumProp);
        return result;
    }

    /**
     * @param type type=1 查询前一周的时间（包括今天）type=2 查询前一月的时间（包括今天）type=3 查询前一年的月份（包括当月）
     * @return
     */
    private Map<String, Object> dateMap(String type) {
        Map<String, Object> params = new HashMap<>();
        System.out.println("type============"+type);
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
        }
        return params;
    }

    //2022-04-19  查询设备的数据列表
    @Override
    public Map<String, Object> selectEquipmentList(Map<String, Object> params) {
        Map<String, Object>list = iEquipmentAssetsService.selectEquipmentList(params);
        return list;
    }

    @Override
    public Map<String, Object> alarmNumberByDay() {
        Map<String, Object> result = iMonitorPointAlertService.alarmNumberByDay();
        return result;
    }

    @Override
    public List<Map<String, Object>> projectAlarmNumNew() {

        // 查询设备报警数量
        List<Map<String, Object>> alarmNumList = iMonitorPointAlertService.projectAlarmNumNew();

        // 查询安全隐患数量
        List<Map<String, Object>> dangerNumList = inspectionDetailService.proDangerNumList();
        Map<Object, Object> dangerNumByProjectId = dangerNumList.stream().collect(Collectors.toMap(a->a.get("projectId"), b->b));

        Map<String, Object> alarmMap = null;
        Map<String, Object> dangerMap = null;
        for (int i=0; i<alarmNumList.size();i++) {
            // 查询项目一直，直接添加安全隐患相关信息
            alarmMap = alarmNumList.get(i);
            dangerMap = (Map<String, Object>) dangerNumByProjectId.get(alarmMap.get("projectId"));
            alarmMap.put("todayDangerNum", dangerMap.get("todayDangerNum"));
            alarmMap.put("totalDangerNum", dangerMap.get("totalDangerNum"));
        }

        return alarmNumList;
    }

    @Override
    public List<Map<String, Object>> projectInfoListByAlertType(Map<String, Object> params) {
        return iProjectInfoService.projectInfoListByAlertType(params);
    }

    @Override
    public Map<String, Object> machineEquNumber() {
        return iEquipmentAssetsService.machineEquNumber();
    }

    @Override
    public BasePage<Map<String, Object>> selectEquiListByType(EquipmentAssetsBO assetsBO) {
        return iEquipmentAssetsService.selectEquiListByType(assetsBO);
    }

    @Override
    public Map<String, Object> getLineByThreeDimensional(Map<String, Object> params) {
        List<Map<String, Object>> list = iEquipmentAssetsService.getLineByThreeDimensional(params);

        Map<String, Object> result = new HashMap<>();

        result.put("sjk", list.get(0));
        result.put("tj", list.get(1));
        result.put("sjj", list.get(2));
        result.put("gzm", list.get(3));

        return result;
    }
}

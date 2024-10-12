package com.kakarote.build.equipment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorDataBO;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorDataEchartsBO;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorDataMonth;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorPoint;
import com.kakarote.build.equipment.mapper.EquipmentMonitorDataMonthMapper;
import com.kakarote.build.equipment.service.IEquipmentMonitorDataMonthService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;


/**
 * <p>
 * 设备监测-监测项月数据表 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-10-28
 */
@Service
public class EquipmentMonitorDataMonthServiceImpl extends BaseServiceImpl<EquipmentMonitorDataMonthMapper, EquipmentMonitorDataMonth> implements IEquipmentMonitorDataMonthService {

    @Autowired
    private EquipmentMonitorPointServiceImpl monitorPointService;

    @Autowired
    private EquipmentMonitorPointServiceImpl equipmentMonitorPointService;

    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public BasePage<Map<String, Object>> selectMonitorDataListByMonth(EquipmentMonitorDataBO monitorDataBO) {
        BasePage<Map<String,Object>> list = getBaseMapper().selectMonitorDataListByMonth(monitorDataBO.parse(), monitorDataBO);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectMonitorDataMonthEcharts(EquipmentMonitorDataEchartsBO monitorDataEchartsBO) {
        // 查询监测点信息
        List<EquipmentMonitorPoint> pointList = monitorPointService.getPointListByIds(monitorDataEchartsBO.getPointIds());

        List<Map<String, Object>> resultList = new ArrayList<>();
        for (EquipmentMonitorPoint monitorPoint:pointList) {

            Map<String, Object> data = new HashMap<>();
            data.put("name", monitorPoint.getPointCode());
            data.put("type", "line");

            // 查询小时表数据
            List<Map<String, Object>> dataList = getBaseMapper().selectMonitorDataMonthByPointId(monitorPoint.getId(), monitorDataEchartsBO);
            List<List<Object>> list = this.generateListData(dataList);
            data.put("data", list);

            resultList.add(data);
        }

        return resultList;
    }

    @Override
    public Map<String, Object> selectRecentData(Map<String, Object> params) {

        if (params.get("type") == null || params.get("type").equals("")) {
            throw new CrmException(500, "参数type不能为空！");
        }

        // 查询近7天数据条件
        Map<String, Object> map = dateMap("1");
        // 环境子类型
        map.put("monitorSonType", params.get("type"));

        List<Map<String, Object>> list = getBaseMapper().selectRecentData(map);

        // 查询对应监测点
        QueryWrapper<EquipmentMonitorPoint> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("monitor_type", "9");
        queryWrapper.eq("monitor_son_type", params.get("type"));
        queryWrapper.last("limit 1");

        EquipmentMonitorPoint point = equipmentMonitorPointService.getOne(queryWrapper);

        // 数据为空的情况下，默认返回100
        if (point == null) {
            point = new EquipmentMonitorPoint();
            point.setAlarmUpper(new BigDecimal(100));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("point", point);
        result.put("list", list);

        return result;
    }

    /**
     * 生成列表数据  echarts图使用
     * @param dataList
     * @return
     */
    private List<List<Object>> generateListData(List<Map<String, Object>> dataList) {
        List<List<Object>> result = new ArrayList<>();

        for (Map<String, Object> map:dataList) {
            List<Object> list = new ArrayList<>();
            list.add(map.get("startTime"));
            list.add(map.get("value"));

            result.add(list);
        }

        return result;
    }

    /**
     * @param type type=1 查询前一周的时间（包括今天）type=2 查询前一月的时间（包括今天）type=3 查询前一年的月份（包括当月）
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
        }
        return params;
    }

    //2022-06-20 日 环境历史监测新增
    @Override
    public Map<String, Object> airQualityMouth(Map<String, Object> params) {

        if (params.get("monthDate") == null || params.get("monthDate").equals("")) {
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            if(month<10){
                params.put("monthDate",year+"-0"+month);
            }else{
                params.put("monthDate",year+"-"+month);
            }

        }
        double pm10_minValue=0;
        double pm10_maxValue=0;
        double pm25_minValue=0;
        double pm25_maxValue=0;
        double tsp_minValue=0;
        double tsp_maxValue=0;
        String goodDate ="暂无";
        String badDate ="暂无";
        List<String> dateList =getMonthDay(params.get("monthDate").toString());
        params.put("tenantId",UserUtil.getUser().getTenantId());
        params.put("projectId",UserUtil.getUser().getProjectId());
        params.put("tenantId",15190);//测试用
        params.put("projectId",51);//测试用
        params.put("monitorSonType","2");
        List <String>  idList= getBaseMapper().pointIdList(params);
        List <String>  PM25List= new ArrayList<>();
        List <String>  PM10List= new ArrayList<>();
        List <String>  TSPList= new ArrayList<>();
        //PM10数据比对查询
        if(idList.size()>0){
            for(int i=0;i<dateList.size();i++){
                Map<String, Object> param = new HashMap<>();
                param.put("date",params.get("monthDate")+"-"+dateList.get(i));
                param.put("ids",idList);
                param.put("tenantId",UserUtil.getUser().getTenantId());
                param.put("projectId",UserUtil.getUser().getProjectId());
                param.put("tenantId",15190);//测试用
                param.put("projectId",51);//测试用
                List<Map<String, Object>> dataList = getBaseMapper().airQualityMouth(param);
                List<Map<String, Object>> dataListLineTable = getBaseMapper().dataListLineTable(param);
                for(int j=0;j<dataList.size();j++){
                    Map map= dataList.get(j);
                    double minValue_bd=Double.valueOf(map.get("minValue").toString());
                    double maxValue_bd=Double.valueOf(map.get("maxValue").toString());
                    if(maxValue_bd>pm10_maxValue){
                        pm10_maxValue = maxValue_bd;
                        badDate = params.get("monthDate")+"-"+dateList.get(i);
                    }
                    if(pm10_minValue ==0){
                        pm10_minValue = minValue_bd;
                        goodDate = dateList.get(i);
                    } else if(pm10_minValue>minValue_bd){
                        pm10_minValue = minValue_bd;
                        goodDate = params.get("monthDate")+"-"+dateList.get(i);
                    }
                }
                if(dataListLineTable.size()>0){
                    if(dataListLineTable.get(0)!=null){
                        PM10List.add(dataListLineTable.get(0).get("pjs").toString());
                    }else{
                        PM10List.add("0");
                    }

                }else{
                    PM10List.add("0");
                }
            }
        }else{
            for(int i=0;i<dateList.size();i++){
                PM10List.add("0");
            }
        }
        params.put("monitorSonType","1");
        idList= getBaseMapper().pointIdList(params);
        //PM2.5数据比对查询
        if(idList.size()>0){
            for(int i=0;i<dateList.size();i++){
                Map<String, Object> param = new HashMap<>();
                param.put("date",params.get("monthDate")+"-"+dateList.get(i));
                param.put("ids",idList);
                param.put("tenantId",UserUtil.getUser().getTenantId());
                param.put("projectId",UserUtil.getUser().getProjectId());
                param.put("tenantId",15190);//测试用
                param.put("projectId",51);//测试用
                List<Map<String, Object>> dataList = getBaseMapper().airQualityMouth(param);
                List<Map<String, Object>> dataListLineTable = getBaseMapper().dataListLineTable(param);
                for(int j=0;j<dataList.size();j++){
                    Map map= dataList.get(j);
                    double minValue_bd=Double.valueOf(map.get("minValue").toString());
                    double maxValue_bd=Double.valueOf(map.get("maxValue").toString());
                    if(maxValue_bd>pm25_maxValue){
                        pm25_maxValue = maxValue_bd;
                    }
                    if(pm25_minValue ==0){
                        pm25_minValue = minValue_bd;
                    } else if(pm25_minValue>minValue_bd){
                        pm25_minValue = minValue_bd;
                    }
                }
                if(dataListLineTable.size()>0){
                    if(dataListLineTable.get(0)!=null){
                        PM25List.add(dataListLineTable.get(0).get("pjs").toString());
                    }else{
                        PM25List.add("0");
                    }
                }else{
                    PM25List.add("0");
                }
            }
        }else{
            for(int i=0;i<dateList.size();i++){
                PM25List.add("0");
            }
        }

        params.put("monitorSonType","9");
        idList= getBaseMapper().pointIdList(params);
        //TSP数据比对查询
        if(idList.size()>0){
            for(int i=0;i<dateList.size();i++){
                Map<String, Object> param = new HashMap<>();
                param.put("date",params.get("monthDate")+"-"+dateList.get(i));
                param.put("ids",idList);
                param.put("tenantId",UserUtil.getUser().getTenantId());
                param.put("projectId",UserUtil.getUser().getProjectId());
                param.put("tenantId",15190);//测试用
                param.put("projectId",51);//测试用
                List<Map<String, Object>> dataList = getBaseMapper().airQualityMouth(param);
                List<Map<String, Object>> dataListLineTable = getBaseMapper().dataListLineTable(param);
                for(int j=0;j<dataList.size();j++){
                    Map map= dataList.get(j);
                    double minValue_bd=Double.valueOf(map.get("minValue").toString());
                    double maxValue_bd=Double.valueOf(map.get("maxValue").toString());
                    if(maxValue_bd>tsp_maxValue){
                        tsp_maxValue = maxValue_bd;
                    }
                    if(tsp_minValue ==0){
                        tsp_minValue = minValue_bd;
                    } else if(tsp_minValue>minValue_bd){
                        tsp_minValue = minValue_bd;
                    }
                }
                if(dataListLineTable.size()>0){
                    if(dataListLineTable.get(0)!=null){
                        TSPList.add(dataListLineTable.get(0).get("pjs").toString());
                    }else{
                        TSPList.add("0");
                    }

                }else{
                    TSPList.add("0");
                }
            }
        }else{
            for(int i=0;i<dateList.size();i++){
                TSPList.add("0");
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("pm10_minValue",pm10_minValue);
        result.put("pm10_maxValue",pm10_maxValue);
        result.put("pm25_minValue",pm25_minValue);
        result.put("pm25_maxValue",pm25_maxValue);
        result.put("tsp_minValue",tsp_minValue);
        result.put("tsp_maxValue",tsp_maxValue);
        result.put("goodDate",goodDate);
        result.put("badDate",badDate);
        result.put("dateList",dateList);
        result.put("PM25List",PM25List);
        result.put("PM10List",PM10List);
        result.put("TSPList",TSPList);
        return result;
    }
    public List<String> getMonthDay(String date) {
        List<String> list = getMonthFullDay(date);
        List<String> listDay = new ArrayList<>();
        for (String date1 : list) {
            listDay.add(date1.substring(8, 10));
        }
        return listDay;
    }

    public List<String> getMonthFullDay(String date) {
        List<String> fullDayList = new ArrayList<String>();
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = 1;// 所有月份从1号开始
        Calendar cal = Calendar.getInstance();// 获得当前日期对象
        cal.clear();// 清除信息
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);// 1月从0开始
        cal.set(Calendar.DAY_OF_MONTH, day);
        int count = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int j = 0; j <= (count - 1); ) {
            if (sdf.format(cal.getTime()).equals(getLastDay(year, month)))
                break;
            cal.add(Calendar.DAY_OF_MONTH, j == 0 ? +0 : +1);
            j++;
            fullDayList.add(sdf.format(cal.getTime()));
        }
        return fullDayList;
    }
    public String getLastDay(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        return sdf.format(cal.getTime());
    }


    //2022-06-24 日 环境历史监测年度新增
    @Override
    public Map<String, Object> yearDataStatistics(Map<String, Object> params) {

        if (params.get("yearDate") == null || params.get("yearDate").equals("")) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            params.put("yearDate",year);
        }
        double optimalMin = 0 ; //优判定最小值
        double optimalMix = 35 ; //优判定最大值
        double goodMin = 35 ; //良判定最小值
        double goodMix = 75 ; //良判定最大值
        double mildMin = 75 ; //轻度污染判定最小值
        double mildMix = 115 ; //轻度污染判定最大值
        double moderateMin = 115 ; //中度污染判定最小值
        double moderateMix = 150 ; //中度污染判定最大值
        double severeMin = 150 ; //重度污染判定最小值
        double severeMix = 250 ; //重度污染判定最大值
        double seriousMin = 250 ; //严重污染判定最小值
        double seriousMix = 9999 ; //严重污染判定最大值

        int PM25Date =0 ; //pm2.5 监测总日期数
        int PM10Date =0 ; //pm10 监测总日期数
        int TSPDate =0 ; //tsp 监测总日期数
        int optimalDate =0; //优 占比总日期数
        int goodDate =0; //良 占比总日期数
        int mildDate =0; //轻度污染 占比总日期数
        int moderateDate =0; //中度污染 占比总日期数
        int severeDate =0; //重度污染 占比总日期数
        int seriousDate =0; //严重污染 占比总日期数
        double PM25AvgData =0 ; //pm2.5 日平均值
        double PM10AvgData =0 ; //pm10 日平均值
        double TSPAvgData =0 ; //tsp 日平均值
        double PM25Data =0 ; //pm2.5 总量值
        double PM10Data =0 ; //pm10 总量值
        double TSPData =0 ; //tsp 总量值
        params.put("ndks",params.get("yearDate")+"-01-01");
        params.put("ndjs",params.get("yearDate")+"-12-31");
        params.put("tenantId",UserUtil.getUser().getTenantId());
        params.put("projectId",UserUtil.getUser().getProjectId());
        params.put("monitorSonType","1");
        List <String>  PM25ids= getBaseMapper().pointIdList(params);
        params.put("PM25ids",PM25ids);

        params.put("monitorSonType","2");
        List PM10ids= getBaseMapper().pointIdList(params);
        params.put("PM10ids",PM10ids);

        params.put("monitorSonType","9");
        List TSPids= getBaseMapper().pointIdList(params);
        params.put("TSPids",TSPids);
        List<Map<String, Object>> standardList = getBaseMapper().standardList(params);
        for(int i=0;i<standardList.size();i++){ //从数据库中取出最新的标准 并更新到对应判定条件中
            Map map = standardList.get(i);
            if("优".equals(map.get("levelName"))){
                 optimalMin = Double.valueOf(map.get("min").toString()) ; //优判定最小值
                 optimalMix = Double.valueOf(map.get("max").toString()) ; //优判定最大值
            }else if("良".equals(map.get("levelName"))){
                goodMin = Double.valueOf(map.get("min").toString()) ; //优判定最小值
                goodMix = Double.valueOf(map.get("max").toString()) ; //优判定最大值
            }else if("轻度污染".equals(map.get("levelName"))){
                mildMin = Double.valueOf(map.get("min").toString()) ; //优判定最小值
                mildMix = Double.valueOf(map.get("max").toString()) ; //优判定最大值
            }else if("中度污染".equals(map.get("levelName"))){
                moderateMin = Double.valueOf(map.get("min").toString()) ; //优判定最小值
                moderateMix = Double.valueOf(map.get("max").toString()) ; //优判定最大值
            }else if("重度污染".equals(map.get("levelName"))){
                severeMin = Double.valueOf(map.get("min").toString()) ; //优判定最小值
                severeMix = Double.valueOf(map.get("max").toString()) ; //优判定最大值
            }else if("严重污染".equals(map.get("levelName"))){
                seriousMin = Double.valueOf(map.get("min").toString()) ; //优判定最小值
                seriousMix = Double.valueOf(map.get("max").toString()) ; //优判定最大值
            }
        }
        List<Map<String, Object>> dataList = getBaseMapper().yearDataStatistics(params);
        for(int i=0;i<dataList.size();i++){
            Map map = dataList.get(i);
            if(map.get("PM25")!=null){
                PM25Date=PM25Date+1; //监测日期加1天
                PM25Data=PM25Data+Double.valueOf(map.get("PM25").toString());//监测总数值累计
            }
            if(map.get("TSP")!=null){
                TSPDate=TSPDate+1; //监测日期加1天
                TSPData=TSPData+Double.valueOf(map.get("TSP").toString());//监测总数值累计
            }
            if(map.get("PM10")!=null){
                double  pm10 =Double.valueOf(map.get("PM10").toString()); //获取pm10监测值
                PM10Date=PM10Date+1; //监测日期加1天
                PM10Data=PM10Data+Double.valueOf(map.get("PM10").toString());//监测总数值累计
                if(pm10>=optimalMin&&optimalMix>pm10){
                    optimalDate=optimalDate+1;
                }else if(pm10>=goodMin&&goodMix>pm10){
                    goodDate=goodDate+1;
                }else if(pm10>=mildMin&&mildMix>pm10){
                    mildDate=mildDate+1;
                }else if(pm10>=moderateMin&&moderateMix>pm10){
                    moderateDate=moderateDate+1;
                }else if(pm10>=severeMin&&severeMix>pm10){
                    severeDate=severeDate+1;
                }else if(pm10>=seriousMin&&seriousMix>pm10){
                    seriousDate=seriousDate+1;
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("optimalPercentage",accountedFor(optimalDate,PM10Date));
        result.put("goodPercentage",accountedFor(goodDate,PM10Date));
        result.put("mildPercentage",accountedFor(mildDate,PM10Date));
        result.put("moderatePercentage",accountedFor(moderateDate,PM10Date));
        result.put("severePercentage",accountedFor(severeDate,PM10Date));
        result.put("seriousPercentage",accountedFor(seriousDate,PM10Date));
        result.put("optimalDate",optimalDate);
        result.put("goodDate",goodDate);
        result.put("mildDate",mildDate);
        result.put("moderateDate",moderateDate);
        result.put("severeDate",severeDate);
        result.put("seriousDate",seriousDate);
        DecimalFormat df = new DecimalFormat("#.00");//对获取后的平均数保留两位小数处理
        if(PM25Data!=0&&PM10Date!=0){
            PM25AvgData =PM25Data/PM10Date ;// 获取平均数
            result.put("PM25AvgData",df.format(PM25AvgData));
        }else{
            result.put("PM25AvgData","0");
        }
        if(PM10Data!=0&&PM10Date!=0){
            PM10AvgData =PM10Data/PM10Date ;// 获取平均数
            result.put("PM10AvgData",df.format(PM10AvgData));
        }else{
            result.put("PM10AvgData","0");
        }
        if(TSPData!=0&&TSPDate!=0){
            TSPAvgData =TSPData/TSPDate ;// 获取平均数
            result.put("TSPAvgData",df.format(TSPAvgData));
        }else{
            result.put("TSPAvgData","0");
        }
        return result;
    }

    public String  accountedFor (int cqrs,int zrs ){ //获取占比
        String result ="0";
        if(cqrs!=0&&zrs!=0){
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
             result = numberFormat.format((float) cqrs / (float) zrs * 100);
        }
        return result;
    }
}

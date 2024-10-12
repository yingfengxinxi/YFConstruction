package com.kakarote.build.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.project.entity.BO.ProgressPlanManageBO;
import com.kakarote.build.project.entity.PO.ProgressPlanDayFillin;
import com.kakarote.build.project.entity.PO.ProgressPlanManageDetails;
import com.kakarote.build.project.mapper.*;
import com.kakarote.build.project.service.*;
import com.kakarote.build.utils.DateUtil;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Title: ProgressPlanDaPingShowServiceImpl
 * @Package com.kakarote.build.project.service.impl
 * @Description: 描述
 * @author: ZangYu
 * @date: 2021-07-14 16:19
 */
@Service
public class ProgressPlanDaPingShowServiceImpl extends BaseServiceImpl<ProgressPlanDaPingShowMapper,Object> implements ProgressPlanDaPingShowService {

    @Autowired
    private ProgressPlanManageDetailsMapper progressPlanManageDetailsMapper;

    @Autowired
    private ProgressPlanMonthDetailsMapper progressPlanMonthDetailsMapper;
    @Autowired
    private IProgressPlanMonthDetailsService progressPlanMonthDetailsService;


    @Autowired
    private IProgressPlanManageService progressPlanManageService;

    @Autowired
    private ProgressPlanDayFillinMapper progressPlanDayFillinMapper;

    @Autowired
    private IProgressPlanModelService progressPlanModelService;


    /**
     * 大屏展示---月计划
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> selectMapForDaPingShowProgressMonth(Map<String, Object> map) throws Exception {
        Map<String, Object> mapReturn = new HashMap<>();

        //获取总计划最近一条计划的mid  projectId ??
        Map mapP = getLastPlanMid(map);
        //返回mid，月份
        Map dateMonth = getDateMonth(mapP);

        dateMonth.put("tableName","b_progress_plan_month_details");

        //1、上方8个数据
        Map map4 = getBaseMapper().allPlan(dateMonth);

        //3、总计划进度--y轴、x轴、实际、计划
        Map map3 = new HashMap();
        if(map4 != null) {
            Map map1 = new HashMap();
            map1.put("mid",dateMonth.get("mid"));
            map1.put("planMonth",dateMonth.get("planMonth"));
            map1.put("startDate",map4.get("startDate"));
            map1.put("endDate",map4.get("endDate"));

            map3 = getMapEchartsMonth(map1);
        }


        mapReturn.put("upDate",map4);
        mapReturn.put("echartsDate",map3);

        return mapReturn;

    }

    /**
     * 大屏展示---总计划
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> selectMapForDaPingShowProgress(Map<String, Object> map) throws Exception {
        Map<String, Object> mapReturn = new HashMap<>();

        //获取最近一条计划  projectId ??
        Map mapP = getLastPlanMid(map);
        mapP.put("tableName","b_progress_plan_manage_details");

        //1、上方8个数据
        Map map4 = getBaseMapper().allPlan(mapP);

        //2、echarts总计划进度--y轴、x轴、实际、计划
        Map map3 = new HashMap();
        if(map4 != null) {
            Map map1 = new HashMap();
            map1.put("mid",mapP.get("mid"));
            map1.put("startDate",map4.get("startDate"));
            map1.put("endDate",map4.get("endDate"));

            map3 = getMapEcharts(map1);
        }

        mapReturn.put("upDate",map4);
        mapReturn.put("echartsDate",map3);

        return mapReturn;
    }


    /**
     * 大屏
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public List<Map> selectByMapForDaPing(Map map) {
        Map lastPlanMid = getLastPlanMid(map);

        List<Map> maps = progressPlanDayFillinMapper.selectByMapForDaPing(lastPlanMid);

        return maps;
    }

    @Override
    public Map<String, Object> selectPlanProjectDuration(Map<String, Object> params) {

        //获取最近一条计划  projectId ??
        Map mapP = getLastPlanMid(params);
        mapP.put("tableName","b_progress_plan_manage_details");

        // 查询数据状态
        Map<String, Object> result = getBaseMapper().selectPlanProjectDuration(mapP);

        return result;
    }

    @Override
    public Map<String, Object> selectPlanProgressList(Map<String, Object> params) {

        Map<String, Object> result = new HashMap<>();
        // 获取最新一条计划
        Map lastPlanMid = getLastPlanMid(params);

        // 查询数据列表
        List<Map<String, Object>> progressList = progressPlanManageDetailsMapper.selectPlanProgressList(lastPlanMid);

        // 统计当前计划id下的模型数量
        Integer modelNumber = progressPlanModelService.selectModelNumberByPlanId(lastPlanMid);

        // 根据计划id查询对应模型数据
        if (modelNumber > 0) {
            List<Map<String, Object>> modelList = null;
            for (Map<String, Object> map:progressList) {
                modelList = progressPlanModelService.selectModelListByPlanId((Integer) map.get("planId"));
                map.put("modelList", modelList);
            }
        }

        result.put("progressList", progressList);
        result.put("modelNumber", modelNumber);

        return result;
    }


    //获取最近一条计划的mid
    public Map getLastPlanMid(Map<String, Object> map){
        QueryWrapper queryWrapper2 = new QueryWrapper();
        queryWrapper2.select("id","mid").orderByDesc("create_time");
        queryWrapper2.last("limit 1");
        Map mapP = progressPlanManageService.getMap(queryWrapper2);

        return  mapP;
    }


    //---------------总计划  开始

    //2、总计划进度--y轴、x轴、实际、计划
    public Map getMapEcharts(Map map) throws Exception{
        Map map3 = new HashMap();

        Integer mid = (Integer)map.get("mid");

        //获取最小开始月份和最大结束月份
        String startDate = (String)map.get("startDate");
        String endDate = (String)map.get("endDate");

        //X轴
        List<String> echartsX = DateUtil.getDateList(startDate,endDate,6);

        ProgressPlanManageBO progressPlanManageBO = new ProgressPlanManageBO();
        progressPlanManageBO.setMid(mid);
        List<Map> maps = progressPlanManageDetailsMapper.selectByMapForSHOU(progressPlanManageBO);
        List<Map> planShouYe = createPlanTree("0",maps);

        List<Map> planShouYe1 = createPlanShouYe(planShouYe);
        List<Map> planShouYe2 = new ArrayList<>();
        List<String> echartsY = new ArrayList<>();//Y轴
        List<Map> echartsActual = new ArrayList<>();//实际
        List<Map> echartsPlan = new ArrayList<>();//计划

        for(Map mapP1 : planShouYe1){
            String ifMilestone = (String) mapP1.get("ifMilestone");
            if(ifMilestone!= null && ifMilestone.equals("Y")){
                planShouYe2.add(mapP1);

                echartsY.add(String.valueOf(mapP1.get("name")));

                Map param1 = new HashMap();
                param1.put("实际开始",mapP1.get("actualStartDate"));
                param1.put("实际结束",mapP1.get("actualEndDate"));
                echartsActual.add(param1);

                Map param2 = new HashMap();
                param2.put("计划开始",mapP1.get("planStartDate"));
                param2.put("计划结束",mapP1.get("planEndDate"));
                echartsPlan.add(param2);

            }

        }

        map3.put("全部",planShouYe2);
        map3.put("X轴",echartsX);
        map3.put("Y轴",echartsY);
        map3.put("实际",echartsActual);
        map3.put("计划",echartsPlan);



        return map3;

    }

    //---------------总计划  结束



    //---------------月计划  开始
    //1最近生成计划的  月份  --大屏月计划
    public Map getDateMonth(Map mapP){
        Integer mid = (Integer)mapP.get("mid");

        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.select("plan_month","mid").orderByDesc("create_time");
        queryWrapper1.eq("mid",mid);
        queryWrapper1.last("limit 1");
        Map map = progressPlanMonthDetailsService.getMap(queryWrapper1);

        return map;
    }

    //3、月计划进度--y轴、x轴、实际、计划
    public Map getMapEchartsMonth(Map map1) throws Exception{
        Map map3 = new HashMap();

        //获取最小开始月份和最大结束月份
        String startDate = (String)map1.get("startDate");
        String endDate = (String)map1.get("endDate");

        //X轴
        List<String> echartsX = DateUtil.getDateList(startDate,endDate,6);


        List<Map> maps = progressPlanMonthDetailsMapper.selectByMapForSHOU(map1);
        List<Map> planShouYe = createPlanTree("0",maps);

        List<Map> planShouYe1 = createPlanShouYe(planShouYe);
        List<Map> planShouYe2 = new ArrayList<>();
        List<String> echartsY = new ArrayList<>();//Y轴
        List<Map> echartsActual = new ArrayList<>();//实际
        List<Map> echartsPlan = new ArrayList<>();//计划



        for(Map mapP1 : planShouYe1){
            String ifMilestone = (String) mapP1.get("ifMilestone");
            if(ifMilestone!= null && ifMilestone.equals("Y")){
                planShouYe2.add(mapP1);

                echartsY.add(String.valueOf(mapP1.get("name")));

                Map param1 = new HashMap();
                param1.put("实际开始",mapP1.get("actualStartDate"));
                param1.put("实际结束",mapP1.get("actualEndDate"));
                echartsActual.add(param1);

                Map param2 = new HashMap();
                param2.put("计划开始",mapP1.get("planStartDate"));
                param2.put("计划结束",mapP1.get("planEndDate"));
                echartsPlan.add(param2);

            }
        }

        map3.put("全部",planShouYe2);
        map3.put("X轴",echartsX);
        map3.put("Y轴",echartsY);
        map3.put("实际",echartsActual);
        map3.put("计划",echartsPlan);

        return map3;

    }


    //---------------月计划  结束


    //-----公用


    //计划生成  table-tree数据
    public List<Map> createPlanTree(String id,List<Map> mapList){
        List<Map> all = new ArrayList<>();

        List<Map> list = new ArrayList<>();
        for(int i = 0; i < mapList.size(); i++){
            String  pmdid = String.valueOf(mapList.get(i).get("pmdid"));
            if(pmdid.equals(id)){
                list.add(mapList.get(i));
            }
        }
        if(list.size()<=0){
            return null;
        }
        for(Map catalog :list){

            List children = createPlanTree((String) catalog.get("mdid"),mapList);
            if(children != null){
                catalog.put("children",children);
            }else{
                catalog.put("children",null);
            }
            all.add(catalog);
        }
        return all;
    }

    //首页 工程进度顺序
    public List<Map> createPlanShouYe(List<Map> mapList){
        List<Map> all = new ArrayList<>();

        for(Map catalog : mapList){
            all.add(catalog);
            if(catalog.get("children") != null){
                List<Map> children = createPlanShouYe((List<Map>) catalog.get("children"));
                for(Map catalog2 : children){
                    all.add(catalog2);
                }
            }
        }

        return all;
    }


}
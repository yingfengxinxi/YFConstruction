package com.kakarote.build.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.equipment.entity.PO.EquipmentAssets;
import com.kakarote.build.project.entity.BO.ProgressPlanManageBO;
import com.kakarote.build.project.entity.PO.ProgressModelset;
import com.kakarote.build.project.entity.PO.ProgressPlanManage;
import com.kakarote.build.project.entity.PO.ProgressPlanManageDetails;
import com.kakarote.build.project.entity.PO.ProgressPlanMonthDetails;
import com.kakarote.build.project.mapper.ProgressPlanManageDetailsMapper;
import com.kakarote.build.project.service.IProgressModelsetService;
import com.kakarote.build.project.service.IProgressPlanManageDetailsService;
import com.kakarote.build.project.service.IProgressPlanManageService;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 进度管理--计划管理--子表 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-05-06
 */
@Service
public class ProgressPlanManageDetailsServiceImpl extends BaseServiceImpl<ProgressPlanManageDetailsMapper, ProgressPlanManageDetails> implements IProgressPlanManageDetailsService {

    @Autowired
    private IProgressPlanManageService progressPlanManageService;


    @Override
    public BasePage<Map> selectByMap(ProgressPlanManageBO progressPlanManageBO) {
        BasePage<Map> mapBasePage = getBaseMapper().selectByMap(progressPlanManageBO.parse(), progressPlanManageBO);
        return mapBasePage;
    }

    @Override
    public List<Map> selectByMapNoPage(ProgressPlanManageBO progressPlanManageBO) {
        List<Map> maps = getBaseMapper().selectByMapNoPage(progressPlanManageBO);
        List<Map> planTree = createPlanTree("0", maps);
        return planTree;
    }

    /**
     * 查询最近生成的计划信息
     * 主表，子表
     * @return
     */
    @Override
    public Map<String, Object> selectPlanLastTime() {
        Map<String, Object> mapAll = new HashMap<>();

        Map map = getLastPlan();

        if(map != null){
            Integer mid = (Integer)map.get("mid");

            ProgressPlanManageBO progressPlanManageBO = new ProgressPlanManageBO();
            progressPlanManageBO.setMid(mid);
            List<Map> maps = getBaseMapper().selectByMapNoPage(progressPlanManageBO);
            List<Map> planTree = createPlanTree("0", maps);

            mapAll.put("ZUB",map);
            mapAll.put("ZIB",planTree);
            return mapAll;
        }else{
            return null;
        }

    }

    /**
     * 首页
     * @return
     */
    @Override
    public Map<String, Object> selectPlanLastTimeSHOUYE() {
        Map<String, Object> mapAll = new HashMap<>();

        Map map = getLastPlan();

        if(map != null){

            Integer mid = (Integer)map.get("mid");

            ProgressPlanManageBO progressPlanManageBO = new ProgressPlanManageBO();
            progressPlanManageBO.setMid(mid);
            List<Map> maps = getBaseMapper().selectByMapForSHOU(progressPlanManageBO);
            List<Map> planShouYe = createPlanTree("0",maps);

            List<Map> planShouYe1 = createPlanShouYe(planShouYe);
            List<Map> planShouYe2 = new ArrayList<>();

            for(Map map1 : planShouYe1){
                String ifMilestone = (String) map1.get("ifMilestone");
                if(ifMilestone!= null && ifMilestone.equals("Y")){
                    planShouYe2.add(map1);
                }
            }
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.select("sum(plan_gqi_number) allDate");
            queryWrapper1.eq("mid",mid);
            queryWrapper1.eq("pmdid",0);
            Map map1 = getMap(queryWrapper1);
            map.put("allDate",map1.get("allDate"));

            mapAll.put("ZUB",map);
            mapAll.put("ZIB",planShouYe2);

            return mapAll;
        }else{
            return null;
        }

    }

    //保存 主表、子表信息
    @Override
    public R saveAndDelete(Map<String, Object> map) throws Exception {

        Map<String, Object> ZHUB = (Map<String, Object>)map.get("ZHUB");
        List<Map<String, Object>> ZIB = (List<Map<String, Object>>)map.get("ZIB");

        ProgressPlanManage  a = JSON.parseObject(JSON.toJSONString(ZHUB), ProgressPlanManage.class);


        QueryWrapper queryWrapper2= new QueryWrapper<>();
        queryWrapper2.eq("mid",a.getMid());
        List<ProgressPlanManageDetails> list = getBaseMapper().selectList(queryWrapper2);

        List<Integer> listIds = new ArrayList<>();

        for(ProgressPlanManageDetails progressPlanManageDetails : list){
            long count = ZIB.stream().filter(down ->
                    (down.get("id") != null && Integer.valueOf(String.valueOf(down.get("id"))).equals(progressPlanManageDetails.getId()) )
            ).count();
            if(count == 0){
                listIds.add(progressPlanManageDetails.getId());
            }
        }

        //保存主表信息
        boolean save = progressPlanManageService.saveOrUpdate(a);

        if(save){
            DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
            List<ProgressPlanManageDetails> lists = new ArrayList<>();

            for(int i = 0; i < ZIB.size(); i++){
                ProgressPlanManageDetails progressPlanManageDetails = JSON.parseObject(JSON.toJSONString(ZIB.get(i)), ProgressPlanManageDetails.class);

                String planStartDate = progressPlanManageDetails.getPlanStartDate();
                String planEndDate = progressPlanManageDetails.getPlanEndDate();

                if((planStartDate != null ) && (planEndDate != null )){
                    if(dft.parse(planEndDate).getTime() < dft.parse(planStartDate).getTime()){
                        throw new CrmException(500,"计划结束时间不能小于计划开始时间");
                    }
                }

                if((planStartDate != null ) && (planEndDate == null )){
                    throw new CrmException(500,"请填写【"+progressPlanManageDetails.getName()+"】的计划结束时间");
                }

                if((planStartDate == null ) && (planEndDate != null )){
                    throw new CrmException(500,"请填写【"+progressPlanManageDetails.getName()+"】的计划开始时间");

                }

                String actualStartDate = progressPlanManageDetails.getActualStartDate();
                String actualEndDate = progressPlanManageDetails.getActualEndDate();

                if((actualStartDate != null ) && (actualEndDate != null )){
                    if(dft.parse(actualEndDate).getTime() < dft.parse(actualStartDate).getTime()){
                        throw new CrmException(500,"实际结束时间不能小于实际开始时间");
                    }
                }

                progressPlanManageDetails.setZhid(a.getId());
//                progressPlanManageDetails.setTenantId(null);

                lists.add(progressPlanManageDetails);
            }

            removeByIds(listIds);

            saveOrUpdateBatch(lists);

        }

        return new R();
    }


    /**
     * 月计划--选择的月份范围 计划开始--结束
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> getMaxMinFormLastPlan(){
        //最近生成计划
        Map lastPlan = getLastPlan();
        if(lastPlan == null) {
            return null;
        }
        //最近生成计划的  计划开始、计划结束、实际开始、实际结束
//        Integer zhid = (Integer)lastPlan.get("id");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("min(plan_start_date) planStart","max(plan_end_date) planEnd","min(actual_start_date) actualStart","max(actual_end_date) actualEnd")
                .eq("zhid",lastPlan.get("id"));

        Map dateStartEnd = getMap(queryWrapper);


        Map maxMin = new HashMap();
        maxMin.put("mid",lastPlan.get("mid"));

        maxMin.put("startDate",dateStartEnd.get("planStart"));
        if(dateStartEnd.get("planStart") != null && dateStartEnd.get("planEnd") == null){
            DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String format = dft.format(date);
            maxMin.put("endDate",format);

        }else{
            maxMin.put("endDate",dateStartEnd.get("planEnd"));
        }

        return maxMin;

    }

    /**
     * 根据模板id及，选择的月份  生成月计划
     * @param map
     * @return
     */
    @Override
    public List<Map> selectByMapForMonthPlan(Map<String, Object> map) {
        String planMonth = (String)map.get("planMonth");
        String lastDayOfMonth = getLastDayOfMonth(planMonth);
        String firstDayOfMonth = planMonth + "-01";

        map.put("firstDayOfMonth",firstDayOfMonth);
        map.put("lastDayOfMonth",lastDayOfMonth);

        //根据模板id、月份，查询子表数据
        List<Map> maps = getBaseMapper().selectByMapForMonthPlan(map);

        //因为根据的月份过滤，所以数据会出现断层，将断层的数据的parentID设置问0
        for(Map map1 : maps){
            String parentId = (String) map1.get("pmdid");
            if(!parentId.equals("0")) {
                List<Map> list = maps.stream().filter(down -> (down.get("mdid").equals(parentId))).collect(Collectors.toList());
                if(list.size()==0){//此数据的父任务不存在此list里面，此数据是断层数据
                    map1.put("parentId","0");
                    map1.put("pmdid","0");
                }
            }

        }
        List<Map> planTree = createPlanTree("0", maps);

        return planTree;
    }

    @Override
    public BasePage<Map<String, Object>> selectByPage(ProgressPlanManageBO planManageBO) {
        BasePage<Map<String, Object>> list = getBaseMapper().selectByPage(planManageBO.parse(), planManageBO);
        return list;
    }

    //计划生成  table-tree数据
    public List<Map> createPlanTree(String id,List<Map> mapList){
        List<Map> all = new ArrayList<>();

        List<Map> list = new ArrayList<>();
//                mapList.stream()
//                        .filter(down -> (down.get("pmdid").equals(id)))
//                        .collect(Collectors.toList());
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
            catalog.remove("tenantId");

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

    //最近生成计划
    public Map getLastPlan(){
        QueryWrapper queryWrapper2 = new QueryWrapper();
        queryWrapper2.select("id","mid").orderByDesc("create_time");
        queryWrapper2.last("limit 1");
        Map mapP = progressPlanManageService.getMap(queryWrapper2);
        return mapP;
    }


    //获取指定月份的最后一天
    public static String getLastDayOfMonth(String yearMonth) {
        int year = Integer.parseInt(yearMonth.split("-")[0]);  //年
        int month = Integer.parseInt(yearMonth.split("-")[1]); //月
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置月份
        // cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.MONTH, month); //设置当前月的上一个月
        // 获取某月最大天数
        //int lastDay = cal.getActualMaximum(Calendar.DATE);
        int lastDay = cal.getMinimum(Calendar.DATE); //获取月份中的最小值，即第一天
        // 设置日历中月份的最大天数
        //cal.set(Calendar.DAY_OF_MONTH, lastDay);
        cal.set(Calendar.DAY_OF_MONTH, lastDay - 1); //上月的第一天减去1就是当月的最后一天
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

}

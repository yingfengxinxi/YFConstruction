package com.kakarote.build.project.service.impl;

import com.kakarote.build.project.entity.PO.ProgressModelsetDetails;
import com.kakarote.build.project.mapper.ProgressModelsetDetailsMapper;
import com.kakarote.build.project.service.IProgressModelsetDetailsService;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 进度管理--模板设置--明细表 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-04-28
 */
@Service
public class ProgressModelsetDetailsServiceImpl extends BaseServiceImpl<ProgressModelsetDetailsMapper, ProgressModelsetDetails> implements IProgressModelsetDetailsService {

    @Override
    public List<ProgressModelsetDetails> queryByPidAndMid(Map<String,Object> map) {

        List<ProgressModelsetDetails> progressModelsetDetails = getBaseMapper().queryByPidAndMid(map);
        return progressModelsetDetails;
    }

    /**
     * 计划生成使用
     * @param map
     * @return
     */
    @Override
    public List<Map> queryMapByPidAndMid(Map<String, Object> map) {
        // 1.无前置节点的：计划开始日期=页面填写的开始日期；计划结束日期=计划开始日期+计划工期天数
        // 2.有前置节点的：计划开始日期=前置节点的计划结束日期；计划结束日期=计划开始日期+计划工期天数

        List<Map> maps = getBaseMapper().queryMapByPidAndMid(map);


        //页面填写的开始日期
        String startDate = (String)map.get("startDate");

        //1、无前置节点
        for(int i = 0 ; i < maps.size() ; i++){
            if(maps.get(i).get("preNode") == null || maps.get(i).get("preNode").equals("")){
                //计划开始日期
                maps.get(i).put("planStartDate",startDate);

                if( maps.get(i).get("planStartDate") != null){
                    //计划工期天数
                    int num = (Integer)maps.get(i).get("planGqiNumber");
                    try {
                        //计划结束日期
                        String date = addDate(startDate,num);
                        maps.get(i).put("planEndDate",date);
                    }catch (Exception e){
                        e.printStackTrace();
                        log.error("打印错误：" + e);
                        e.printStackTrace();
                    }
                }

            }
        }

        //2、有前置节点
        for(int i = 0 ; i < maps.size() ; i++){
            Map map1 = maps.get(i);

            boolean b = map1.get("planStartDate") != null && map1.get("planEndDate") != null;

            if(!b){

                if(map1.get("preNode") != null && !map1.get("preNode").equals("")){

                    //计划开始日期
                    try {
                        String s = preNodeData(map1,maps);
                        map1.put("planStartDate",s);

                        if(map1.get("planStartDate") != null ){
                            //计划工期天数
                            int num = (Integer)maps.get(i).get("planGqiNumber");
                            try {
                                //计划结束日期
                                String date = addDate((String) maps.get(i).get("planStartDate"),num);
                                maps.get(i).put("planEndDate",date);
                            }catch (Exception e){
                                e.printStackTrace();
                                log.error("打印错误：" + e);
                                e.printStackTrace();
                            }
                        }

                    }catch (StackOverflowError e){
                        e.printStackTrace();
                        log.error("前置节点不能嵌套：" + e);
                        throw new CrmException(500,"前置节点不能嵌套，请检查");
//                        e.printStackTrace();


                    }


                }

            }


        }

        List<Map> planTree = createPlanTree("0",maps);

        return planTree;
    }

    @Override
    public List<Map> selectTree(Integer mid) {
        List<ProgressModelsetDetails> list = query().eq("mid",mid)
                .ne("tenant_id",0).ne("project_id",0)//不做租户及项目数据隔离
                .orderByAsc("serial_number").orderByDesc("create_time").list();
        // 设置返回的格式
        String value = "id";
        String label = "name";
        return recursion("0",list,value,label);
    }

    /**
     * 根据当前id删除此数据信息，以及此节点下的子节点信息
     * @param id
     * @return
     */
    @Override
    public Boolean delById(String id) {
        //先查询此数据是否是父节点
        List<ProgressModelsetDetails> list = query().eq("pid", id)
                .ne("tenant_id",0).ne("project_id",0)//不做租户及项目数据隔离
                .list();
        if(list.size() > 0){//是父节点
            for(int i = 0 ; i < list.size(); i++){
                delById(list.get(i).getId());
                removeById(id);
            }
        }else{
            removeById(id);
        }
        return null;
    }

    @Override
    public List<Map> appSelectTree(Integer mid) {
        List<ProgressModelsetDetails> list = query().eq("mid",mid)
                .ne("tenant_id",0).ne("project_id",0)//不做租户及项目数据隔离
                .orderByAsc("serial_number").orderByDesc("create_time").list();
        // 设置返回的格式
        String value = "id";
        String label = "label";
        return recursion("0",list, value, label);
    }

    public List<Map> recursion(String id,List<ProgressModelsetDetails> listAll, String value, String label){
        List<Map> all = new ArrayList<>();
        List<ProgressModelsetDetails> list = listAll.stream().filter(down -> (down.getPid().equals(id))).collect(Collectors.toList());
        if(list.size()<=0){
            return null;
        }
        for(ProgressModelsetDetails catalog :list){
            Map obj = new HashMap();
            obj.put(value,catalog.getId());
            obj.put(label,catalog.getName());

            List children = recursion(catalog.getId(),listAll, value, label);
            if(children != null){
                obj.put("children",children);
            }else{
                obj.put("children",null);
            }
            all.add(obj);
        }
        return  all;
    }

    //日期加上天数，后的日期
    public static String addDate(String date,int dayl) throws ParseException {
        long day = (int)dayl;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date currdate = format.parse(date);

        long time = currdate.getTime(); // 得到指定日期的毫秒数
        day = day*24*60*60*1000; // 要加上的天数转换成毫秒数
        time+=day; // 相加得到新的毫秒数
        Date date1 = new Date(time);// 将毫秒数转换成日期

        String format1 = format.format(date1);
        return format1;
    }

    //计划生成  table-tree数据
    public List<Map> createPlanTree(String id,List<Map> mapList){
        List<Map> all = new ArrayList<>();

        List<Map> list = mapList.stream().filter(down -> (down.get("pmdid").equals(id))).collect(Collectors.toList());
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

    //计划生成  有前置节点的开始日期计算  递归
    public String preNodeData(Map map1,List<Map> mapList){
        String  preNode = (String) map1.get("preNode");

        List<Map> list = mapList.stream().filter(down -> (down.get("mdid").equals(preNode))).collect(Collectors.toList());
        if(list.size()==0){
            return null;
        }

        Map mapA = list.get(0);

        if(mapA.get("planStartDate") == null && mapA.get("planEndDate") == null && (mapA.get("preNode") != null && !mapA.get("preNode").equals(""))){
            String preNode1 = preNodeData(mapA, mapList);

            for(Map map : mapList){
                if(String.valueOf(map.get("mdid")).equals(preNode)){
                    map.put("planStartDate",preNode1);
                    //计划工期天数
                    int num = (Integer)map.get("planGqiNumber");
                    try {
                        //计划结束日期
                        String date = addDate((String) map.get("planStartDate"),num);
                        map.put("planEndDate",date);
                        return date;
                    }catch (Exception e){
                        e.printStackTrace();
                        log.error("打印错误：" + e);
                    }
                }
            }
        }else if(mapA.get("planStartDate") != null && mapA.get("planEndDate") != null){
            return (String) mapA.get("planEndDate");
        }else if(mapA.get("planStartDate") != null && mapA.get("planEndDate") == null){
            for(Map map : mapList){
                if(String.valueOf(map.get("mdid")).equals(String.valueOf(map1.get("mdid")))){

                    //计划工期天数
                    int num = (Integer)map.get("planGqiNumber");
                    try {
                        //计划结束日期
                        String date = addDate((String) map.get("planStartDate"),num);
                        map.put("planEndDate",date);
                        return date;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

        return null;

    }
}

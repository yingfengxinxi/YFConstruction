package com.kakarote.build.project.service.impl;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.project.entity.BO.ProgressPlanMonthBO;
import com.kakarote.build.project.entity.PO.ProgressPlanMonthDetails;
import com.kakarote.build.project.mapper.ProgressPlanMonthDetailsMapper;
import com.kakarote.build.project.service.IProgressPlanMonthDetailsService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 进度管理--月计划明细 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-06-24
 */
@Service
public class ProgressPlanMonthDetailsServiceImpl extends BaseServiceImpl<ProgressPlanMonthDetailsMapper, ProgressPlanMonthDetails> implements IProgressPlanMonthDetailsService {


    //根据月份 查询月计划
    @Override
    public List<Map> selectByMapNoPage(Map map) {
        String planMonth = (String)map.get("planMonth");
        Integer mid = (Integer)map.get("mid");
        QueryWrapper queryWrapper = new QueryWrapper();

        queryWrapper.select("*","case when check_status is null or check_status = 5 then 1 else 0 end editFlag");
        queryWrapper.eq("plan_month", planMonth);
        queryWrapper.eq("mid", mid);
        queryWrapper.orderByAsc("serial_number");
        queryWrapper.orderByDesc("create_time");

        List<Map> list = listMaps(queryWrapper);

        List<Map> planTree = createPlanTree("0", list);
        return planTree;
    }

    @Override
    public void saveOrDelete(List<ProgressPlanMonthDetails> lists) {
        if(lists != null){
            ProgressPlanMonthDetails progressPlanMonthDetails = lists.get(0);
            Integer mid = progressPlanMonthDetails.getMid();
            String planMonth = progressPlanMonthDetails.getPlanMonth();


            QueryWrapper queryWrapper = new QueryWrapper();

            queryWrapper.select("*");
            queryWrapper.eq("plan_month", planMonth);
            queryWrapper.eq("mid", mid);
            queryWrapper.orderByAsc("serial_number");
            queryWrapper.orderByDesc("create_time");

            List<ProgressPlanMonthDetails> listAll = list(queryWrapper);

            List<String> listIds = new ArrayList<>();

            for(ProgressPlanMonthDetails progressPlanMonthDetails1 : listAll){
                long count = lists.stream().filter(down ->
                        (down.getId() != null && down.getId().equals(progressPlanMonthDetails1.getId()))
                ).count();
                if(count == 0){
                    listIds.add(progressPlanMonthDetails1.getId());
                }
            }


            removeByIds(listIds);

            for(ProgressPlanMonthDetails progressPlanMonthDetails1:lists){
                if(progressPlanMonthDetails1.getOwnerUserId() == null){
                    progressPlanMonthDetails1.setOwnerUserId(UserUtil.getUserId());
                }
            }
            saveOrUpdateBatch(lists);
        }
    }

    /**
     * 查询此模板最近生成的计划
     * @param mid
     * @return
     */
    @Override
    public List<Map> selectByLast(Integer mid) {
        List<Map> maps = new ArrayList<>();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("plan_month","mid").orderByDesc("create_time");
        queryWrapper.eq("mid",mid);
        queryWrapper.last("limit 1");
        Map map = getMap(queryWrapper);

        if(map != null){
            maps = selectByMapNoPage(map);
        }

        return maps;
    }

    /**
     * 查询 月计划 id和name 的树形结构 mid 和 月份
     * @param map
     * @return
     */
    @Override
    public List<Map> selectTree(Map map) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mid",map.get("mid"));
        queryWrapper.eq("plan_month",map.get("planMonth"));
        queryWrapper.select("id","name","plan_detail_md_id","parent_id");

        List<Map> list = getBaseMapper().selectMaps(queryWrapper);
        List<Map> recursion = recursion("0", list);

        return recursion;
    }



    /**
     * 根据选择的日期范围  生成周计划
     * @param map
     * @return
     */
    @Override
    public List<Map> selectByMapForWeekPlan(Map<String, Object> map) {

        List<Map> mapsForWeekPlan = getBaseMapper().getMapsForWeekPlan(map);

        //因为根据的月份过滤，所以数据会出现断层，将断层的数据的parentID设置问0
        for(Map map1 : mapsForWeekPlan){
            String parentId = (String) map1.get("parentId");
            if(!parentId.equals("0")) {
                List<Map> list = mapsForWeekPlan.stream().filter(down -> (down.get("planDetailMdId").equals(parentId))).collect(Collectors.toList());
                if(list.size()==0){//此数据的父任务不存在此list里面，此数据是断层数据
                    map1.put("parentId","0");

                }
            }

            String relatedBuildingId = String.valueOf(map1.get("relatedBuildingId"));
            if(relatedBuildingId != null && !relatedBuildingId.equals("") && !relatedBuildingId.equals("null") ){
                String[] split = relatedBuildingId.split(",");
                List<Integer> list1 = new ArrayList<>();
                for(String string : split){
                    Integer integer = Integer.valueOf(string);
                    list1.add(integer);
                }
                map1.put("relatedBuildingId",list1);
            }

        }

        List<Map> planTree = createPlanTree("0", mapsForWeekPlan);

        return planTree;
    }

    /**
     * 查询月计划最新的一条数据的mid--周计划使用
     * @return
     */
    @Override
    public Map selectLastMidFormMonth() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("mid").orderByDesc("create_time");
        queryWrapper.last("limit 1");

        Map map = getMap(queryWrapper);

        return map;
    }

    @Override
    public void exportExcel(Map<String, Object> map, HttpServletResponse response) {

        List<Map> list = getBaseMapper().getMapForExcle(map);

        //将list转化成treelist
        List<Map> treeList = createPlanTreeForExcel("0", list, 0);

        //将treelist 转化成 list
        List<Map> forExcel = createForExcel(treeList);
//        for(Map map1:forExcel){
//            System.out.println((String)map1.get("name"));
//        }

        try (ExcelWriter writer = ExcelUtil.getWriter()) {

            writer.addHeaderAlias("name","任务名称");
            writer.addHeaderAlias("level","等级");
            writer.addHeaderAlias("planGqiNumber","计划工期(天)");
            writer.addHeaderAlias("ifMilestoneLabel","是否里程碑");
            writer.addHeaderAlias("ifCriticalRouteLabel","是否关键路线");
            writer.addHeaderAlias("preNodeLabel","前置节点");
            writer.addHeaderAlias("relatedBuildingIdLabel","关联楼座");
            writer.addHeaderAlias("constructionSite","施工部位");
            writer.addHeaderAlias("planStartDate","计划开始日期");
            writer.addHeaderAlias("planEndDate","计划结束日期");

            writer.addHeaderAlias("actualStartDate","实际开始日期");
            writer.addHeaderAlias("actualEndDate","实际结束日期");
            writer.addHeaderAlias("actualGqiNumber","实际工期(天)");
            writer.addHeaderAlias("delayDays","延误工期(天)");
            writer.addHeaderAlias("designTotal","设计总量");
            writer.addHeaderAlias("unit","计量单位");
            writer.addHeaderAlias("currentMonthPlan","本月计划");
            writer.addHeaderAlias("monthTired","月累完成");
            writer.addHeaderAlias("currentMonthPercent","本月占比%");


            writer.setOnlyAlias(true);
            //水平左对齐，垂直中间对齐
            writer.getStyleSet().setAlign(HorizontalAlignment.LEFT, VerticalAlignment.CENTER);

            writer.write(forExcel, true);

            for(int  i = 0;i <= forExcel.size();i++){
                writer.setRowHeight(i, 30);
            }
            for (int i = 0; i <= 20; i++) {
                writer.setColumnWidth(i, 30);
            }

            Cell cell = writer.getCell(0, 0);
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font font = writer.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            cellStyle.setFont(font);
            cellStyle.setWrapText(true);
            cell.setCellStyle(cellStyle);
            //自定义标题别名
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            String title = "月计划("+(String) map.get("planMonth")+")导出数据";
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(title, "UTF-8")+".xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);

        } catch (Exception e) {
            log.error("导出月计划错误：", e);
        }


    }

    @Override
    public BasePage<Map<String, Object>> selectByPage(ProgressPlanMonthBO planMonthBO) {
        BasePage<Map<String, Object>> list = getBaseMapper().selectByPage(planMonthBO.parse(), planMonthBO);
        return list;
    }


    public List<Map>getMapList(Map map){

        QueryWrapper queryWrapper = new QueryWrapper();

        queryWrapper.select("*");
        queryWrapper.eq("plan_month", map.get("planMonth"));
        queryWrapper.eq("mid", map.get("mid"));
//        queryWrapper.eq("parent_Id",map.get("parentId"));
        queryWrapper.orderByAsc("serial_number");
        queryWrapper.orderByDesc("create_time");

        List<Map> list = listMaps(queryWrapper);
        return  list;
    }

    //计划生成  table-tree数据
    public List<Map> createPlanTree(String id,List<Map> mapList){
        List<Map> all = new ArrayList<>();

        List<Map> list = mapList.stream().filter(down -> (down.get("parentId").equals(id))).collect(Collectors.toList());
        if(list.size()<=0){
            return null;
        }
        for(Map catalog :list){
            catalog.remove("tenantId");
            catalog.put("showPopover",false);

            List children = createPlanTree((String) catalog.get("planDetailMdId"),mapList);
            if(children != null){
                catalog.put("children",children);
            }else{
                catalog.put("children",null);
            }
            all.add(catalog);
        }
        return all;
    }


    public List<Map> recursion(String id,List<Map> listAll){
        List<Map> all = new ArrayList<>();
        List<Map> list = listAll.stream().filter(down -> (down.get("parentId").equals(id))).collect(Collectors.toList());
        if(list.size()<=0){
            return null;
        }
        for(Map catalog :list){
            Map obj = new HashMap();
            obj.put("id",catalog.get("id"));
            obj.put("name",catalog.get("name"));

            List children = recursion((String) catalog.get("planDetailMdId"),listAll);
            if(children != null){
                obj.put("children",children);
            }else{
                obj.put("children",null);
            }
            all.add(obj);
        }
        return  all;
    }


    //导出树形数据  Excel使用
    public static List<Map> createPlanTreeForExcel(String id,List<Map> mapList,Integer i){
        List<Map> all = new ArrayList<>();

        List<Map> list = mapList.stream().filter(down -> (down.get("parentId").equals(id))).collect(Collectors.toList());
        if(list.size()<=0){
            return null;
        }
        for(Map catalog :list){

            catalog.put("name-"+i,catalog.get("name"));
            String a = "";
            for(int i1=1;i1<=i;i1++){
                a = a + " 　";
                if(i1 == i){
                    a = a + "∟";
                }
            }
            catalog.put("name",a+(String)catalog.get("name"));

            List children = createPlanTreeForExcel((String) catalog.get("planDetailMdId"),mapList,i+1);
            if(children != null){
                catalog.put("children",children);
            }else{
                catalog.put("children",null);
            }
            all.add(catalog);
        }
        return all;
    }

    //导出将树形数据转化成list
    public static List<Map> createForExcel(List<Map> mapList){
        List<Map> all = new ArrayList<>();

        for(Map catalog : mapList){

            all.add(catalog);

            if(catalog.get("children") != null){
                List<Map> children = createForExcel((List<Map>) catalog.get("children"));
                for(Map catalog2 : children){
//                    catalog2.remove("children");
                    all.add(catalog2);
                }
            }

        }

        return all;
    }


}

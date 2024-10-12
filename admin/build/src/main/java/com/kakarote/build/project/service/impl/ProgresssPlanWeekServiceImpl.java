package com.kakarote.build.project.service.impl;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.project.entity.BO.ProgresssPlanWeekBO;
import com.kakarote.build.project.entity.PO.ProgresssPlanWeek;
import com.kakarote.build.project.mapper.ProgresssPlanWeekMapper;
import com.kakarote.build.project.service.IProgresssPlanWeekService;
import com.kakarote.core.servlet.BaseServiceImpl;
import java.net.URLEncoder;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 计划管理--周计划 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-07-05
 */
@Service
public class ProgresssPlanWeekServiceImpl extends BaseServiceImpl<ProgresssPlanWeekMapper, ProgresssPlanWeek> implements IProgresssPlanWeekService {

    @Override
    public List<Map> selectByMapNoPage(Map map) {
//        String weekStartDate = (String)map.get("weekStartDate");
//        String weekEndDate = (String)map.get("weekEndDate");
//
//        QueryWrapper queryWrapper = new QueryWrapper();
//
//        queryWrapper.select("*");
//        queryWrapper.eq("week_Start_Date", weekStartDate);
//        queryWrapper.eq("week_end_Date", weekEndDate);
//        queryWrapper.eq("mid", map.get("mid"));
//        queryWrapper.orderByAsc("serial_number");
//        queryWrapper.orderByDesc("create_time");
//
//        List<Map> list = listMaps(queryWrapper);

        List<Map> list = getBaseMapper().selectList(map);

        List<Map> planTree = createPlanTree("0", list);
        return planTree;
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
        queryWrapper.select("week_Start_Date","week_end_Date","mid").orderByDesc("create_time");
        queryWrapper.eq("mid",mid);
        queryWrapper.last("limit 1");
        Map map = getMap(queryWrapper);

        if(map != null){
            maps = selectByMapNoPage(map);
        }

        return maps;
    }

    /**
     * 保存 信息时，检查是否删除
     * @param lists
     * @return
     */
    @Override
    public void saveOrDelete(List<ProgresssPlanWeek> lists) {
        if(lists != null){
            ProgresssPlanWeek progresssPlanWeek = lists.get(0);
            Integer mid = progresssPlanWeek.getMid();
            String weekStartDate = progresssPlanWeek.getWeekStartDate();
            String weekEndDate = progresssPlanWeek.getWeekEndDate();

            Map map = new HashMap();
            map.put("mid",mid);
            map.put("weekStartDate",weekStartDate);
            map.put("weekEndDate",weekEndDate);

            List<Map> listAll = getBaseMapper().selectList(map);

            List<String> listIds = new ArrayList<>();

            for(Map map1 : listAll){
                long count = lists.stream().filter(down ->
                        (down.getId() != null && down.getId().equals(String.valueOf(map1.get("id"))))
                        ).count();
                if(count == 0){
                    listIds.add(String.valueOf(map1.get("id")));
                }
            }


            removeByIds(listIds);

            saveOrUpdateBatch(lists);


        }

    }

    @Override
    public void exportExcel(Map<String, Object> map, HttpServletResponse response) {

        List<Map> list = getBaseMapper().getMapForExcle(map);

        if(list.size()>0){

            String startAndEndDate = String.valueOf(list.get(0).get("weekStartDate"))+"至"+ String.valueOf(list.get(0).get("weekEndDate"));

            //将list转化成treelist
            List<Map> treeList = ProgressPlanMonthDetailsServiceImpl.createPlanTreeForExcel("0", list, 0);

            //将treelist 转化成 list
            List<Map> forExcel = ProgressPlanMonthDetailsServiceImpl.createForExcel(treeList);
//        for(Map map1:forExcel){
//            System.out.println((String)map1.get("name"));
//        }

            try (ExcelWriter writer = ExcelUtil.getWriter()) {

                writer.addHeaderAlias("name","任务名称");
                writer.addHeaderAlias("planGqiNumber","计划工期(天)");
                writer.addHeaderAlias("relatedBuildingIdLabel","关联楼座");
                writer.addHeaderAlias("planStartDate","计划开始日期");
                writer.addHeaderAlias("planEndDate","计划结束日期");
                writer.addHeaderAlias("actualStartDate","实际开始日期");
                writer.addHeaderAlias("actualEndDate","实际结束日期");
                writer.addHeaderAlias("actualGqiNumber","实际工期(天)");
                writer.addHeaderAlias("materialType","材料种类");
                writer.addHeaderAlias("planAmount","计划量");
                writer.addHeaderAlias("actualAmount","实际量");
                writer.addHeaderAlias("unit","单位");


                writer.setOnlyAlias(true);
                //水平左对齐，垂直中间对齐
                writer.getStyleSet().setAlign(HorizontalAlignment.LEFT, VerticalAlignment.CENTER);

                writer.write(forExcel, true);

                for(int  i = 0;i <= forExcel.size();i++){
                    writer.setRowHeight(i, 30);
                }
                for (int i = 0; i <= 12; i++) {
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
                String title = "周计划("+ startAndEndDate +")导出数据";
                response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(title, "UTF-8")+".xls");
                ServletOutputStream out = response.getOutputStream();
                writer.flush(out);

            } catch (Exception e) {
                log.error("导出周计划错误：", e);
            }

        }



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
//            catalog.put("showPopover",false);
            String relatedBuildingId = String.valueOf(catalog.get("relatedBuildingId"));
            if(relatedBuildingId != null && !relatedBuildingId.equals("") && !relatedBuildingId.equals("null") ){
                String[] split = relatedBuildingId.split(",");
                List<Integer> list1 = new ArrayList<>();
                for(String string : split){
                    Integer integer = Integer.valueOf(string);
                    list1.add(integer);
                }
                catalog.put("relatedBuildingId",list1);
            }

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
    @Override
    public List<Map> selectAppList(ProgresssPlanWeekBO progresssPlanWeekBO) {
        List<Map> list = getBaseMapper().selectAppList(progresssPlanWeekBO.parse(),progresssPlanWeekBO);
        return list;
    }

    @Override
    public ProgresssPlanWeek getByIdAsApp(String id) {
        return getBaseMapper().selectById(id);
    }

    @Override
    public List<Map> selectByLastAsApp(Integer mid) {
        List<Map> maps = new ArrayList<>();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("week_Start_Date","week_end_Date","mid").orderByDesc("create_time");
        queryWrapper.eq("mid",mid);
        queryWrapper.last("limit 1");
        Map map = getMap(queryWrapper);

        if(map != null){
            maps = getBaseMapper().selectList(map);
        }
        return maps;
    }

}

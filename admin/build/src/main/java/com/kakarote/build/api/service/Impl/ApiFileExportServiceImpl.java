package com.kakarote.build.api.service.Impl;/**
 * @ClassName ApiFileExportServiceImpl
 * @Author wnj58
 * @Date 2021/9/9 14:11
 * @purpose
 * @Version 1.0
 **/

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.kakarote.build.api.service.ApiFileExportService;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminDictData;
import com.kakarote.core.feign.admin.service.AdminService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ApiFileExportServiceImpl
 * @Author wnj58
 * @Date 2021/9/9 14:11
 * @purpose
 * @Version 1.0
 **/
@Service
public class ApiFileExportServiceImpl implements ApiFileExportService {
    @Autowired
    private AdminService adminService;

    @Override
    public void export(Map<String, Object> param, HttpServletResponse response) {
        if (param == null) {
            throw new CrmException(4031, "请检查入参是否正确");
        }
        if (param.get("result") == null) {
            throw new CrmException(4032, "缺少必填参数result或result为空");
        }
        if (param.get("fieldList") == null) {
            throw new CrmException(4033, "缺少必填参数fieldList或fieldList为空");
        }
        if (param.get("title") == null) {
            throw new CrmException(4034, "缺少必填参数title或title为空");
        }
        List<Map<String, Object>> fieldList = (List) param.get("fieldList");
        List<Map> result = (List) param.get("result");
        Integer titleLevel = 2;//标题级别
        if (param.get("titleLevel") != null && Integer.valueOf(String.valueOf(param.get("titleLevel"))) > 2) {
            titleLevel = Integer.valueOf(String.valueOf(param.get("titleLevel")));
        }
        List<Map> forExcel = FormatData( result , fieldList);

        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            //定义标题
            writer.merge(fieldList.size() - 1, String.valueOf(param.get("title")));//设置主标题
            //处理主标题之外的标题
            TitleResolution(writer, fieldList, titleLevel);
            writer.setOnlyAlias(true);//Excel中只有设置了别名的字段
            if (titleLevel > 2) {
                writer.setCurrentRow(writer.getCurrentRow() + (titleLevel - 2));
            }
            writer.write(forExcel, true);
            for (int i = 0; i <= forExcel.size() + 1; i++) {
                if (i < titleLevel) {
                    //设置标题行固定高度
                    writer.setRowHeight(i, 25);
                } else {
                    //设置数据行高
                    writer.setRowHeight(i, 15);
                }
            }
            for (int i = 0; i <= fieldList.size() - 1; i++) {
                //根据标题字数设置列宽
                writer.setColumnWidth(i, String.valueOf(fieldList.get(i).get("label")).getBytes().length * 2);
            }
            setCellAndFont(writer);
            //自定义标题别名
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            String title = String.valueOf(param.get("title"));
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(title, "UTF-8") + ".xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);

        } catch (Exception e) {
            e.printStackTrace();
            throw new CrmException(5000, "服务器错误");
        }

    }

    /**
     * @param result
     * @param fieldList
     * @return
     */
    private List<Map> FormatData(List<Map> result, List<Map<String, Object>> fieldList) {
        for (Map<String, Object> map : fieldList) {
            if(map.containsKey("dictType")){
                List<AdminDictData> list_dict = adminService.dictType(String.valueOf(map.get("dictType"))).getData();
                for(Map map2:result){
                       String tempValue= String.valueOf(map2.get(map.get("prop")));
                        for(AdminDictData ADD:list_dict){
                                if(ADD.getDictValue().equals(tempValue)){
                                    map2.put(map.get("prop"),ADD.getDictLabel());
                                    break;
                                }
                        }
                }
            }else{
                if(map.containsKey("dictList")){
                    List<Map<String,Object>> dictList=(List)map.get("dictList");
                    for(Map map2:result){
                        String tempValue= String.valueOf(map2.get(map.get("prop")));
                        for(Map<String,Object> ADD:dictList){
                            if(ADD.get("dictValue").equals(tempValue)){
                                map2.put(String.valueOf(map.get("prop")),String.valueOf(ADD.get("dictLabel")));
                                break;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 设置子级标题
     *
     * @param writer
     * @param fieldList
     * @param titleLevel
     */
    private void TitleResolution(ExcelWriter writer, List<Map<String, Object>> fieldList, Integer titleLevel) {
        if (titleLevel == 2) {
            //处理单级表头
            for (Map<String, Object> map : fieldList) {
                writer.addHeaderAlias(String.valueOf(map.get("prop")), String.valueOf(map.get("label")));//设置二级标题
            }
        } else {

            //处理复杂表头共同方法未实现，具体可参考：com.kakarote.build.labour.service.impl.BLabourWageServiceImpl.export 进行定制复杂表头的导出。
        }

    }

    private void recursion(ExcelWriter writer, List<Map<String, Object>> fieldList) {
        for (int i = 0; i < fieldList.size(); i++) {
            Map<String, Object> map = fieldList.get(i);

        }
    }

    /**
     * 设置表格样式及字体
     *
     * @param writer
     */
    private void setCellAndFont(ExcelWriter writer) {
        writer.getStyleSet().setAlign(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);//水平居中，垂直居中
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
    }
}

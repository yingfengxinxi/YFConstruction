package com.kakarote.build.labour.service.impl;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.kakarote.build.labour.entity.BO.LabourOddVisaBO;
import com.kakarote.build.labour.entity.PO.LabourOddVisa;
import com.kakarote.build.labour.mapper.LabourOddVisaMapper;
import com.kakarote.build.labour.service.ILabourOddVisaService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 零工签证 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-06-07
 */
@Service
public class LabourOddVisaServiceImpl extends BaseServiceImpl<LabourOddVisaMapper, LabourOddVisa> implements ILabourOddVisaService {

    @Override
    public BasePage<Map<String, Object>> selectList(LabourOddVisaBO labourOddVisaBO) {
        labourOddVisaBO.setOrdersBd("create_time",false);
        BasePage<Map<String, Object>> mapBasePage = getBaseMapper().selectList(labourOddVisaBO.parse(), labourOddVisaBO);
        return mapBasePage;
    }

    @Override
    public void exportExcel(LabourOddVisaBO labourOddVisaBO, HttpServletResponse response) {
        List<Map> list = getBaseMapper().selectListNoPage(labourOddVisaBO);

        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.addHeaderAlias("recordPeopleName","记录人姓名");
            writer.addHeaderAlias("dispatchTime","派工时间");
            writer.addHeaderAlias("dispatchReason","派工原因");
            writer.addHeaderAlias("dispatchUnitName","派工单位名称");
            writer.addHeaderAlias("dispatchTeamName","派工班组名称");
            writer.addHeaderAlias("dispatchPeopleName","派工人员姓名");
            writer.addHeaderAlias("dispatchText","派工内容");
            writer.addHeaderAlias("visaDepartmentName","签证部门名称");
            writer.addHeaderAlias("responsibleName","责任人姓名");
            writer.addHeaderAlias("workTimeStart","作业开始时间");
            writer.addHeaderAlias("workTimeEnd","作业结束时间");

            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 30);
            writer.setRowHeight(1, 20);
            for (int i = 0; i <= 10; i++) {
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
            String title = "零工签证导出数据";
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(title, "UTF-8")+".xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);

        } catch (Exception e) {
            log.error("导出零工签证错误：", e);
        }
    }
}

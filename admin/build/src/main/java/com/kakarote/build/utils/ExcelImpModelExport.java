package com.kakarote.build.utils;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;

import java.io.*;
import java.net.URLEncoder;

import com.kakarote.core.servlet.upload.UploadConfig;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Title: ExcelImpModelExport
 * @Package com.kakarote.build.utils
 * @Description: excel导入数据模板 导出
 * @author: ZangYu
 * @date: 2021-06-18 16:43
 */
@RestController
@RequestMapping("/excelImpModelExport")
public class ExcelImpModelExport {

    @PostMapping("/excelImpModelExport")
    @ApiOperation("下载excel导入数据模板")
    @OperateLog(detail = "下载excel导入数据模板", types = LogTypes.OTHER, behavior = LogBehavior.SELECT)
    public void excelImpModelExport(@RequestBody Map<String, Object> param, HttpServletResponse response) throws IOException {
        //模板名称
        String excelTitle = (String) param.get("excelTitle");
        //标题
        String title = (String) param.get("title");
        ExcelWriter writer = ExcelUtil.getWriter();

        // 头部信息
        List<String> sheet1List = (List<String>) param.get("colNames");

        //合并单元格，设置标题
        writer.merge(sheet1List.size() - 1, title);
        writer.writeHeadRow(sheet1List);

        // 头部信息 code--监测项历史数据导入模板  有此行
        List<String> sheetCode1List = (List<String>) param.get("colNameCodes");
        if (sheetCode1List != null) {
            writer.writeHeadRow(sheetCode1List);
        }

        // 示例数据
        List<Map> sampleDataList = (List<Map>) param.get("sampleData");
        writer.writeRow(sampleDataList);

        //标题行的高度
        writer.setRowHeight(0, 30);

        for (int i = 0; i < sheet1List.size(); i++) {
            if (sheet1List.get(i).length() <= 6) {
                writer.setColumnWidth(i, 30);
            } else {
                writer.setColumnWidth(i, 60);
            }

        }

        CellStyle headCellStyle = writer.getHeadCellStyle();

        Font font = writer.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        headCellStyle.setFont(font);
        headCellStyle.setWrapText(true);

        //自定义标题别名
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(excelTitle, "UTF-8") + ".xls");

        ServletOutputStream out = response.getOutputStream();

        writer.flush(out);
        // 关闭writer，释放内存
        writer.close();

    }
    //todo 20211128 演示下载环境报告

    @Autowired
    private UploadConfig uploadConfig;

    @PostMapping("/download")
    public HttpServletResponse download(HttpServletResponse response) {
        try {
            Map<String, String> uploadPath = uploadConfig.getLocal().getUploadPath();
            String path = uploadPath.get("0") + "/20211129.xlsx";
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }

}
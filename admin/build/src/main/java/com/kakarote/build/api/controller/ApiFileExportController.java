package com.kakarote.build.api.controller;/**
 * @ClassName ApiFileExport
 * @Author wnj58
 * @Date 2021/9/9 11:37
 * @purpose
 * @Version 1.0
 **/

import com.kakarote.build.api.service.ApiFileExportService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ApiFileExport
 * @Author wnj58
 * @Date 2021/9/9 11:37
 * @purpose
 * @Version 1.0
 **/
@RestController
@RequestMapping("/ApiFile")
public class ApiFileExportController {
    @Autowired
    private  ApiFileExportService  apiFileExportService;


    /**
     * 页面生成要导出的结果集，解析并生成文件返回
     */
    @PostMapping("/export")
    @ApiOperation("列表数据批量导出")
    @OperateLog(detail = "批量导出",types = LogTypes.BUILD,behavior = LogBehavior.EXPORT)
    public void export(@RequestBody Map<String,Object> param, HttpServletResponse response) {
        apiFileExportService.export(param,response);
    }
}

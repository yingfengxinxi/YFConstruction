package com.kakarote.build.api.service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ApiFileExportService
 * @Author wnj58
 * @Date 2021/9/9 14:10
 * @purpose
 * @Version 1.0
 **/
public interface ApiFileExportService {
    void export(Map<String,Object> param, HttpServletResponse response);
}

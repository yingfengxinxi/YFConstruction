package com.kakarote.build.common;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.build.api.common.EnvMonitorCodeEnum;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.build.utils.Sha256Util;
import com.kakarote.core.exception.CrmException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * 数据上传过滤器
 */
@Slf4j
//@WebFilter(filterName = "dataUploadFilter", urlPatterns = {"/SiteDataUpload/*"})
public class DataUploadFilter implements Filter {
    @Autowired
    private IProjectInfoService projectInfoService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("数据上传过滤器 : dataUploadFilter inited ……");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //一、获取参数map
        String params = this.getPostData(request);
        if (StringUtils.isEmpty(params)) {
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_PARAM_FORMAT_ERROR);
        }
        //有序JSON
        JSONObject jsonObject = JSONObject.parseObject(params,JSONObject.class,Feature.OrderedField);
        if (!jsonObject.containsKey("timestamp") || "".equals(jsonObject.get("timestamp"))) {
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_MISSING_PARAM);
        }

        // 验证时间戳与服务器时间的误差 时间戳与服务器时间误差不能超过1分钟
        String timestamp = (String) jsonObject.get("timestamp");
        String currentTime = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        Long min = this.getMin(timestamp, currentTime);
        /*if (min < 0 || min > 1) {
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_TIMESTAMP_TIMEOUT);
        }*/

        // 验证是否缺少必填参数，参数格式是否正确。
        if (!jsonObject.containsKey("appid") || "".equals(jsonObject.get("appid"))) {
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_MISSING_PARAM);
        }

        if (!jsonObject.containsKey("sign") || "".equals(jsonObject.get("sign"))) {
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_MISSING_PARAM);
        }

        if (!jsonObject.containsKey("data") || "".equals(jsonObject.get("data"))) {
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_MISSING_PARAM);
        }
        //二、 判断项目id是否存在 获取项目
        LambdaQueryWrapper<ProjectInfo> projectQueryWrapper = new LambdaQueryWrapper<>();
        projectQueryWrapper.eq(ProjectInfo::getAppid, jsonObject.get("appid"));
        projectQueryWrapper.ge(ProjectInfo::getTenantId, "0");
        projectQueryWrapper.ge(ProjectInfo::getProjectId, "0");
        List<ProjectInfo> proList = projectInfoService.list(projectQueryWrapper);

        if (proList == null || proList.size() <= 0) {
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_PROJECT_NOT_EXIST);
        }
        ProjectInfo projectInfo = proList.get(0);
        String appsecret = projectInfo.getAppsecret();
        //三、校验签名
        // 生成签名
        String sign = this.generateSign(jsonObject, appsecret);
        // System.out.println("==="+sign);
        // 验证签名
        if (!sign.equals(jsonObject.get("sign"))) {
            // 签名校验失败
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_SIGN_VERIFY);
        }
        //执行
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        log.info("DataUploadFilter destroyed …");
    }

    //生成签名
    private String generateSign(JSONObject jsonObject, String appsecret) {

        // 获取map的所有键值
        List<String> keys = new ArrayList<>();
        for (String key : jsonObject.keySet()) {
            keys.add(key);
        }
        String paramsStr = "";
        for (String key : keys) {
            if (!key.equals("sign")) {
                String str = key + '=' + jsonObject.get(key) + "&";
                paramsStr += str;
            }
        }
        // 拼接appsecret
        paramsStr += "appsecret=" + appsecret;
        // 生成签名
        String sign = Sha256Util.getSHA256StrJava(paramsStr);
        return sign;
    }

    /**
     * 获取参数
     *
     * @param request
     * @return
     */
    private String getPostData(HttpServletRequest request) {
        StringBuffer data = new StringBuffer();
        String line;
        BufferedReader reader;
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine()))
                data.append(line);
        } catch (IOException e) {
        } finally {
        }
        return data.toString();
    }

    /**
     * 获取系统时间和时间戳的分钟误差
     *
     * @param sdate 传输过来的时间戳
     * @param edate 系统当前时间
     * @return
     */
    private Long getMin(String sdate, String edate) {

        Date start = DateUtil.parse(sdate, "yyyyMMddHHmmss");
        Date end = DateUtil.parse(edate, "yyyyMMddHHmmss");

        long between = (end.getTime() - start.getTime()) / 1000;
        long min = between / 60;

        return min;

    }
}

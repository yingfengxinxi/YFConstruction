package com.kakarote.build.project.service;

import com.kakarote.build.project.entity.PO.ProgressPlanDayFillin;
import com.kakarote.build.project.entity.PO.ProgressPlanManageDetails;
import com.kakarote.build.project.mapper.ProgressPlanDaPingShowMapper;
import com.kakarote.core.servlet.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Title: ProgressPlanDaPingShowService
 * @Package com.kakarote.build.project.service
 * @Description: 描述
 * @author: ZangYu
 * @date: 2021-07-14 16:17
 */
public interface ProgressPlanDaPingShowService extends BaseService<Object> {

    //大屏展示--月计划
    public Map<String, Object> selectMapForDaPingShowProgressMonth(Map<String,Object>map) throws Exception;

    //大屏展示--总计划
    public Map<String, Object> selectMapForDaPingShowProgress(Map<String,Object>map) throws Exception;


    //大屏展示--每日填报
    List<Map> selectByMapForDaPing(Map map);

    Map<String, Object> selectPlanProjectDuration(Map<String, Object> params);

    Map<String, Object> selectPlanProgressList(Map<String, Object> params);
}

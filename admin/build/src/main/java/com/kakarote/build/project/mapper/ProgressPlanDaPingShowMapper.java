package com.kakarote.build.project.mapper;

import com.kakarote.build.project.entity.PO.ProgressPlanManageDetails;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @Title: ProgressPlanDaPingShowMapper
 * @Package com.kakarote.build.project.mapper
 * @Description: 描述
 * @author: ZangYu
 * @date: 2021-07-16 16:28
 */
public interface ProgressPlanDaPingShowMapper extends BaseMapper<Object> {

    //总计划
    Map allPlan(@Param("data") Map map);

    Map<String, Object> selectPlanProjectDuration(@Param("data") Map mapP);
}

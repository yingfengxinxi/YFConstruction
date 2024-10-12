package com.kakarote.build.project.mapper;

import com.kakarote.build.project.entity.BO.ProgressPlanMonthBO;
import com.kakarote.build.project.entity.BO.ProgressPlanManageBO;
import com.kakarote.build.project.entity.PO.ProgressPlanMonthDetails;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 进度管理--月计划明细 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-06-24
 */
public interface ProgressPlanMonthDetailsMapper extends BaseMapper<ProgressPlanMonthDetails> {

    //根据时间范围及mid 查询生成周计划
    List<Map> getMapsForWeekPlan(@Param("data")Map map);

    //导出月计划Excel使用
    List<Map> getMapForExcle(@Param("data")Map map);

    BasePage<Map<String, Object>> selectByPage(BasePage<Object> parse, @Param("data") ProgressPlanMonthBO planMonthBO);


    //大屏展示
    List<Map> selectByMapForSHOU(@Param("data") Map map);
}

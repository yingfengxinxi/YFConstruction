package com.kakarote.build.project.mapper;

import com.kakarote.build.project.entity.BO.ProgressPlanManageBO;
import com.kakarote.build.project.entity.PO.ProgressPlanManageDetails;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 进度管理--计划管理--子表 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-05-06
 */
public interface ProgressPlanManageDetailsMapper extends BaseMapper<ProgressPlanManageDetails> {

    BasePage<Map> selectByMap(BasePage<ProgressPlanManageBO> page, @Param("data") ProgressPlanManageBO progressPlanManageBO);

    List<Map> selectByMapNoPage(@Param("data") ProgressPlanManageBO progressPlanManageBO);

    //首页
    List<Map> selectByMapForSHOU(@Param("data") ProgressPlanManageBO progressPlanManageBO);

    //根据选择的月份  生成月计划
    List<Map> selectByMapForMonthPlan(@Param("data") Map<String,Object>map);

    BasePage<Map<String, Object>> selectByPage(BasePage<Object> parse, @Param("data") ProgressPlanManageBO planManageBO);

    List<Map<String, Object>> selectPlanProgressList(@Param("data") Map lastPlanMid);
}

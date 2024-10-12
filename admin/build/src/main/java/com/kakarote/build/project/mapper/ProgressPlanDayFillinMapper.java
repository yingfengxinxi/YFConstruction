package com.kakarote.build.project.mapper;

import com.kakarote.build.project.entity.PO.ProgressPlanDayFillin;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 计划管理---每日填报 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-07-16
 */
public interface ProgressPlanDayFillinMapper extends BaseMapper<ProgressPlanDayFillin> {

    List<Map> selectByMap(@Param("data")Map map);

    //大屏展示
    List<Map> selectByMapForDaPing(@Param("data")Map map);
}

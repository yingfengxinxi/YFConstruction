package com.kakarote.build.project.mapper;

import com.kakarote.build.project.entity.BO.ProgresssPlanWeekBO;
import com.kakarote.build.project.entity.PO.ProgresssPlanWeek;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 计划管理--周计划 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-07-05
 */
public interface ProgresssPlanWeekMapper extends BaseMapper<ProgresssPlanWeek> {


    List<Map> selectList(@Param("data") Map map);

    //导出周计划Excel使用
    List<Map> getMapForExcle(@Param("data")Map map);

    List<Map> selectAppList(BasePage<Object> parse,@Param("data") ProgresssPlanWeekBO progresssPlanWeekBO);
}

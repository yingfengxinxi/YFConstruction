package com.kakarote.build.project.mapper;

import com.kakarote.build.project.entity.BO.ProgressPlanMonthBO;
import com.kakarote.build.project.entity.PO.ProgressPlanMonthEquipmentLease;
import com.kakarote.build.project.entity.PO.ProgressPlanMonthLabor;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 计划管理--月计划--月劳务明细 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-06-29
 */
public interface ProgressPlanMonthLaborMapper extends BaseMapper<ProgressPlanMonthLabor> {

    BasePage<Map<String,Object>> selectList(BasePage<ProgressPlanMonthLabor> page, @Param("data") ProgressPlanMonthBO progressPlanMonthBO);

}

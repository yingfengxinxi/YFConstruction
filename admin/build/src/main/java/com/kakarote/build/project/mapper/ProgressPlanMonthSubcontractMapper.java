package com.kakarote.build.project.mapper;

import com.kakarote.build.project.entity.BO.ProgressPlanMonthBO;
import com.kakarote.build.project.entity.PO.ProgressPlanMonthMaterial;
import com.kakarote.build.project.entity.PO.ProgressPlanMonthSubcontract;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 计划管理--月计划--分包明细 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-06-29
 */
public interface ProgressPlanMonthSubcontractMapper extends BaseMapper<ProgressPlanMonthSubcontract> {

    BasePage<Map<String,Object>> selectList(BasePage<ProgressPlanMonthSubcontract> page, @Param("data") ProgressPlanMonthBO progressPlanMonthBO);

}

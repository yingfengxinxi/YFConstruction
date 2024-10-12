package com.kakarote.build.project.service;

import com.kakarote.build.project.entity.BO.ProgressPlanMonthBO;
import com.kakarote.build.project.entity.PO.ProgressPlanMonthLabor;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * 计划管理--月计划--月劳务明细 服务类
 * </p>
 *
 * @author zy
 * @since 2021-06-29
 */
public interface IProgressPlanMonthLaborService extends BaseService<ProgressPlanMonthLabor> {

    public BasePage<Map<String,Object>> selectList(ProgressPlanMonthBO progressPlanMonthBO);

}

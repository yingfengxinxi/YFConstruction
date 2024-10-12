package com.kakarote.build.project.service;

import com.kakarote.build.project.entity.BO.ProgressPlanMonthBO;
import com.kakarote.build.project.entity.PO.ProgressPlanMonthTurnmatLea;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * 计划管理--月计划--周转材租赁明细 服务类
 * </p>
 *
 * @author zy
 * @since 2021-06-29
 */
public interface IProgressPlanMonthTurnmatLeaService extends BaseService<ProgressPlanMonthTurnmatLea> {

    public BasePage<Map<String,Object>> selectList(ProgressPlanMonthBO progressPlanMonthBO);

}

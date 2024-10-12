package com.kakarote.build.project.service;

import com.kakarote.build.project.entity.BO.ProgressPlanMonthBO;
import com.kakarote.build.project.entity.PO.ProgressPlanMonthSubcontract;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * 计划管理--月计划--分包明细 服务类
 * </p>
 *
 * @author zy
 * @since 2021-06-29
 */
public interface IProgressPlanMonthSubcontractService extends BaseService<ProgressPlanMonthSubcontract> {

    public BasePage<Map<String,Object>> selectList(ProgressPlanMonthBO progressPlanMonthBO);

}

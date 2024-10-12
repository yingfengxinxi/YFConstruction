package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.QualPlanBO;
import com.kakarote.build.qual.entity.PO.QualPlan;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 质量方案 服务类
 * </p>
 *
 * @author wudw
 * @since 2021-03-22
 */
public interface IQualPlanService extends BaseService<QualPlan> {

    BasePage<QualPlan> selectByPage(QualPlanBO planBO);

}

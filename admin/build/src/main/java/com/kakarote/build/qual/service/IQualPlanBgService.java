package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.QualPlanBgBO;
import com.kakarote.build.qual.entity.PO.QualPlanBg;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 质量方案变更 服务类
 * </p>
 *
 * @author wudw
 * @since 2021-03-23
 */
public interface IQualPlanBgService extends BaseService<QualPlanBg> {

    BasePage<QualPlanBg> selectByPage(QualPlanBgBO planBgBO);
}

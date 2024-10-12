package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.QualDigitInfoBO;
import com.kakarote.build.qual.entity.BO.QualPlanBO;
import com.kakarote.build.qual.entity.PO.QualDigitInfo;
import com.kakarote.build.qual.entity.PO.QualPlan;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 数字质量资料 服务类
 * </p>
 *
 * @author pz
 * @since 2021-03-22
 */
public interface IQualDigitInfoService extends BaseService<QualDigitInfo> {
    BasePage<QualDigitInfo> selectByPage(QualDigitInfoBO qualDigitInfoBO);
}

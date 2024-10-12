package com.kakarote.build.project.service;

import com.kakarote.build.project.entity.BO.ProgressSelfEvaluateYearBO;
import com.kakarote.build.project.entity.PO.ProgressSelfEvaluateYear;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 年度自评 服务类
 * </p>
 *
 * @author shz
 * @since 2022-06-17
 */
public interface IProgressSelfEvaluateYearService extends BaseService<ProgressSelfEvaluateYear> {
    BasePage<ProgressSelfEvaluateYearBO> selectList(ProgressSelfEvaluateYearBO selfEvaluateBO);

    List<ProgressSelfEvaluateYearBO> getEvaluateList(String id);

    Map getPrintData(String id);
}

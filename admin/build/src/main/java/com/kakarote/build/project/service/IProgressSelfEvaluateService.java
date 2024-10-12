package com.kakarote.build.project.service;

import com.kakarote.build.project.entity.BO.ProgressSelfEvaluateBO;
import com.kakarote.build.project.entity.PO.ProgressSelfEvaluate;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 月度自评 服务类
 * </p>
 *
 * @author shz
 * @since 2022-06-07
 */
public interface IProgressSelfEvaluateService extends BaseService<ProgressSelfEvaluate> {

    BasePage<ProgressSelfEvaluateBO> selectList(ProgressSelfEvaluateBO selfEvaluateBO);

    List<ProgressSelfEvaluateBO> getEvaluateList(String id);

    Map getPrintData(String id);
}

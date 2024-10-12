package com.kakarote.build.eval.service;

import com.kakarote.build.eval.entity.BO.EvalApplyBO;
import com.kakarote.build.eval.entity.PO.EvalApply;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 智慧工地评价-评估打分申请表 服务类
 * </p>
 *
 * @author lzy
 * @since 2022-02-23
 */
public interface IEvalApplyService extends BaseService<EvalApply> {

    String saveApplyAndApplyItemData(Map<String, Object> params);

    Map<String, Object> selectDataById(String id);

    BasePage<EvalApply> selectByPage(EvalApplyBO evalApplyBO);

    String gradeOrHold(Map<String, Object> params);

    BasePage<EvalApply> selectRankingList(EvalApplyBO evalApplyBO);

    Map<String, Object> selectExportDataById(String id);

    Map<String, Object> getInfoById(String id);

    List<Map<String, Object>> getItemList(Map<String, Object> params);

    String updateInfoAndGrade(EvalApply evalApply);
}

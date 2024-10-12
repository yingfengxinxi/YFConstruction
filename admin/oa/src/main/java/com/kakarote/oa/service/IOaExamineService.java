package com.kakarote.oa.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.oa.entity.BO.AuditExamineBO;
import com.kakarote.oa.entity.BO.ExamineExportBO;
import com.kakarote.oa.entity.BO.ExaminePageBO;
import com.kakarote.oa.entity.BO.GetExamineFieldBO;
import com.kakarote.oa.entity.PO.OaExamine;
import com.kakarote.oa.entity.PO.OaExamineCategory;
import com.kakarote.oa.entity.PO.OaExamineField;
import com.kakarote.oa.entity.VO.ExamineVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 审批表 服务类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface IOaExamineService extends BaseService<OaExamine> {

    BasePage<ExamineVO> myInitiate(ExaminePageBO examinePageBO);

    BasePage<ExamineVO> myOaExamine(ExaminePageBO examinePageBO);

    List<OaExamineField> getField(GetExamineFieldBO getExamineFieldBO);

    void setOaExamine(JSONObject jsonObject);

    void oaExamine(AuditExamineBO auditExamineBO);

    ExamineVO queryOaExamineInfo(String examineId);

    JSONObject getOaExamineInfo(Long tenantId, String examineId);

    JSONObject queryExamineRecordList(String recordId);

    List<JSONObject> queryExamineLogList(Integer recordId);

    void deleteOaExamine(Integer examineId);

    OaExamineCategory queryExaminStep(String categoryId);

    List<Map<String, Object>> export(ExamineExportBO examineExportBO, OaExamineCategory category, List<OaExamineField> fieldList);

    public List<ExamineVO> transfer(List<ExamineVO> recordList);

    BasePage<Map<String, Object>> queryMoney(Integer customerId, PageEntity pageEntity);

    BasePage<Map<String, Object>> contractQueryMoney(Integer contractId, PageEntity pageEntity);

    BasePage<Map<String, Object>> businessQueryMoney(Integer businessId, PageEntity pageEntity);
}

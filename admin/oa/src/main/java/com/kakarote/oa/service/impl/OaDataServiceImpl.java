package com.kakarote.oa.service.impl;

import com.kakarote.core.common.Result;
import com.kakarote.oa.entity.PO.*;
import com.kakarote.oa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OaDataServiceImpl implements IOaDataService
{
    @Autowired
    IOaLogRuleService oaLogRuleService;

    @Autowired
    IOaCalendarTypeService oaCalendarTypeService;

    @Autowired
    IOaExamineCategoryService oaExamineCategoryService;

    @Autowired
    IOaExamineFieldService oaExamineFieldService;

    @Autowired
    IOaExamineStepService oaExamineStepService;
    @Override
    public Result initData(Integer tenantId) {
        //3、日志规则：oa_log_rule、日程类型oa_calendar_type
        List<OaLogRule> logRuleList = oaLogRuleService.list();
        for(OaLogRule oaLogRule:logRuleList){
            oaLogRule.setTenantId(tenantId);
        }
        oaLogRuleService.saveBatch(logRuleList);
        List<OaCalendarType> calendarTypes = oaCalendarTypeService.list();
        for(OaCalendarType calendarType:calendarTypes){
            calendarType.setTenantId(tenantId);
        }
        oaCalendarTypeService.saveBatch(calendarTypes);
        //办公审批：oa_examine_category, oa_examine_field, oa_examine_step
        List<OaExamineCategory> examineCategories = oaExamineCategoryService.list();
        for(OaExamineCategory oaExamineCategory:examineCategories){
            Integer oldCid = oaExamineCategory.getCategoryId();
            oaExamineCategory.setTenantId(tenantId);
            oaExamineCategoryService.save(oaExamineCategory);
            //oa_examine_field
            List<OaExamineField> examineFields = oaExamineFieldService.query()
                    .eq("examine_category_id",oldCid).list();
            for(OaExamineField examineField:examineFields){
                examineField.setTenantId(tenantId);
                examineField.setExamineCategoryId(oaExamineCategory.getCategoryId());
            }
            oaExamineFieldService.saveBatch(examineFields);
            //oa_examine_step
            List<OaExamineStep> examineSteps = oaExamineStepService.query()
                    .eq("category_id",oldCid).list();
            for(OaExamineStep examineStep:examineSteps){
                examineStep.setTenantId(tenantId);
                examineStep.setCategoryId(oaExamineCategory.getCategoryId());
            }
            oaExamineStepService.saveBatch(examineSteps);
        }
        return Result.ok();
    }
}

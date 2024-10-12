package com.kakarote.build.project.service;

import com.kakarote.build.equipment.entity.PO.EquipmentAssets;
import com.kakarote.build.project.entity.BO.ProgressPlanManageBO;
import com.kakarote.build.project.entity.PO.ProgressPlanManageDetails;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 进度管理--计划管理--子表 服务类
 * </p>
 *
 * @author zy
 * @since 2021-05-06
 */
public interface IProgressPlanManageDetailsService extends BaseService<ProgressPlanManageDetails> {

    public BasePage<Map> selectByMap(ProgressPlanManageBO progressPlanManageBO);

    public List<Map> selectByMapNoPage(ProgressPlanManageBO progressPlanManageBO);

    public Map<String, Object> selectPlanLastTime();

    public Map<String, Object> selectPlanLastTimeSHOUYE();

    public R saveAndDelete(Map<String,Object>map) throws Exception;


    //月计划--从最近生成计划的 计划开始、计划结束 获取最小开始月份和最大结束月份
    public Map<String, Object> getMaxMinFormLastPlan() ;

    //根据选择的月份  生成月计划
    public List<Map>selectByMapForMonthPlan(Map<String,Object>map);

    // 分页查询数据
    BasePage<Map<String, Object>> selectByPage(ProgressPlanManageBO planManageBO);
}

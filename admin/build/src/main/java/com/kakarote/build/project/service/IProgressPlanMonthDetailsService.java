package com.kakarote.build.project.service;

import com.kakarote.build.labour.entity.BO.LabourOddVisaBO;
import com.kakarote.build.project.entity.BO.ProgressPlanManageBO;
import com.kakarote.build.project.entity.BO.ProgressPlanMonthBO;
import com.kakarote.build.project.entity.PO.ProgressPlanMonthDetails;
import com.kakarote.build.project.entity.PO.ProgresssPlanWeek;
import com.kakarote.core.common.R;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 进度管理--月计划明细 服务类
 * </p>
 *
 * @author zy
 * @since 2021-06-24
 */
public interface IProgressPlanMonthDetailsService extends BaseService<ProgressPlanMonthDetails> {

    public List<Map> selectByMapNoPage(Map map);

    public void saveOrDelete(List<ProgressPlanMonthDetails> lists);

    //查询此模板最近生成的计划
    public List<Map> selectByLast(Integer mid);

    //查询 月计划 id和name 的树形结构
    public List<Map> selectTree(Map map);



    //根据选择的日期  生成周计划
    public List<Map>selectByMapForWeekPlan(Map<String,Object>map);

    //查询月计划最新的一条数据的mid--周计划使用
    public Map selectLastMidFormMonth();


    public void exportExcel(Map<String,Object>map, HttpServletResponse httpServletResponse);


    BasePage<Map<String, Object>> selectByPage(ProgressPlanMonthBO planMonthBO);
}

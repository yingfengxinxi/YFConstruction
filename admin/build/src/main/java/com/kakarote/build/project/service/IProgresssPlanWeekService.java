package com.kakarote.build.project.service;

import com.kakarote.build.project.entity.BO.ProgresssPlanWeekBO;
import com.kakarote.build.project.entity.PO.ProgresssPlanWeek;
import com.kakarote.core.servlet.BaseService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 计划管理--周计划 服务类
 * </p>
 *
 * @author zy
 * @since 2021-07-05
 */
public interface IProgresssPlanWeekService extends BaseService<ProgresssPlanWeek> {

    public List<Map> selectByMapNoPage(Map map);

    //查询此模板最近生成的计划
    public List<Map> selectByLast(Integer mid);

    public void saveOrDelete(List<ProgresssPlanWeek> lists);

    public void exportExcel(Map<String,Object>map, HttpServletResponse httpServletResponse);


    List<Map> selectByLastAsApp(Integer mid);


    List<Map> selectAppList(ProgresssPlanWeekBO progresssPlanWeekBO);

    ProgresssPlanWeek getByIdAsApp(String id);
}

package com.kakarote.build.project.service;

import com.kakarote.build.project.entity.PO.ProgressPlanDayFillin;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 计划管理---每日填报 服务类
 * </p>
 *
 * @author zy
 * @since 2021-07-16
 */
public interface IProgressPlanDayFillinService extends BaseService<ProgressPlanDayFillin> {

    List<Map> selectByMap(Map map)throws Exception;

}

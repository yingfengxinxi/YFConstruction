package com.kakarote.build.project.service;

import com.kakarote.build.project.entity.PO.ProgressPlanModel;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 计划模型关联表 服务类
 * </p>
 *
 * @author lzy
 * @since 2022-04-18
 */
public interface IProgressPlanModelService extends BaseService<ProgressPlanModel> {

    List<Map<String, Object>> selectModelListByPlanId(Integer id);

    Integer selectModelNumberByPlanId(Map lastPlanMid);
}

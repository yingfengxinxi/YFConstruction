package com.kakarote.build.project.service.impl;

import com.kakarote.build.project.entity.PO.ProgressPlanModel;
import com.kakarote.build.project.mapper.ProgressPlanModelMapper;
import com.kakarote.build.project.service.IProgressPlanModelService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 计划模型关联表 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2022-04-18
 */
@Service
public class ProgressPlanModelServiceImpl extends BaseServiceImpl<ProgressPlanModelMapper, ProgressPlanModel> implements IProgressPlanModelService {

    @Override
    public List<Map<String, Object>> selectModelListByPlanId(Integer id) {
        List<Map<String, Object>> result = getBaseMapper().selectModelListByPlanId(id);
        return result;
    }

    @Override
    public Integer selectModelNumberByPlanId(Map lastPlanMid) {
        return getBaseMapper().selectModelNumberByPlanId(lastPlanMid);
    }
}

package com.kakarote.build.project.service.impl;

import com.kakarote.build.project.entity.BO.ProgressPlanMonthBO;
import com.kakarote.build.project.entity.PO.ProgressPlanMonthMaterial;
import com.kakarote.build.project.mapper.ProgressPlanMonthMaterialMapper;
import com.kakarote.build.project.service.IProgressPlanMonthMaterialService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 计划管理--月计划--材料明细 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-06-29
 */
@Service
public class ProgressPlanMonthMaterialServiceImpl extends BaseServiceImpl<ProgressPlanMonthMaterialMapper, ProgressPlanMonthMaterial> implements IProgressPlanMonthMaterialService {

    @Override
    public BasePage<Map<String, Object>> selectList(ProgressPlanMonthBO progressPlanMonthBO) {
        BasePage<Map<String, Object>> mapBasePage = getBaseMapper().selectList(progressPlanMonthBO.parse(), progressPlanMonthBO);
        return mapBasePage;
    }
}

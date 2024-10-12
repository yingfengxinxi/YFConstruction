package com.kakarote.build.qual.service.impl;

import com.kakarote.build.qual.entity.BO.QualPlanBgBO;
import com.kakarote.build.qual.entity.PO.QualPlanBg;
import com.kakarote.build.qual.mapper.QualPlanBgMapper;
import com.kakarote.build.qual.service.IQualPlanBgService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 质量方案变更 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-03-23
 */
@Service
public class QualPlanBgServiceImpl extends BaseServiceImpl<QualPlanBgMapper, QualPlanBg> implements IQualPlanBgService {
    @Override
    public BasePage<QualPlanBg> selectByPage(QualPlanBgBO planBgBO){
        planBgBO.setOrdersBd("a.create_time",false);
        return this.getBaseMapper().selectByPage(planBgBO.parse(),planBgBO);
    }
}

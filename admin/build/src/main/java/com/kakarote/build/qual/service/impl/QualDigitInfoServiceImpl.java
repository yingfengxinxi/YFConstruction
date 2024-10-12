package com.kakarote.build.qual.service.impl;

import com.kakarote.build.qual.entity.BO.QualDigitInfoBO;
import com.kakarote.build.qual.entity.BO.QualPlanBO;
import com.kakarote.build.qual.entity.PO.QualDigitInfo;
import com.kakarote.build.qual.entity.PO.QualPlan;
import com.kakarote.build.qual.mapper.QualDigitInfoMapper;
import com.kakarote.build.qual.service.IQualDigitInfoService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数字质量资料 服务实现类
 * </p>
 *
 * @author pz
 * @since 2021-03-22
 */
@Service
public class QualDigitInfoServiceImpl extends BaseServiceImpl<QualDigitInfoMapper, QualDigitInfo> implements IQualDigitInfoService {

    @Override
    public BasePage<QualDigitInfo> selectByPage(QualDigitInfoBO bo){
        return this.getBaseMapper().selectByPage(bo.parse(),bo);
    }
}

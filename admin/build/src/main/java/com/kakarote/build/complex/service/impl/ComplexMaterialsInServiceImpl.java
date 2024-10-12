package com.kakarote.build.complex.service.impl;

import com.kakarote.build.complex.entity.BO.ComplexMaterialsInBO;
import com.kakarote.build.complex.entity.PO.ComplexMaterialsIn;
import com.kakarote.build.complex.mapper.ComplexMaterialsInMapper;
import com.kakarote.build.complex.service.IComplexMaterialsInService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 施工综合--物料进场 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-06-16
 */
@Service
public class ComplexMaterialsInServiceImpl extends BaseServiceImpl<ComplexMaterialsInMapper, ComplexMaterialsIn> implements IComplexMaterialsInService {

    @Override
    public BasePage<Map<String, Object>> selectList(ComplexMaterialsInBO complexMaterialsInBO) {
        complexMaterialsInBO.setOrdersBd("create_time",false);
        return getBaseMapper().selectList(complexMaterialsInBO.parse(),complexMaterialsInBO);
    }
}

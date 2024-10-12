package com.kakarote.build.complex.service.impl;

import com.kakarote.build.complex.entity.BO.ComplexMaterialsInBO;
import com.kakarote.build.complex.entity.PO.ComplexMaterialsOut;
import com.kakarote.build.complex.mapper.ComplexMaterialsOutMapper;
import com.kakarote.build.complex.service.IComplexMaterialsOutService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 施工综合--物料出场 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-06-16
 */
@Service
public class ComplexMaterialsOutServiceImpl extends BaseServiceImpl<ComplexMaterialsOutMapper, ComplexMaterialsOut> implements IComplexMaterialsOutService {

    @Override
    public BasePage<Map<String, Object>> selectList(ComplexMaterialsInBO complexMaterialsInBO) {
        complexMaterialsInBO.setOrdersBd("a.create_time",false);
        return getBaseMapper().selectList(complexMaterialsInBO.parse(),complexMaterialsInBO);
    }
}

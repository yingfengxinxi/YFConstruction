package com.kakarote.build.labour.service.impl;

import com.kakarote.build.labour.entity.BO.LabourSubContractorBO;
import com.kakarote.build.labour.entity.PO.LabourSubcontractor;
import com.kakarote.build.labour.mapper.LabourSubcontractorGrpMapper;
import com.kakarote.build.labour.service.ILabourSubcontractorService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 劳务管理--分包商 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-03-15
 */
@Service
public class LabourSubcontractorServiceImpl extends BaseServiceImpl<LabourSubcontractorGrpMapper, LabourSubcontractor> implements ILabourSubcontractorService {

    @Override
    public BasePage<Map<String, Object>> selectList(LabourSubContractorBO subContractorBO) {
        List a = getBaseMapper().seee();
        BasePage list = getBaseMapper().selectList(subContractorBO.parse(), subContractorBO);
        return list;
    }

    @Override
    public List<LabourSubcontractor> selcetIdsAndName() {
        List<LabourSubcontractor> lists = getBaseMapper().selcetIdsAndName();
        return lists;
    }

    @Override
    public boolean removeByIdNew(Integer id) {
        int result = getBaseMapper().removeByIdNew(id);
        boolean b = false;
        if (result > 0) {
            b = true;
        }
        return b;
    }
    public List<LabourSubcontractor> selcetCorpCodeData(Map<String, Object> param){
        List<LabourSubcontractor> list =  getBaseMapper().selcetCorpCodeData(param);
        return list;
    }
}

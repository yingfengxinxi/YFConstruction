package com.kakarote.build.labour.service.impl;

import com.kakarote.build.labour.entity.BO.LabourSafetyOfficerBO;
import com.kakarote.build.labour.entity.PO.LabourSafetyOfficer;
import com.kakarote.build.labour.mapper.LabourSafetyOfficerMapper;
import com.kakarote.build.labour.service.ILabourSafetyOfficerService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 证件管理--安全从业人员证件台账 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-11-10
 */
@Service
public class LabourSafetyOfficerServiceImpl extends BaseServiceImpl<LabourSafetyOfficerMapper, LabourSafetyOfficer> implements ILabourSafetyOfficerService {

    @Override
    public BasePage<Map> getList(LabourSafetyOfficerBO labourSafetyOfficerBO) {
        labourSafetyOfficerBO.setOrdersBd("a.create_time",false);
        BasePage<Map> list=getBaseMapper().getList(labourSafetyOfficerBO.parse(),labourSafetyOfficerBO);
        return list;
    }

    @Override
    public Map getInfoById(Integer id) {
        return getBaseMapper().getInfoById(id);
    }

    @Override
    public BasePage<Map> getListNew(LabourSafetyOfficerBO labourSafetyOfficerBO) {
        labourSafetyOfficerBO.setOrdersBd("a.create_time",false);
        BasePage<Map> list=getBaseMapper().getListNew(labourSafetyOfficerBO.parse(),labourSafetyOfficerBO);
        return list;
    }
}

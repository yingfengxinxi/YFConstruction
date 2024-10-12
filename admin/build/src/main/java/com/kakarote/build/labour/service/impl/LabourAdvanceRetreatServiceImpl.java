package com.kakarote.build.labour.service.impl;

import com.kakarote.build.labour.entity.BO.LabourAdvanceRetreatBO;
import com.kakarote.build.labour.entity.PO.LabourAdvanceRetreat;
import com.kakarote.build.labour.mapper.LabourAdvanceRetreatMapper;
import com.kakarote.build.labour.service.ILabourAdvanceRetreatService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 人员进场退场管理 服务实现类
 * </p>
 *
 * @author shz
 * @since 2022-04-20
 */
@Service
public class LabourAdvanceRetreatServiceImpl extends BaseServiceImpl<LabourAdvanceRetreatMapper, LabourAdvanceRetreat> implements ILabourAdvanceRetreatService {

    @Override
    public BasePage<Map<String, Object>> selectPageList(LabourAdvanceRetreatBO advanceRetreatBO) {
        return getBaseMapper().selectPageList(advanceRetreatBO.parse(),advanceRetreatBO);
    }
}

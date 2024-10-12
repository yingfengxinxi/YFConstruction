package com.kakarote.build.equipment.service.impl;

import com.kakarote.build.equipment.entity.BO.EquipmentPitReportBO;
import com.kakarote.build.equipment.mapper.EquipmentPitReportMapper;
import com.kakarote.build.equipment.service.IEquipmentPitReportService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.qdoner.build.equipment.entity.PO.EquipmentPitReport;



import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shz
 * @since 2023-05-07
 */
@Service
public class EquipmentPitReportServiceImpl extends BaseServiceImpl<EquipmentPitReportMapper, EquipmentPitReport> implements IEquipmentPitReportService {

    @Override
    public BasePage<EquipmentPitReport> selectPage(EquipmentPitReportBO pitReportBO) {
        pitReportBO.setOrdersBd("a.create_time",false);
        return getBaseMapper().selectPage(pitReportBO.parse(),pitReportBO);
    }
}

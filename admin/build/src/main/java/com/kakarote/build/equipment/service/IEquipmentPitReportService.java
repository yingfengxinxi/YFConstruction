package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.BO.EquipmentPitReportBO;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import com.qdoner.build.equipment.entity.PO.EquipmentPitReport;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shz
 * @since 2023-05-07
 */
public interface IEquipmentPitReportService extends BaseService<EquipmentPitReport> {

    BasePage<EquipmentPitReport> selectPage(EquipmentPitReportBO pitReportBO);

}

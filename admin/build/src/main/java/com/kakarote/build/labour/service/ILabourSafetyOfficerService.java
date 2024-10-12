package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.BO.LabourSafetyOfficerBO;
import com.kakarote.build.labour.entity.PO.LabourSafetyOfficer;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * 证件管理--安全从业人员证件台账 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-11-10
 */
public interface ILabourSafetyOfficerService extends BaseService<LabourSafetyOfficer> {

    BasePage<Map> getList(LabourSafetyOfficerBO labourSafetyOfficerBO);

    Map getInfoById(Integer id);

    BasePage<Map> getListNew(LabourSafetyOfficerBO labourSafetyOfficerBO);
}

package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.BO.LabourAdvanceRetreatBO;
import com.kakarote.build.labour.entity.PO.LabourAdvanceRetreat;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * 人员进场退场管理 服务类
 * </p>
 *
 * @author shz
 * @since 2022-04-20
 */
public interface ILabourAdvanceRetreatService extends BaseService<LabourAdvanceRetreat> {

    BasePage<Map<String, Object>> selectPageList(LabourAdvanceRetreatBO advanceRetreatBO);

}

package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.BO.LabourConstructionUnitBO;
import com.kakarote.build.labour.entity.PO.LabourConstructionUnit;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 建设单位管理 服务类
 * </p>
 *
 * @author shz
 * @since 2022-04-20
 */
public interface ILabourConstructionUnitService extends BaseService<LabourConstructionUnit> {

    BasePage<Map<String, Object>> selectPageList(LabourConstructionUnitBO constructionUnitBO);
}

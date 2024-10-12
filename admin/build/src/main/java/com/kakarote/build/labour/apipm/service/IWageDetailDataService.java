package com.kakarote.build.labour.apipm.service;

import com.kakarote.build.labour.entity.PO.BLabourWageDetail;
import com.kakarote.core.common.Result;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工资管理详情 服务类
 * </p>
 *
 * @author author
 * @since 2021-04-26
 */
public interface IWageDetailDataService extends BaseService<BLabourWageDetail> {

    public Result wageDetailDataAdd(Map<String, Object> parpam);
}

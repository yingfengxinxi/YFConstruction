package com.kakarote.build.labour.service;


import com.kakarote.build.labour.entity.BO.BLabourWageDetailBO;
import com.kakarote.build.labour.entity.PO.BLabourWageDetail;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 工资管理详情 服务类
 * </p>
 *
 * @author author
 * @since 2021-04-26
 */
public interface IBLabourWageDetailService extends BaseService<BLabourWageDetail> {



    public BasePage<BLabourWageDetail> selectList(BLabourWageDetailBO bLabourWageDetailBO);
    public List<BLabourWageDetail> selectByPid(String pid);
    public String  deleteByPid(String pid);

    public String  selectCumulativeBalance(BLabourWageDetail bLabourWageDetail);
    public BLabourWageDetail  selectSumPid(String pid);
}

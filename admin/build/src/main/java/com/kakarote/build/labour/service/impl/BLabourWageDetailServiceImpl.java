package com.kakarote.build.labour.service.impl;

import com.kakarote.build.labour.entity.BO.BLabourWageDetailBO;
import com.kakarote.build.labour.entity.PO.BLabourWageDetail;
import com.kakarote.build.labour.mapper.BLabourWageDetailMapper;
import com.kakarote.build.labour.service.IBLabourWageDetailService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 工资管理详情 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-04-26
 */
@Service
public class BLabourWageDetailServiceImpl extends BaseServiceImpl<BLabourWageDetailMapper, BLabourWageDetail> implements IBLabourWageDetailService {


    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BLabourWageDetail> selectList(BLabourWageDetailBO bLabourWageDetailBO)
    {
        BasePage<BLabourWageDetail> adminMessageBasePage = getBaseMapper().selectDataList(bLabourWageDetailBO.parse(),bLabourWageDetailBO);
        return adminMessageBasePage;
    }

    @Override
    public List<BLabourWageDetail> selectByPid(String pid){
        List<BLabourWageDetail> list =  getBaseMapper().selectByPid(pid);
         return list;
    }

    @Override
    public String  deleteByPid(String pid){
        getBaseMapper().deleteByPid(pid);
        return "";
    }
    public String  selectCumulativeBalance(BLabourWageDetail bLabourWageDetail){

        return getBaseMapper().selectCumulativeBalance(bLabourWageDetail);
    }

    public BLabourWageDetail  selectSumPid(String pid){
        return getBaseMapper().selectSumPid(pid);
    }
}

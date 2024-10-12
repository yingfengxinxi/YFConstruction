package com.kakarote.build.qual.service.impl;

import com.kakarote.build.qual.entity.BO.BQualMarkingroomMonitoringBO;
import com.kakarote.build.qual.entity.PO.BQualMarkingroomMonitoring;
import com.kakarote.build.qual.mapper.BQualMarkingroomMonitoringMapper;
import com.kakarote.build.qual.service.IBQualMarkingroomMonitoringService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标养室监测信息 服务实现类
 * </p>
 *
 * @author zhang
 * @since 2022-07-14
 */
@Service
public class BQualMarkingroomMonitoringServiceImpl extends BaseServiceImpl<BQualMarkingroomMonitoringMapper, BQualMarkingroomMonitoring> implements IBQualMarkingroomMonitoringService {

    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BQualMarkingroomMonitoring> selectDataList(BQualMarkingroomMonitoringBO bQualMarkingroomMonitoringBO)
    {
        BasePage<BQualMarkingroomMonitoring> adminMessageBasePage = getBaseMapper().selectDataList(bQualMarkingroomMonitoringBO.parse(),bQualMarkingroomMonitoringBO);
        return adminMessageBasePage;
    }
}

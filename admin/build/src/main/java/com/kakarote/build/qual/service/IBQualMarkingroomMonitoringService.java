package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.BQualMarkingroomMonitoringBO;
import com.kakarote.build.qual.entity.PO.BQualMarkingroomMonitoring;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 标养室监测信息 服务类
 * </p>
 *
 * @author zhang
 * @since 2022-07-14
 */
public interface IBQualMarkingroomMonitoringService extends BaseService<BQualMarkingroomMonitoring> {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BQualMarkingroomMonitoring> selectDataList(BQualMarkingroomMonitoringBO bQualMarkingroomMonitoringBO);
}

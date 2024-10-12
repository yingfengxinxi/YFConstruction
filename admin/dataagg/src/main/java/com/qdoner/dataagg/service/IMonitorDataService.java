package com.qdoner.dataagg.service;

import com.kakarote.core.servlet.BaseService;
import com.qdoner.dataagg.entity.PO.MonitorData;


/**
 * <p>
 * 设备监测-监测项历史数据表 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-05-20
 */
public interface IMonitorDataService extends BaseService<MonitorData> {
    int deleteByMonitorTime(String mTime);
}

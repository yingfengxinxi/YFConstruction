package com.qdoner.dataagg.service;

import com.kakarote.core.servlet.BaseService;
import com.qdoner.dataagg.entity.PO.MonitorPoint;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-监测点设置 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-05-18
 */
public interface IMonitorPointService extends BaseService<MonitorPoint> {
    List<Map<String, String>> getAssetStatus();
}

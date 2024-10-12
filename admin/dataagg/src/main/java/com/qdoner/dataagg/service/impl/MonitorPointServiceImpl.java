package com.qdoner.dataagg.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.qdoner.dataagg.entity.PO.MonitorPoint;
import com.qdoner.dataagg.mapper.MonitorPointMapper;
import com.qdoner.dataagg.service.IMonitorPointService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-监测点设置 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-05-18
 */
@Service
public class MonitorPointServiceImpl extends BaseServiceImpl<MonitorPointMapper, MonitorPoint> implements IMonitorPointService {

    @Override
    public List<Map<String, String>> getAssetStatus() {
        return getBaseMapper().getAssetStatus();
    }
}

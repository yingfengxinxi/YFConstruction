package com.qdoner.dataagg.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.qdoner.dataagg.entity.PO.MonitorData;
import com.qdoner.dataagg.mapper.MonitorDataMapper;
import com.qdoner.dataagg.service.IMonitorDataService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备监测-监测项历史数据表 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-05-20
 */
@Service
public class MonitorDataServiceImpl extends BaseServiceImpl<MonitorDataMapper, MonitorData> implements IMonitorDataService {
    @Override
    public int deleteByMonitorTime(String mTime){
        return getBaseMapper().deleteByMonitorTime(mTime);
    }
}

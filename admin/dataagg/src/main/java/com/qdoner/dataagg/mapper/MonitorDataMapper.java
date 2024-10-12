package com.qdoner.dataagg.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.qdoner.dataagg.entity.PO.MonitorData;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备监测-监测项历史数据表 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-05-20
 */
public interface MonitorDataMapper extends BaseMapper<MonitorData> {
    int deleteByMonitorTime(@Param("mTime") String mTime);
}

package com.qdoner.datarecept.env.service;

import com.qdoner.datarecept.env.entity.PO.EnvPostLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 扬尘在线监测设备数据传输日志 服务类
 * </p>
 *
 * @author shz
 * @since 2021-08-23
 */
public interface IEnvPostLogService extends BaseService<EnvPostLog> {

    /**
     * 车辆清洗数据
     * @return
     */
    Result vehicleCleaningData(List<Map<String, Object>> list);

    Result baseSoilAlarmData(List<Map<String, Object>> list);

    /**
     * 环境监测数据传输
     * @param map
     * @return
     */
    Result envMonitorDataAsync(Map<String, Object> map);
}

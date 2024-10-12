package com.qdoner.datarecept.env.mapper;

import com.qdoner.datarecept.env.entity.PO.EnvPostLog;
import com.kakarote.core.servlet.BaseMapper;

/**
 * <p>
 * 扬尘在线监测设备数据传输日志 Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2021-08-23
 */
public interface EnvPostLogMapper extends BaseMapper<EnvPostLog> {

    int saveLog(EnvPostLog postLog);
}

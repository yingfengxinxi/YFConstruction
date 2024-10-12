package com.qdoner.datarecept.equipment.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.qdoner.datarecept.equipment.entity.PO.MonitorPointAlert;
import com.kakarote.core.servlet.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 监测点警报 Mapper 接口
 * </p>
 *
 * @author shao
 * @since 2021-05-20
 */
public interface MonitorPointAlertMapper extends BaseMapper<MonitorPointAlert> {

    @SqlParser(filter = true)
    int saveAlertByList(List<MonitorPointAlert> list);
}

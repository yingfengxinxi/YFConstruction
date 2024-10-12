package com.qdoner.datarecept.equipment.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.qdoner.datarecept.equipment.entity.PO.EquipmentMonitorPoint;
import com.kakarote.core.servlet.BaseMapper;

/**
 * <p>
 * 设备监测-监测点设置 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-05-18
 */
public interface EquipmentMonitorPointMapper extends BaseMapper<EquipmentMonitorPoint> {

    @SqlParser(filter = true)
    EquipmentMonitorPoint getByIdInApi(Integer id);
}

package com.qdoner.datarecept.equipment.service;

import com.qdoner.datarecept.equipment.entity.PO.EquipmentMonitorPoint;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 设备监测-监测点设置 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-05-18
 */
public interface IEquipmentMonitorPointService extends BaseService<EquipmentMonitorPoint> {

    /**
    * @Description:监测数据接口
    * @author shz
    * @date 2022-4-11 11:53
    * @param
    * @return
    */
    EquipmentMonitorPoint getByIdInApi(Integer id);


}

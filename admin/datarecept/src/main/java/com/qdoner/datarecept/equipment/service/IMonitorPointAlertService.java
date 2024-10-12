package com.qdoner.datarecept.equipment.service;

import com.qdoner.datarecept.equipment.entity.PO.MonitorPointAlert;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 监测点警报 服务类
 * </p>
 *
 * @author shao
 * @since 2021-05-20
 */
public interface IMonitorPointAlertService extends BaseService<MonitorPointAlert> {

    /**
    * @Description:品茗数据接口批量添加报警数据
    * @author shz
    * @date 2022-4-7 11:12
    * @param list
    * @return 
    */
    int saveAlertByList(List<MonitorPointAlert> list);
    //工具方法
    MonitorPointAlert copyPastry(MonitorPointAlert copyObj);

}
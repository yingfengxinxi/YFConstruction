package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.BO.EquipmentMonitorDataBO;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorDataEchartsBO;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorDataMonth;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-监测项月数据表 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-10-28
 */
public interface IEquipmentMonitorDataMonthService extends BaseService<EquipmentMonitorDataMonth> {

    BasePage<Map<String, Object>> selectMonitorDataListByMonth(EquipmentMonitorDataBO monitorDataBO);

    List<Map<String,Object>> selectMonitorDataMonthEcharts(EquipmentMonitorDataEchartsBO monitorDataEchartsBO);

    Map<String,Object> selectRecentData(Map<String, Object> params);

    //2022-06-20 日 环境历史监测新增
    Map<String,Object> airQualityMouth(Map<String, Object> params);


    //2022-06-24 日 环境历史监测年度新增
    Map<String,Object> yearDataStatistics(Map<String, Object> params);
}

package com.kakarote.build.equipment.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorDataDay;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 设备监测-监测项日数据表 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-10-28
 */
public interface IEquipmentMonitorDataDayService extends BaseService<EquipmentMonitorDataDay> {
    /**
     * 根据工程获取基坑小时数据-点位 折线图
     * @return
     */
    public JSONObject getDayDataByPoint(Integer proId);

    /**
     * 根据设备获取小时数据-点位 折线图
     * @return
     */
    JSONObject getDayDataByEquiId(Integer equipmentId);
}

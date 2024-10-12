package com.qdoner.datarecept.service;

import com.kakarote.core.common.Result;
import com.qdoner.datarecept.entity.PO.BMonitorPointAlert;

/**
 * 设备状态通用处理类
 */
public interface EqStatusCommonService {
    /**
     * 统一处理设备状态
     * @param assetId 设备ID
     * @param status 当前状态
     */
    Result handleOfflineSatus(Integer assetId, String status);

    void sendMsgEquipment(BMonitorPointAlert pointAlert);
}

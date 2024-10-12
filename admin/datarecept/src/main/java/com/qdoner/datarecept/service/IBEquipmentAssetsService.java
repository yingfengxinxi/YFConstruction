package com.qdoner.datarecept.service;

import com.qdoner.datarecept.entity.PO.BEquipmentAssets;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备资产 服务类
 * </p>
 *
 * @author wudw
 * @since 2022-07-12
 */
public interface IBEquipmentAssetsService extends BaseService<BEquipmentAssets> {



    public void updateByAssetCode(BEquipmentAssets assets);

    Map selectTowerCraneById(String id);

    Map selectElevatorById(String id);

    /**
     * 根据设备编号查询设备
     * @param assetCode
     * @return
     */
    BEquipmentAssets selectByAssetId(Integer assetCode);

    List<Map<String, Object>> selectMonitorDataList(String timeValue);

    List<Map<String, Object>> selectTowerCraneDataList(String timeValue);

    List<Map<String, Object>> selectElevatorDataList(String timeValue);

    List<Map<String, Object>> selectRoomDataList(String timeValue);
}

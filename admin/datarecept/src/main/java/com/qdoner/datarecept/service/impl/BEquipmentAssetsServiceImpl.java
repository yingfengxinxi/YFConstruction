package com.qdoner.datarecept.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.qdoner.datarecept.entity.PO.BEquipmentAssets;
import com.qdoner.datarecept.mapper.BEquipmentAssetsMapper;
import com.qdoner.datarecept.service.IBEquipmentAssetsService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备资产 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2022-07-12
 */
@Service
public class BEquipmentAssetsServiceImpl extends BaseServiceImpl<BEquipmentAssetsMapper, BEquipmentAssets> implements IBEquipmentAssetsService {

    public void updateByAssetCode(BEquipmentAssets assets){
        UpdateWrapper<BEquipmentAssets> wrapper = new UpdateWrapper<>();
        wrapper.eq("asset_code",assets.getAssetCode());
        update(assets,wrapper);
    }

    @Override
    public Map selectTowerCraneById(String id) {
        Map map = getBaseMapper().selectTowerCraneById(id);
        if (map == null) {
            map = new HashMap();
            map.put("assetName", "#塔机");
            map.put("rotation", 0);
            map.put("windspeed", 0);
            map.put("amplitude", 0);
            map.put("weight", 0);
            map.put("amplitude", 0);
            map.put("dipxZero", 0);
            map.put("value", 0);
        }
        return map;
    }

    @Override
    public Map selectElevatorById(String id) {
        Map<String, Object> obj = getBaseMapper().selectElevatorById(id);
        if (obj == null) {
            obj = new HashMap<>();
            obj.put("assetName", "#升降机");
        }
        return obj;
    }

    @Override
    public List<Map<String, Object>> selectMonitorDataList(String timeValue) {
        List<Map<String, Object>> list = getBaseMapper().selectMonitorDataList(timeValue);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectTowerCraneDataList(String timeValue) {
        List<Map<String, Object>> list = getBaseMapper().selectTowerCraneDataList(timeValue);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectElevatorDataList(String timeValue) {
        List<Map<String, Object>> list = getBaseMapper().selectElevatorDataList(timeValue);
        return list;
    }

    @Override
    public BEquipmentAssets selectByAssetId(Integer assetId) {
        return getBaseMapper().selectByAssetId(assetId);
    }

    @Override
    public List<Map<String, Object>> selectRoomDataList(String timeValue) {
        List<Map<String, Object>> list = getBaseMapper().selectRoomDataList(timeValue);
        return list;
    }
}

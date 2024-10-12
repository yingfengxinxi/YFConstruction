package com.kakarote.build.gb28181.service;

import com.kakarote.build.equipment.entity.PO.BEquipmentCertificate;
import com.kakarote.build.gb28181.entity.PO.DeviceProjectRelation;
import com.kakarote.core.common.Result;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ysyDockingService
 * @Author wnj58
 * @Date 2021/12/6 18:16
 * @purpose
 * @Version 1.0
 **/
public interface YsyDockingService extends BaseService<DeviceProjectRelation> {
    Result playback(Map<String, Object> map);

    Result getAccessToken(Map<String, Object> map);

    Result ptzControl(Map<String, Object> map);

    Result wvpPtzControl(Map<String,Object> map);

    Result ysyPostInit(Map<String, Object> params, String URL);

    Result ysyPostInit2(Map<String,Object> param, String URL);

    List<String> wvpOpenUrl(Map<String,Object> map);

    void runOfflineUpdates(Integer id);
}

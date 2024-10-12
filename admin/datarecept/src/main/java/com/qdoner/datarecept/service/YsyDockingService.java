package com.qdoner.datarecept.service;

import com.kakarote.core.common.Result;
import com.kakarote.core.servlet.BaseService;
import com.qdoner.datarecept.entity.PO.DeviceProjectRelation;

import java.util.List;
import java.util.Map;

public interface YsyDockingService extends BaseService<DeviceProjectRelation> {
    Result playback(Map<String, Object> map);

    Result getAccessToken(Map<String, Object> map);

    Result ptzControl(Map<String, Object> map);

    Result wvpPtzControl(Map<String,Object> map);

    Result ysyPostInit(Map<String, Object> params, String URL);

    Result ysyPostInit2(Map<String,Object> param, String URL);


    void runOfflineUpdates(Integer id);
}

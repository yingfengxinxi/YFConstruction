package com.kakarote.build.wifi.service;

import com.kakarote.build.wifi.entity.BO.WifiVideoBO;
import com.kakarote.build.wifi.entity.PO.WifiVideoWatchRecord;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 教育视频观看记录 服务类
 * </p>
 *
 * @author zy
 * @since 2021-06-01
 */
public interface IWifiVideoWatchRecordService extends BaseService<WifiVideoWatchRecord> {

    public BasePage<Map<String,Object>> selectList(WifiVideoBO wifiVideoBO);

    public Map getCreateTimeLast(Map map);

}

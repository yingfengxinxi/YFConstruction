package com.kakarote.build.wifi.service;

import com.kakarote.build.wifi.entity.BO.WifiTestPaperRulesBO;
import com.kakarote.build.wifi.entity.BO.WifiVideoBO;
import com.kakarote.build.wifi.entity.PO.WifiVideo;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * 教育视频配置--主表 服务类
 * </p>
 *
 * @author zy
 * @since 2021-06-01
 */
public interface IWifiVideoService extends BaseService<WifiVideo> {

    public BasePage<Map<String,Object>> selectList(WifiVideoBO wifiVideoBO);

    public WifiVideo saveTable(WifiVideo wifiVideo);

    public WifiVideo updateTable(WifiVideo wifiVideo);

    public Map selectById(String id);

}

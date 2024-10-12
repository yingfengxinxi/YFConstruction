package com.kakarote.build.wifi.service;

import com.kakarote.build.wifi.entity.PO.WifiVideoDetail;
import com.kakarote.core.servlet.BaseService;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 教育视频配置--明细表 服务类
 * </p>
 *
 * @author zy
 * @since 2021-06-01
 */
public interface IWifiVideoDetailService extends BaseService<WifiVideoDetail> {

    public Map getOneVideoRandom(Map map);
    public WifiVideoDetail selectByBithIdToFileId (String  id);

}

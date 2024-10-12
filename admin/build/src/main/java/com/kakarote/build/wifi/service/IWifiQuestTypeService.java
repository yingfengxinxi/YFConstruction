package com.kakarote.build.wifi.service;

import com.kakarote.build.wifi.entity.PO.WifiQuestType;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * WiFi教育答题--问题类别管理 服务类
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
public interface IWifiQuestTypeService extends BaseService<WifiQuestType> {

    public List<Map> queryChildrenByMap(Map<String,Object>map);

    public List<Map> selectTree();

    public void delById(String id);

}

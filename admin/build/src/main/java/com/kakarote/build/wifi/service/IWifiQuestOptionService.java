package com.kakarote.build.wifi.service;

import com.kakarote.build.wifi.entity.PO.WifiQuestOption;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * wifi教育答题--问题选项表 服务类
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
public interface IWifiQuestOptionService extends BaseService<WifiQuestOption> {

    List<Map<String,Object>> TestPaperOpt(Map<String,Object> map);

    /**
     * 根据id删除数据
     * @param id
     */
    void deleteById(String id);

    /**
     * 根据questId删除数据
     * @param questId
     */
    void deleteByQuestId(String questId);
}

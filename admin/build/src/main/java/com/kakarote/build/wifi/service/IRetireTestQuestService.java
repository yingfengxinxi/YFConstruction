package com.kakarote.build.wifi.service;

import com.kakarote.build.wifi.entity.PO.RetireTestQuest;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 试卷问题 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-05-25
 */
public interface IRetireTestQuestService extends BaseService<RetireTestQuest> {

    List<Map<String,Object>> getAllTestQuest(Map<String,Object> map);

    void insertQue(Map<String, Object> tpq);
}

package com.kakarote.build.event.service;

import com.kakarote.build.event.entity.BO.EventInfoBO;
import com.kakarote.build.event.entity.PO.EventInfo;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 环保事件督办——事件表 服务类
 * </p>
 *
 * @author lzy
 * @since 2022-03-07
 */
public interface IEventInfoService extends BaseService<EventInfo> {

    Map<String, Object> selectStatusNumber();

    BasePage<EventInfo> selectByPage(EventInfoBO eventInfoBO);

    Map<String, Object> selectDataById(String id);

    List<Map<String, Object>> selectProEventRanking();

    List<Map<String, Object>> selectEventNumber();
}

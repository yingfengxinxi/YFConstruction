package com.kakarote.build.event.service;

import com.kakarote.build.event.entity.BO.EventItemBO;
import com.kakarote.build.event.entity.PO.EventItem;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 环保事件督办——处理表 服务类
 * </p>
 *
 * @author lzy
 * @since 2022-03-07
 */
public interface IEventItemService extends BaseService<EventItem> {

    void saveHandleInfo(EventItem eventItem);

    void eventCheck(EventItem eventItem);

    BasePage<Map<String, Object>> selectProEventReturn(EventItemBO eventItemBO);

    List<EventItem> selectItemList(Map<String, Object> params);
}

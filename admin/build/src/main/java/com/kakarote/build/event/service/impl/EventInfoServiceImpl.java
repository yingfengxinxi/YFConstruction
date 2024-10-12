package com.kakarote.build.event.service.impl;

import com.kakarote.build.event.entity.BO.EventInfoBO;
import com.kakarote.build.event.entity.PO.EventInfo;
import com.kakarote.build.event.entity.PO.EventItem;
import com.kakarote.build.event.mapper.EventInfoMapper;
import com.kakarote.build.event.service.IEventInfoService;
import com.kakarote.build.event.service.IEventItemService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 环保事件督办——事件表 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2022-03-07
 */
@Service
public class EventInfoServiceImpl extends BaseServiceImpl<EventInfoMapper, EventInfo> implements IEventInfoService {

    @Autowired
    private IEventItemService eventItemService;

    @Autowired
    private AdminService adminService;

    @Override
    public Map<String, Object> selectStatusNumber() {
        Map<String, Object> map = this.getBaseMapper().selectStatusNumber();
        return map;
    }

    @Override
    public BasePage<EventInfo> selectByPage(EventInfoBO eventInfoBO) {
        BasePage<EventInfo> list = this.getBaseMapper().selectByPage(eventInfoBO.parse(), eventInfoBO);
        return list;
    }

    @Override
    public Map<String, Object> selectDataById(String id) {
        Map<String, Object> data = new HashMap<>();
        // 查询事件信息
        EventInfo eventInfo = this.getBaseMapper().selectById(id);

        // 查询用户信息
        SimpleUser simpleUser = adminService.queryUserById(eventInfo.getInformant()).getData();
        eventInfo.setInformantName(simpleUser.getRealname());

        Map<String, Object> params = new HashMap<>();
        params.put("eventId", id);
        List<EventItem> eventItemList = eventItemService.selectItemList(params);

        // 查询事件处理数据
//        List<EventItem> eventItemList = eventItemService.lambdaQuery()
//                .eq(EventItem::getEventId, id).orderByDesc(EventItem::getHandleTime).list();

        data.put("eventInfo", eventInfo);
        data.put("eventItemList", eventItemList);

        return data;
    }

    @Override
    public List<Map<String, Object>> selectProEventRanking() {
        List<Map<String, Object>> list = getBaseMapper().selectProEventRanking();
        return list;
    }

    @Override
    public List<Map<String, Object>> selectEventNumber() {
        return getBaseMapper().selectEventNumber();
    }
}

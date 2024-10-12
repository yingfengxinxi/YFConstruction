package com.kakarote.build.event.service.impl;

import com.kakarote.build.event.entity.BO.EventItemBO;
import com.kakarote.build.event.entity.PO.EventInfo;
import com.kakarote.build.event.entity.PO.EventItem;
import com.kakarote.build.event.mapper.EventItemMapper;
import com.kakarote.build.event.service.IEventInfoService;
import com.kakarote.build.event.service.IEventItemService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 环保事件督办——处理表 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2022-03-07
 */
@Service
public class EventItemServiceImpl extends BaseServiceImpl<EventItemMapper, EventItem> implements IEventItemService {

    @Autowired
    private IEventInfoService eventInfoService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveHandleInfo(EventItem eventItem) {

        if (eventItem.getEventId() == null || eventItem.getEventId().equals("")) {
            throw new CrmException(500, "参数EventId不能为空！");
        }

        Long userId = UserUtil.getUser().getUserId();
        String wxOpenId = UserUtil.getUser().getWxappOpenId();
        String uuid= UUID.randomUUID().toString().replaceAll("-", "");
        eventItem.setId(uuid);
        eventItem.setHandleBy(wxOpenId);
        eventItem.setHandleTime(new Date());

        // 保存处理信息
        boolean result = this.save(eventItem);

        // 如果事件处理信息保持成功，更新主表状态为2：待审核：
        if (result) {
            EventInfo eventInfo = new EventInfo();
            eventInfo.setId(eventItem.getEventId());
            eventInfo.setEventStatus("2");
            eventInfo.setUpdateBy(userId);
            eventInfoService.updateById(eventInfo);
        } else {
            throw new CrmException(500, "事件处理保存失败！");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eventCheck(EventItem eventItem) {

        if (eventItem.getId() == null || eventItem.getId().equals("")) {
            throw new CrmException(500, "参数id不能为空");
        }
        if (eventItem.getEventId() == null || eventItem.getEventId().equals("")) {
            throw new CrmException(500, "参数EventId不能为空！");
        }
        if (eventItem.getCheckResult() == null || eventItem.getCheckResult().equals("")) {
            throw new CrmException(500, "参数checkResult不能为空！");
        }

        Long userId = UserUtil.getUser().getUserId();
        eventItem.setCheckBy(userId);
        eventItem.setCheckTime(new Date());
        eventItem.setUpdateBy(userId);

        // 更新审核信息
        boolean result = this.updateById(eventItem);


        if (!result) {
            throw new CrmException(500, "审核信息保存失败");
        }

        EventInfo eventInfo = new EventInfo();
        eventInfo.setId(eventItem.getEventId());
        eventInfo.setUpdateBy(userId);
        if (eventItem.getCheckResult().equals("1")) {
            // 审核通过 更新主表状态为4：完成
            eventInfo.setEventStatus("4");
        } else if (eventItem.getCheckResult().equals("2")) {
            // 审核未通过 更新主表状态为3：退回
            eventInfo.setEventStatus("3");
        }
        // 更新主表信息
        eventInfoService.updateById(eventInfo);
    }

    @Override
    public BasePage<Map<String, Object>> selectProEventReturn(EventItemBO eventItemBO) {
        BasePage<Map<String, Object>> list = this.getBaseMapper().selectProEventReturn(eventItemBO.parse(), eventItemBO);

        // 计算环比和占比
        for (Map<String, Object> map:list.getList()) {
            // 总数
            Integer totalNumber = Integer.valueOf(String.valueOf(map.get("totalNumber")));
            // 退回数
            Integer number = Integer.valueOf(String.valueOf(map.get("number")));
            // 本期
            Integer currentMonth = Integer.valueOf(String.valueOf(map.get("currentMonth")));
            // 上期
            Integer lastMonth = Integer.valueOf(String.valueOf(map.get("lastMonth")));

            NumberFormat format = NumberFormat.getPercentInstance();
            format.setMaximumFractionDigits(0);
            if (totalNumber == 0) { // 除数为0，百分比直接为100%
                map.put("percent", "0%");
            } else {
                double retio = number * 1.0 / totalNumber;
                String percent = format.format(retio);
                map.put("percent", percent);
            }

            // 环比增长率=（本期数-上期数）/上期数×100%
            Integer sum = currentMonth - lastMonth;
            double res;
            if (lastMonth == 0) {
                res = 0;
            } else {
                res = sum / lastMonth;
            }
            String formatRes = String.format("%.2f", res * 100) + "%";
            map.put("linkRatio", formatRes);
        }

        return list;
    }

    @Override
    public List<EventItem> selectItemList(Map<String, Object> params) {
        List<EventItem> list = this.getBaseMapper().selectItemList(params);
        return list;
    }
}

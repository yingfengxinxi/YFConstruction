package com.kakarote.build.ai.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.kakarote.build.ai.entity.BO.AiVideoAlertBO;
import com.kakarote.build.ai.entity.PO.AiVideoAlert;
import com.kakarote.build.ai.mapper.AiVideoAlertMapper;
import com.kakarote.build.ai.service.IAiVideoAlertService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminDictData;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.UserUtil;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shao
 * @since 2021-05-24
 */
@Service
public class AiVideoAlertServiceImpl extends BaseServiceImpl<AiVideoAlertMapper, AiVideoAlert> implements IAiVideoAlertService {

    @Override
    public BasePage<AiVideoAlert> selectPage(AiVideoAlertBO aiVideoAlertBO) {
        aiVideoAlertBO.setOrdersBd("p.alert_time",false);
        BasePage<AiVideoAlert> list = getBaseMapper().selectPage(aiVideoAlertBO.parse(), aiVideoAlertBO);
        return list;
    }

    @Override
    public Map aiEqumentStatistic() {
        Map map=getBaseMapper().aiEqumentStatistic();
        //从缓存获取固定类型的字典数据
        Redis redis = BaseUtil.getRedis();
        Object a=redis.get("sd-"+UserUtil.getUser().getTenantId()+"-"+"b_ai_event_type");
//        System.out.println("a.tostring:"+String.valueOf(a));
        JSONArray js= JSON.parseArray(String.valueOf(a));

        //已启用AI事件类型 因在数据库中是以字符串分割存储的，需要处理
        String field=String.valueOf(map.get("field"));
        String [] strs=field.split(",");
        Set<String> set=new HashSet<>();
        for(String s:strs){
            set.add(s);
        }
        map.remove("field");
        map.put("EnableAiEventType",set.size());
        //未启用的
        map.put("notEnableAiEventType",js.size()-set.size());
        return map;
    }

    @Override
    public List<Map> aiEventTypeStatistic() {
        return getBaseMapper().aiEventTypeStatistic();
    }

    @Override
    public List<Map<String, Object>> selectAIAlarmList() {
        List<Map<String, Object>> list = this.baseMapper.selectAIAlarmList();
        return list;
    }

    @Override
    public AiVideoAlert selectInfoById(Integer id) {
        AiVideoAlert aiVideoAlert = this.baseMapper.selectInfoById(id);
        return aiVideoAlert;
    }
}

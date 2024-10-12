package com.kakarote.build.wifi.service.impl;

import com.kakarote.build.wifi.entity.PO.RetireTestQuest;
import com.kakarote.build.wifi.mapper.RetireTestQuestMapper;
import com.kakarote.build.wifi.service.IRetireTestQuestService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 试卷问题 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-05-25
 */
@Service
public class RetireTestQuestServiceImpl extends BaseServiceImpl<RetireTestQuestMapper, RetireTestQuest> implements IRetireTestQuestService {

    @Override
    public List<Map<String, Object>> getAllTestQuest(Map<String, Object> map) {
        return getBaseMapper().getAllTestQuest(map);
    }

    @Override
    public void insertQue(Map<String, Object> tpq) {
        int i = getBaseMapper().insertQue(tpq);
    }
}

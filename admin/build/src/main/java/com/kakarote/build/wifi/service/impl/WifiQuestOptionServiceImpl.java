package com.kakarote.build.wifi.service.impl;

import com.kakarote.build.wifi.entity.PO.WifiQuestOption;
import com.kakarote.build.wifi.mapper.WifiQuestOptionMapper;
import com.kakarote.build.wifi.service.IWifiQuestOptionService;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * wifi教育答题--问题选项表 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
@Service
public class WifiQuestOptionServiceImpl extends BaseServiceImpl<WifiQuestOptionMapper, WifiQuestOption> implements IWifiQuestOptionService {

    @Override
    public List<Map<String, Object>> TestPaperOpt(Map<String, Object> map) {
        return getBaseMapper().TestPaperOpt(map);
    }

    @Override
    public void deleteById(String id) {
        getBaseMapper().deleteQuestOptionById(id);
    }

    @Override
    public void deleteByQuestId(String questId) {
        getBaseMapper().deleteByQuestId(questId);
    }
}

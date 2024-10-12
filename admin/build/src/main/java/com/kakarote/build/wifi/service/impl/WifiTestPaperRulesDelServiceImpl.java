package com.kakarote.build.wifi.service.impl;

import com.kakarote.build.wifi.entity.PO.WifiTestPaperRules;
import com.kakarote.build.wifi.entity.PO.WifiTestPaperRulesDel;
import com.kakarote.build.wifi.mapper.WifiTestPaperRulesDelMapper;
import com.kakarote.build.wifi.service.IWifiTestPaperRulesDelService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * wifi教育答题--试卷规则配置详情 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
@Service
public class WifiTestPaperRulesDelServiceImpl extends BaseServiceImpl<WifiTestPaperRulesDelMapper, WifiTestPaperRulesDel> implements IWifiTestPaperRulesDelService {

    @Override
    public List<Map<String, Object>> getRuleDetail(WifiTestPaperRules TestPaperRule) {
        return getBaseMapper().getRuleDetail(TestPaperRule);
    }
}

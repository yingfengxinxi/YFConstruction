package com.kakarote.build.wifi.service;

import com.kakarote.build.wifi.entity.PO.WifiTestPaperRules;
import com.kakarote.build.wifi.entity.PO.WifiTestPaperRulesDel;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * wifi教育答题--试卷规则配置详情 服务类
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
public interface IWifiTestPaperRulesDelService extends BaseService<WifiTestPaperRulesDel> {
        List<Map<String,Object>> getRuleDetail(WifiTestPaperRules TestPaperRule);
}

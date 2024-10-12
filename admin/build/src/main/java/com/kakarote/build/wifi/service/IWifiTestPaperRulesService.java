package com.kakarote.build.wifi.service;

import com.kakarote.build.wifi.entity.BO.WifiQuestionsBO;
import com.kakarote.build.wifi.entity.BO.WifiTestPaperRulesBO;
import com.kakarote.build.wifi.entity.PO.WifiTestPaperRules;
import com.kakarote.build.wifi.entity.PO.WifiTestPaperRulesDel;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * wifi答题教育--试卷规则配置 服务类
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
public interface IWifiTestPaperRulesService extends BaseService<WifiTestPaperRules> {

    public BasePage<Map<String,Object>> selectList(WifiTestPaperRulesBO wifiTestPaperRulesBO);

    WifiTestPaperRules getTestPaperByWorkTypeId(WifiTestPaperRulesBO WTPRB);
}

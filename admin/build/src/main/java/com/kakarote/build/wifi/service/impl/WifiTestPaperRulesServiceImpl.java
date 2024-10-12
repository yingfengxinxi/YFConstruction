package com.kakarote.build.wifi.service.impl;

import com.kakarote.build.wifi.entity.BO.WifiTestPaperRulesBO;
import com.kakarote.build.wifi.entity.PO.WifiTestPaperRules;
import com.kakarote.build.wifi.entity.PO.WifiTestPaperRulesDel;
import com.kakarote.build.wifi.mapper.WifiTestPaperRulesMapper;
import com.kakarote.build.wifi.service.IWifiTestPaperRulesService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * wifi答题教育--试卷规则配置 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
@Service
public class WifiTestPaperRulesServiceImpl extends BaseServiceImpl<WifiTestPaperRulesMapper, WifiTestPaperRules> implements IWifiTestPaperRulesService {

    @Override
    public BasePage<Map<String, Object>> selectList(WifiTestPaperRulesBO wifiTestPaperRulesBO) {
        wifiTestPaperRulesBO.setOrdersBd("create_time",false);
        return getBaseMapper().selectList(wifiTestPaperRulesBO.parse(),wifiTestPaperRulesBO);
    }

    @Override
    public WifiTestPaperRules getTestPaperByWorkTypeId(WifiTestPaperRulesBO WTPRB) {
        return getBaseMapper().getPaperRuleByWorkTypeId(WTPRB);
    }
}

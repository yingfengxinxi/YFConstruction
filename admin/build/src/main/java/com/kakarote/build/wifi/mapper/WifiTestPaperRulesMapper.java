package com.kakarote.build.wifi.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.wifi.entity.BO.WifiQuestionsBO;
import com.kakarote.build.wifi.entity.BO.WifiTestPaperRulesBO;
import com.kakarote.build.wifi.entity.PO.WifiQuestions;
import com.kakarote.build.wifi.entity.PO.WifiTestPaperRules;
import com.kakarote.build.wifi.entity.PO.WifiTestPaperRulesDel;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * wifi答题教育--试卷规则配置 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
public interface WifiTestPaperRulesMapper extends BaseMapper<WifiTestPaperRules> {

    BasePage<Map<String,Object>> selectList(BasePage<WifiTestPaperRules> page, @Param("data") WifiTestPaperRulesBO wifiTestPaperRulesBO);

    /**
     * 获取最新一条试卷配置规则，加注解是因为需要对当前sql放行
     * @param WTPRB
     * @return
     */
    @SqlParser(filter=true)
    WifiTestPaperRules getPaperRuleByWorkTypeId(@Param("data")  WifiTestPaperRulesBO WTPRB);
}

package com.kakarote.build.wifi.mapper;

import com.kakarote.build.wifi.entity.PO.WifiTestPaperRules;
import com.kakarote.build.wifi.entity.PO.WifiTestPaperRulesDel;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * wifi教育答题--试卷规则配置详情 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
public interface WifiTestPaperRulesDelMapper extends BaseMapper<WifiTestPaperRulesDel> {


List<Map<String,Object>> getRuleDetail(@Param("data") WifiTestPaperRules TestPaperRule);
}

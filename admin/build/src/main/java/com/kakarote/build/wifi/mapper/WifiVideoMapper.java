package com.kakarote.build.wifi.mapper;

import com.kakarote.build.wifi.entity.BO.WifiTestPaperRulesBO;
import com.kakarote.build.wifi.entity.BO.WifiVideoBO;
import com.kakarote.build.wifi.entity.PO.WifiTestPaperRules;
import com.kakarote.build.wifi.entity.PO.WifiVideo;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 教育视频配置--主表 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-06-01
 */
public interface WifiVideoMapper extends BaseMapper<WifiVideo> {

    BasePage<Map<String,Object>> selectList(BasePage<WifiVideo> page, @Param("data") WifiVideoBO wifiVideoBO);


}

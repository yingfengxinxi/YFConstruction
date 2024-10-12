package com.kakarote.build.wifi.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.wifi.entity.BO.WifiVideoBO;
import com.kakarote.build.wifi.entity.PO.WifiVideo;
import com.kakarote.build.wifi.entity.PO.WifiVideoWatchRecord;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 教育视频观看记录 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-06-01
 */
public interface WifiVideoWatchRecordMapper extends BaseMapper<WifiVideoWatchRecord> {

    @SqlParser(filter=true)
    BasePage<Map<String,Object>> selectList(BasePage<WifiVideoWatchRecord> page, @Param("data") WifiVideoBO wifiVideoBO);

    public Map getCreateTimeLast(@Param("data") Map map);
}

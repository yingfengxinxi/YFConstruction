package com.kakarote.build.wifi.mapper;

import com.kakarote.build.wifi.entity.PO.WifiVideoDetail;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 教育视频配置--明细表 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-06-01
 */
public interface WifiVideoDetailMapper extends BaseMapper<WifiVideoDetail> {

    public Map getOneVideoRandom(@Param("data") Map map);

    public WifiVideoDetail selectByBithIdToFileId (String  id);
}

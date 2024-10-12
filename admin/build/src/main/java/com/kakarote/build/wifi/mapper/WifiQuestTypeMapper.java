package com.kakarote.build.wifi.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.wifi.entity.PO.WifiQuestType;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * WiFi教育答题--问题类别管理 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
public interface WifiQuestTypeMapper extends BaseMapper<WifiQuestType> {

    public List<Map> queryChildrenByMap(@Param("data") Map<String,Object>map);

    @SqlParser(filter = true)
    void deleteTypeById(@Param("id") String id);
}

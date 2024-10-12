package com.kakarote.build.wifi.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.wifi.entity.PO.WifiQuestOption;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * wifi教育答题--问题选项表 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
public interface WifiQuestOptionMapper extends BaseMapper<WifiQuestOption> {

    @SqlParser(filter = true)
    List<Map<String, Object>> TestPaperOpt(@Param("data") Map<String, Object> map);

    @SqlParser(filter = true)
    void deleteQuestOptionById(@Param("id") String id);

    @SqlParser(filter = true)
    void deleteByQuestId(@Param("questId") String questId);
}

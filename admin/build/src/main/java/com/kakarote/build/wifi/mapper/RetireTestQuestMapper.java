package com.kakarote.build.wifi.mapper;

import com.kakarote.build.wifi.entity.PO.RetireTestQuest;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 试卷问题 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-05-25
 */
public interface RetireTestQuestMapper extends BaseMapper<RetireTestQuest> {
    List<Map<String,Object>> getAllTestQuest(@Param("data") Map<String, Object> map);

    int insertQue(@Param("data") Map<String, Object> tpq);
}

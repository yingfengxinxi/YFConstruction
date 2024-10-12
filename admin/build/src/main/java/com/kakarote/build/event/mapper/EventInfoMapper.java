package com.kakarote.build.event.mapper;

import com.kakarote.build.event.entity.BO.EventInfoBO;
import com.kakarote.build.event.entity.PO.EventInfo;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 环境事件督办——事件表 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2022-03-07
 */
public interface EventInfoMapper extends BaseMapper<EventInfo> {

    Map<String, Object> selectStatusNumber();

    BasePage<EventInfo> selectByPage(BasePage<Object> parse, @Param("data") EventInfoBO eventInfoBO);

    List<Map<String, Object>> selectProEventRanking();

    List<Map<String, Object>> selectEventNumber();
}

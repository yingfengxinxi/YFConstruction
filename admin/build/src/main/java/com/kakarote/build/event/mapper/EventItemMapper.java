package com.kakarote.build.event.mapper;

import com.kakarote.build.event.entity.BO.EventItemBO;
import com.kakarote.build.event.entity.PO.EventItem;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 环境事件督办——处理表 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2022-03-07
 */
public interface EventItemMapper extends BaseMapper<EventItem> {

    BasePage<Map<String, Object>> selectProEventReturn(BasePage<Object> parse, @Param("data") EventItemBO eventItemBO);

    List<EventItem> selectItemList(@Param("data") Map<String, Object> params);
}

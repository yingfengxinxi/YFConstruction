package com.qdoner.datarecept.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.core.servlet.BaseMapper;
import com.qdoner.datarecept.entity.PO.AiVideoAlert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shao
 * @since 2021-05-24
 */
public interface AiVideoAlertMapper extends BaseMapper<AiVideoAlert> {
    @SqlParser(filter = true)
    Integer getDataByEventTypeAndTime(@Param("equipmetnId") Integer equipmetnId, @Param("eventType") String eventType,
                                                 @Param("time") Integer time);
}

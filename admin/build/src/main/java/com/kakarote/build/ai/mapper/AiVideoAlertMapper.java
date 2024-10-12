package com.kakarote.build.ai.mapper;

import com.kakarote.build.ai.entity.BO.AiVideoAlertBO;
import com.kakarote.build.ai.entity.PO.AiVideoAlert;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.logging.Filter;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shao
 * @since 2021-05-24
 */
public interface AiVideoAlertMapper extends BaseMapper<AiVideoAlert> {

    BasePage<AiVideoAlert> selectPage(BasePage<AiVideoAlert> parse, @Param("data") AiVideoAlertBO aiVideoAlertBO);

    Map aiEqumentStatistic();

    List<Map> aiEventTypeStatistic();

    List<Map<String, Object>> selectAIAlarmList();

    AiVideoAlert selectInfoById(@Param("id") Integer id);
}

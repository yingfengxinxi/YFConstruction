package com.kakarote.build.ai.mapper;

import com.kakarote.build.ai.entity.BO.AiDregCarAlertBO;
import com.kakarote.build.ai.entity.PO.AiDregCarAlert;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 渣土车辆智能分析 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-06-04
 */
public interface AiDregCarAlertMapper extends BaseMapper<AiDregCarAlert> {

    BasePage<AiDregCarAlert> selectByPage(BasePage<AiDregCarAlert> parse, @Param("data") AiDregCarAlertBO aiDregCarAlertBO);

    BasePage<Map<String, Object>> selectDregAlertList(BasePage<Object> parse, @Param("data")AiDregCarAlertBO aiDregCarAlertBO);

    BasePage<AiDregCarAlert> selectByPagePro(BasePage<Object> parse, @Param("data") AiDregCarAlertBO aiDregCarAlertBO);
}

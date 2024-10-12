package com.kakarote.build.ai.service;

import com.kakarote.build.ai.entity.BO.AiDregCarAlertBO;
import com.kakarote.build.ai.entity.PO.AiDregCarAlert;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * 渣土车辆智能分析 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-06-04
 */
public interface IAiDregCarAlertService extends BaseService<AiDregCarAlert> {

    BasePage<AiDregCarAlert> selectByPage(AiDregCarAlertBO aiDregCarAlertBO);

    BasePage<Map<String, Object>> selectDregAlertList(AiDregCarAlertBO aiDregCarAlertBO);

    BasePage<AiDregCarAlert> selectByPagePro(AiDregCarAlertBO aiDregCarAlertBO);
}

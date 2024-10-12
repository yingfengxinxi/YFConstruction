package com.kakarote.build.ai.service;

import com.kakarote.build.ai.entity.BO.AiVideoAlertBO;
import com.kakarote.build.ai.entity.PO.AiVideoAlert;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shao
 * @since 2021-05-24
 */
public interface IAiVideoAlertService extends BaseService<AiVideoAlert> {

    public BasePage<AiVideoAlert> selectPage(AiVideoAlertBO aiVideoAlertBO);

    Map aiEqumentStatistic();

    List<Map> aiEventTypeStatistic();

    List<Map<String,Object>> selectAIAlarmList();

    AiVideoAlert selectInfoById(Integer id);
}

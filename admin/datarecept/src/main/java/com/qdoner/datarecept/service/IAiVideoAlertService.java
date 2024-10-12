package com.qdoner.datarecept.service;

import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import com.qdoner.datarecept.entity.PO.AiVideoAlert;

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

    Result aiRecognition(List<Map<String, Object>> list);
}

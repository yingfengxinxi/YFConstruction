package com.qdoner.datarecept.ai.service;

import com.qdoner.datarecept.ai.entity.PO.AiDregCar;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 渣土车记录 服务类
 * </p>
 *
 * @author shao
 * @since 2021-05-24
 */
public interface IAiDregCarService extends BaseService<AiDregCar> {

    Result saveData(List<Map<String, Object>> list);
}
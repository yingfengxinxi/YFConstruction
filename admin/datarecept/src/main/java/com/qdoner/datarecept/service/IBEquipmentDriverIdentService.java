package com.qdoner.datarecept.service;

import com.kakarote.core.common.Result;
import com.qdoner.datarecept.entity.PO.BEquipmentDriverIdent;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 机械设备司机识别 服务类
 * </p>
 *
 * @author wudw
 * @since 2022-07-12
 */
public interface IBEquipmentDriverIdentService extends BaseService<BEquipmentDriverIdent> {

    Result saveData(List<Map<String, Object>> list);

    Result offLine(List<Map<String, Object>> list);
}

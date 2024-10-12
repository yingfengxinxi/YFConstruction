package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.PO.EquipmentFormworkDataAt;
import com.kakarote.core.common.Result;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 高支模实时监测数据 服务类
 * </p>
 *
 * @author shz
 * @since 2022-03-30
 */
public interface IEquipmentFormworkDataAtService extends BaseService<EquipmentFormworkDataAt> {

    Result saveData(List<Map<String,Object>> list);
}

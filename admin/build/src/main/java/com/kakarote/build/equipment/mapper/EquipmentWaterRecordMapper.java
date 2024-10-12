package com.kakarote.build.equipment.mapper;

import com.kakarote.build.equipment.entity.BO.EquipmentWaterRecordBO;
import com.kakarote.build.equipment.entity.PO.EquipmentWaterRecord;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-06-21
 */
public interface EquipmentWaterRecordMapper extends BaseMapper<EquipmentWaterRecord> {

    List<LinkedHashMap<String, Object>> selectTableList(@Param("data") EquipmentWaterRecordBO waterRecordBO);

    int batchSaveData(@Param("list") List<EquipmentWaterRecord> recordList);

    List<LinkedHashMap<String, Object>> selectProTableList(@Param("data") EquipmentWaterRecordBO waterRecordBO);
}

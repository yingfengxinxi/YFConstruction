package com.kakarote.build.equipment.mapper;

import com.kakarote.build.equipment.entity.BO.EquipmentElectricityRecordBO;
import com.kakarote.build.equipment.entity.PO.EquipmentElectricityRecord;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 * 设备用电量 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-06-18
 */
public interface EquipmentElectricityRecordMapper extends BaseMapper<EquipmentElectricityRecord> {

    List<LinkedHashMap<String, Object>> selectTableList(@Param("data") EquipmentElectricityRecordBO electricityRecordBO);

    int batchSaveData(@Param("list") List<EquipmentElectricityRecord> recordList);

    List<LinkedHashMap<String, Object>> selectProTableList(@Param("data")EquipmentElectricityRecordBO electricityRecordBO);
}

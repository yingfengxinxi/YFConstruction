package com.qdoner.datarecept.equipment.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.qdoner.datarecept.equipment.entity.PO.EquipmentPitDataAtBak;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-基坑设备实时数据表 Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2022-03-30
 */
public interface EquipmentPitDataAtBakMapper extends BaseMapper<EquipmentPitDataAtBak> {

    @SqlParser(filter = true)
    int batchSaveData(@Param("list") List<EquipmentPitDataAtBak> bareSoilDataList);

}

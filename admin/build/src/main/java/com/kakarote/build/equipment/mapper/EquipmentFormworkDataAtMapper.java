package com.kakarote.build.equipment.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.equipment.entity.PO.EquipmentFormworkDataAt;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 高支模实时监测数据 Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2022-03-30
 */
public interface EquipmentFormworkDataAtMapper extends BaseMapper<EquipmentFormworkDataAt> {

    @SqlParser(filter = true)
    int batchSaveData(@Param("list") List<EquipmentFormworkDataAt> bareSoilDataList);
}

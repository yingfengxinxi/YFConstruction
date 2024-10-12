package com.qdoner.datarecept.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.qdoner.datarecept.entity.PO.BEquipmentTowerEffect;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 塔机功效数据 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2022-07-12
 */
public interface BEquipmentTowerEffectMapper extends BaseMapper<BEquipmentTowerEffect> {

    @SqlParser(filter = true)
    int batchSaveData(@Param("list") List<BEquipmentTowerEffect> bareSoilDataList);
}

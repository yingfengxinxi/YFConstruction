package com.qdoner.datarecept.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.qdoner.datarecept.entity.PO.BEquipmentTowerDataAt;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 塔机设备实时数据表 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2022-07-12
 */
public interface BEquipmentTowerDataAtMapper extends BaseMapper<BEquipmentTowerDataAt> {

    @SqlParser(filter = true)
    int batchSaveData(@Param("list") List<BEquipmentTowerDataAt> bareSoilDataList);
}

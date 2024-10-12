package com.kakarote.build.equipment.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.equipment.entity.BO.EquipmentTowerDataAtBO;
import com.kakarote.build.equipment.entity.PO.EquipmentTowerDataAt;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 塔机设备实时数据表 Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2022-03-22
 */
public interface EquipmentTowerDataAtMapper extends BaseMapper<EquipmentTowerDataAt> {

    @SqlParser(filter = true)
    int batchSaveData(@Param("list") List<EquipmentTowerDataAt> bareSoilDataList);

    BasePage<Map<String,Object>> selectPage(BasePage<Object> parse, @Param("data")EquipmentTowerDataAtBO towerDataAtBO);

}

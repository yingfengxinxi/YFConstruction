package com.kakarote.build.equipment.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.equipment.entity.BO.EquipmentTowerEffectBO;
import com.kakarote.build.equipment.entity.PO.EquipmentTowerEffect;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2021-12-22
 */
public interface EquipmentTowerEffectMapper extends BaseMapper<EquipmentTowerEffect> {

    List<Map> selectDZTJ(Integer id);

    List<Map> selectLJTJ(Integer id);

    Map selectJRGX(@Param("id")Integer id,@Param("type")String type, @Param("query")String query);

    Map selectJRBJS(Integer id);

    Map selectSJXX(String code);

    @SqlParser(filter = true)
    int batchSaveData(@Param("list") List<EquipmentTowerEffect> bareSoilDataList);

    Map selectOnline(@Param("equipmentId") Integer equipmentId);

    BasePage<Map<String,Object>> selectPage(BasePage<Object> parse, @Param("data")EquipmentTowerEffectBO towerEffectBO);

}

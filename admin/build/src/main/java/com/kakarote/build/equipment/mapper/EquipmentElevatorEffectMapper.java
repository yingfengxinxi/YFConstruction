package com.kakarote.build.equipment.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.equipment.entity.BO.EquipmentElevatorEffectBO;
import com.kakarote.build.equipment.entity.PO.EquipmentElevatorEffect;
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
 * @since 2021-12-29
 */
public interface EquipmentElevatorEffectMapper extends BaseMapper<EquipmentElevatorEffect> {

    List<Map> selectZZTJ(Integer id);

    List<Map> selectGDTJ(Integer id);

    List<Map> selectSDTJ(Integer id);

    Map selectJRGX(@Param("id")Integer id, @Param("type")String type, @Param("query")String query);

    Map selectJRBJS(Integer id);

    Map selectSJXX(String code);

    @SqlParser(filter = true)
    int batchSaveData(@Param("list") List<EquipmentElevatorEffect> bareSoilDataList);

    Map selectRunStatus(@Param("id") Integer id);

    BasePage<Map<String, Object>> selectPage(BasePage<Object> parse, @Param("data")EquipmentElevatorEffectBO elevatorEffectBO);
}

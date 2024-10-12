package com.qdoner.datarecept.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.qdoner.datarecept.entity.PO.BEquipmentElevatorEffect;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 升降机功效数据 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2022-07-21
 */
public interface BEquipmentElevatorEffectMapper extends BaseMapper<BEquipmentElevatorEffect> {

    List<Map> selectZZTJ(Integer id);

    List<Map> selectGDTJ(Integer id);

    List<Map> selectSDTJ(Integer id);

    Map selectJRGX(@Param("id")Integer id, @Param("type")String type, @Param("query")String query);

    Map selectJRBJS(Integer id);

    Map selectSJXX(String code);

    @SqlParser(filter = true)
    int batchSaveData(@Param("list") List<BEquipmentElevatorEffect> bareSoilDataList);

    Map selectRunStatus(@Param("id") Integer id);
}

package com.qdoner.datarecept.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.core.entity.BasePage;
import com.qdoner.datarecept.entity.BO.EquipmentElevatorDataAtBO;
import com.qdoner.datarecept.entity.PO.BEquipmentElevatorDataAt;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 升降电梯设备实时数据表 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2022-07-21
 */
public interface BEquipmentElevatorDataAtMapper extends BaseMapper<BEquipmentElevatorDataAt> {

    @SqlParser(filter = true)
    int batchSaveData(@Param("list") List<BEquipmentElevatorDataAt> bareSoilDataList);

    BasePage<Map<String, Object>> selectPage(BasePage<Object> parse, @Param("data")EquipmentElevatorDataAtBO elevatorDataAtBO);
}

package com.kakarote.build.equipment.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.equipment.entity.BO.EquipmentElevatorDataAtBO;
import com.kakarote.build.equipment.entity.PO.EquipmentElevatorDataAt;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 升降电梯设备实时数据表 Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2022-03-22
 */
public interface EquipmentElevatorDataAtMapper extends BaseMapper<EquipmentElevatorDataAt> {

    @SqlParser(filter = true)
    int batchSaveData(@Param("list") List<EquipmentElevatorDataAt> bareSoilDataList);

    BasePage<Map<String, Object>> selectPage(BasePage<Object> parse, @Param("data")EquipmentElevatorDataAtBO elevatorDataAtBO);
}

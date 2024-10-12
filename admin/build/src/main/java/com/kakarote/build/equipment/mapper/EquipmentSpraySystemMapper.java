package com.kakarote.build.equipment.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.equipment.entity.BO.EquipmentSpraySystemBO;
import com.kakarote.build.equipment.entity.PO.EquipmentSpraySystem;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自动喷淋系统 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-06-01
 */
public interface EquipmentSpraySystemMapper extends BaseMapper<EquipmentSpraySystem> {

    BasePage<EquipmentSpraySystem> selectByPage(BasePage<Object> parse, @Param("data") EquipmentSpraySystemBO spraySystemBO);

    BasePage<EquipmentSpraySystem> selectByPageOperation(BasePage<Object> parse, @Param("data") EquipmentSpraySystemBO spraySystemBO);


    List<Map<String, Object>> selectPieData();

    List<Map<String, Object>> selectBarData(@Param("data") Map<String, Object> params);

    @SqlParser(filter=true)
    List<Map<String, Object>> selectKqsdfxData(@Param("data") Map<String, Object> params);

    @SqlParser(filter=true)
    List<Map<String, Object>> selectPlscfxData(@Param("data") Map<String, Object> params);

    int selectassterNumber(@Param("data") Map<String, Object> params);

    @SqlParser(filter=true)
    List<Map<String,Object>> selectNoPage(@Param("data")Map<String,Object> params);

}

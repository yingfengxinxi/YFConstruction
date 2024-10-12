package com.qdoner.datarecept.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.qdoner.datarecept.entity.PO.BEquipmentDriverIdent;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 机械设备司机识别 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2022-07-12
 */
public interface BEquipmentDriverIdentMapper extends BaseMapper<BEquipmentDriverIdent> {

    @SqlParser(filter = true)
    int batchSaveData(@Param("list") List<BEquipmentDriverIdent> bareSoilDataList);
    @SqlParser(filter = true)
    BEquipmentDriverIdent getDriverIdentByCode(String equCode);
}

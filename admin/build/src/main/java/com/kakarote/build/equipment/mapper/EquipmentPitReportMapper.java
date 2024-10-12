package com.kakarote.build.equipment.mapper;

import com.kakarote.build.equipment.entity.BO.EquipmentPitReportBO;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.qdoner.build.equipment.entity.PO.EquipmentPitReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2023-05-07
 */
@Mapper
public interface EquipmentPitReportMapper extends BaseMapper<EquipmentPitReport> {

    BasePage<EquipmentPitReport>  selectPage(BasePage<Object> parse, @Param("data")EquipmentPitReportBO pitReportBO);
}

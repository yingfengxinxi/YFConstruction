package com.kakarote.build.equipment.mapper;

import com.kakarote.build.equipment.entity.BO.BEquipmentProBO;
import com.kakarote.build.equipment.entity.PO.BEquipmentPro;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 基坑/高支模工程管理  Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2021-12-23
 */
public interface BEquipmentProMapper extends BaseMapper<BEquipmentPro> {
    BasePage<BEquipmentPro> selectByPage(BasePage<BEquipmentPro> parse, @Param("data") BEquipmentProBO bEquipmentProBO);

}

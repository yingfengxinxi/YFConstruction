package com.kakarote.build.qual.mapper;


import com.kakarote.build.qual.entity.BO.BQualMarkingroomEquipmentBO;
import com.kakarote.build.qual.entity.PO.BQualMarkingroomEquipment;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 标养室设备信息 Mapper 接口
 * </p>
 *
 * @author zhang
 * @since 2022-07-14
 */
public interface BQualMarkingroomEquipmentMapper extends BaseMapper<BQualMarkingroomEquipment> {

    BasePage<BQualMarkingroomEquipment> selectDataList(BasePage<AdminMessage> parse, @Param("data") BQualMarkingroomEquipmentBO bQualMarkingroomEquipmentBO);
    List<BQualMarkingroomEquipment> listAll();

}

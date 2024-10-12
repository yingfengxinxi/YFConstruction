package com.kakarote.build.material.mapper;

import com.kakarote.build.equipment.entity.BO.BEquipmentCertificateBO;
import com.kakarote.build.material.entity.BO.BMaterialWeighingBO;
import com.kakarote.build.material.entity.PO.BMaterialWeighing;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 物料称重 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-08-11
 */
public interface BMaterialWeighingMapper extends BaseMapper<BMaterialWeighing> {
    BasePage<BMaterialWeighingBO> selectDataList(BasePage<AdminMessage> parse, @Param("data") BMaterialWeighingBO bMaterialWeighingBO);

}

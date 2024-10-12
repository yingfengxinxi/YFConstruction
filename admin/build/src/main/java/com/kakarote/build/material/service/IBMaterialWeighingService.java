package com.kakarote.build.material.service;

import com.kakarote.build.equipment.entity.BO.BEquipmentCertificateBO;
import com.kakarote.build.equipment.entity.PO.BEquipmentCertificate;
import com.kakarote.build.material.entity.BO.BMaterialWeighingBO;
import com.kakarote.build.material.entity.PO.BMaterialWeighing;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 物料称重 服务类
 * </p>
 *
 * @author author
 * @since 2021-08-11
 */
public interface IBMaterialWeighingService extends BaseService<BMaterialWeighing> {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BMaterialWeighingBO> selectList(BMaterialWeighingBO bMaterialWeighingBO);

    public String add(BMaterialWeighing bMaterialWeighing);

    public String edit(BMaterialWeighing bMaterialWeighing);

}

package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.BQualMarkingroomEquipmentBO;
import com.kakarote.build.qual.entity.PO.BQualMarkingroomEquipment;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 标养室设备信息 服务类
 * </p>
 *
 * @author zhang
 * @since 2022-07-14
 */
public interface IBQualMarkingroomEquipmentService extends BaseService<BQualMarkingroomEquipment> {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BQualMarkingroomEquipment> selectDataList(BQualMarkingroomEquipmentBO bQualMarkingroomEquipmentBO);

    public List<BQualMarkingroomEquipment> listAll();
}

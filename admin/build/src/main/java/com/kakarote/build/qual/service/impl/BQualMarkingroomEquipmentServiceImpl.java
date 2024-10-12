package com.kakarote.build.qual.service.impl;

import com.kakarote.build.qual.entity.BO.BQualMarkingroomEquipmentBO;
import com.kakarote.build.qual.entity.PO.BQualMarkingroomEquipment;
import com.kakarote.build.qual.mapper.BQualMarkingroomEquipmentMapper;
import com.kakarote.build.qual.service.IBQualMarkingroomEquipmentService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 标养室设备信息 服务实现类
 * </p>
 *
 * @author zhang
 * @since 2022-07-14
 */
@Service
public class BQualMarkingroomEquipmentServiceImpl extends BaseServiceImpl<BQualMarkingroomEquipmentMapper, BQualMarkingroomEquipment> implements IBQualMarkingroomEquipmentService {

    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BQualMarkingroomEquipment> selectDataList(BQualMarkingroomEquipmentBO bQualMarkingroomEquipmentBO)
    {
        BasePage<BQualMarkingroomEquipment> adminMessageBasePage = getBaseMapper().selectDataList(bQualMarkingroomEquipmentBO.parse(),bQualMarkingroomEquipmentBO);
        return adminMessageBasePage;
    }
    public List<BQualMarkingroomEquipment> listAll()    {
        List<BQualMarkingroomEquipment> adminMessageBasePage = getBaseMapper().listAll();
        return adminMessageBasePage;
    }
}

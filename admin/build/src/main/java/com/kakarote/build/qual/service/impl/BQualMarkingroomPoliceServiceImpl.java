package com.kakarote.build.qual.service.impl;

import com.kakarote.build.qual.entity.BO.BQualMarkingroomPoliceBO;
import com.kakarote.build.qual.entity.PO.BQualMarkingroomPolice;
import com.kakarote.build.qual.mapper.BQualMarkingroomPoliceMapper;
import com.kakarote.build.qual.service.IBQualMarkingroomPoliceService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标养室报警信息 服务实现类
 * </p>
 *
 * @author zhang
 * @since 2022-07-14
 */
@Service
public class BQualMarkingroomPoliceServiceImpl extends BaseServiceImpl<BQualMarkingroomPoliceMapper, BQualMarkingroomPolice> implements IBQualMarkingroomPoliceService {

    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BQualMarkingroomPolice> selectDataList(BQualMarkingroomPoliceBO bQualMarkingroomPoliceBO)
    {
        BasePage<BQualMarkingroomPolice> adminMessageBasePage = getBaseMapper().selectDataList(bQualMarkingroomPoliceBO.parse(),bQualMarkingroomPoliceBO);
        return adminMessageBasePage;
    }
}

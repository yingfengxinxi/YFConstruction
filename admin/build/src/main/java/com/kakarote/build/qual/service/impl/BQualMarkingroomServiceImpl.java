package com.kakarote.build.qual.service.impl;

import com.kakarote.build.qual.entity.BO.BQualMarkingroomBO;
import com.kakarote.build.qual.entity.PO.BQualMarkingroom;
import com.kakarote.build.qual.mapper.BQualMarkingroomMapper;
import com.kakarote.build.qual.service.IBQualMarkingroomService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 标养室基本信息 服务实现类
 * </p>
 *
 * @author zhang
 * @since 2022-07-14
 */
@Service
public class BQualMarkingroomServiceImpl extends BaseServiceImpl<BQualMarkingroomMapper, BQualMarkingroom> implements IBQualMarkingroomService {

    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BQualMarkingroom> selectDataList(BQualMarkingroomBO bQualMarkingroomBO)
    {
        BasePage<BQualMarkingroom> adminMessageBasePage = getBaseMapper().selectDataList(bQualMarkingroomBO.parse(),bQualMarkingroomBO);
        return adminMessageBasePage;
    }
    public List<BQualMarkingroom> listAll(){
        List<BQualMarkingroom> adminMessageBasePage = getBaseMapper().listAll();
        return adminMessageBasePage;
    }
}

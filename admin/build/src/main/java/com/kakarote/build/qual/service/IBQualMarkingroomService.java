package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.BQualMarkingroomBO;
import com.kakarote.build.qual.entity.PO.BQualMarkingroom;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 标养室基本信息 服务类
 * </p>
 *
 * @author zhang
 * @since 2022-07-14
 */
public interface IBQualMarkingroomService extends BaseService<BQualMarkingroom> {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BQualMarkingroom> selectDataList(BQualMarkingroomBO bQualMarkingroomBO);
    public List<BQualMarkingroom> listAll();

}

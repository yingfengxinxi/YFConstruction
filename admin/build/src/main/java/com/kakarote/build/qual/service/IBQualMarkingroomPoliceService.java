package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.BQualMarkingroomPoliceBO;
import com.kakarote.build.qual.entity.PO.BQualMarkingroomPolice;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 标养室报警信息 服务类
 * </p>
 *
 * @author zhang
 * @since 2022-07-14
 */
public interface IBQualMarkingroomPoliceService extends BaseService<BQualMarkingroomPolice> {
    /**
     * 查询数据列表
     *
     */
    public BasePage<BQualMarkingroomPolice> selectDataList(BQualMarkingroomPoliceBO bQualMarkingroomPoliceBO);

}

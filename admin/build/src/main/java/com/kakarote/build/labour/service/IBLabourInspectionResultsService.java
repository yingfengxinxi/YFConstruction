package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.PO.BLabour5gvideo;
import com.kakarote.build.labour.entity.PO.BLabourInspectionResults;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 企业报验结果表 服务类
 * </p>
 *
 * @author zhang
 * @since 2022-11-30
 */
public interface IBLabourInspectionResultsService extends BaseService<BLabourInspectionResults> {

    public String add(BLabourInspectionResults bLabourInspectionResults);
    public BLabourInspectionResults getPid(String pid);
}

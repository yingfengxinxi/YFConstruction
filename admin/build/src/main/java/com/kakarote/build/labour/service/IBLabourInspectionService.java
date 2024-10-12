package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.BO.BLabour5gvideoBO;
import com.kakarote.build.labour.entity.BO.BLabourInspectionBO;
import com.kakarote.build.labour.entity.PO.BLabour5gvideo;
import com.kakarote.build.labour.entity.PO.BLabourInspection;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * 企业报验 服务类
 * </p>
 *
 * @author zhang
 * @since 2022-11-30
 */
public interface IBLabourInspectionService extends BaseService<BLabourInspection> {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BLabourInspectionBO> selectList(BLabourInspectionBO bLabourInspectionBO);

    public String add(BLabourInspection bLabourInspection);

    public String edit(BLabourInspection bLabourInspection);

    public Map<String, Object> inspectionStatistical();

    BasePage<Map> selectApplyList(BLabourInspectionBO bLabourInspectionBO);

    Map<String, Object> selectApplyAndProjectInfo(String id);

    BasePage<Map> selectAllDataList(BLabourInspectionBO bLabourInspectionBO);
}

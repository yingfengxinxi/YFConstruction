package com.kakarote.build.safety.service;

import com.kakarote.build.safety.entity.BO.SafetyInspectionInfoBO;
import com.kakarote.build.safety.entity.PO.SafetyInspectionInfo;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * 施工安全--安全巡检信息 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-04-08
 */
public interface ISafetyInspectionInfoService extends BaseService<SafetyInspectionInfo> {

    BasePage<SafetyInspectionInfo> selectByPage(SafetyInspectionInfoBO inspectionInfoBO);

    /**
     * 根据Id查询安全巡检信息组子表数据
     * @param id
     * @return inspectionInfo 主表数据  inspectionDetailList 子表数据列表
     */
    Map<String, Object> selectDataById(Integer id);

    /**
     *
     * @param params inspectionInfo 主表数据  inspectionDetailList 子表数据列表
     * @return
     */
    Integer updateInspectionInfo(Map<String, Object> params);

    Map<String, Object> getPrintData(Integer id);

    //集团数据总览-隐患-安全隐患-四个块数据
    Map<String, Object> getCount(Integer projectId);

    BasePage<Map<String, Object>> selectTimeoutList(SafetyInspectionInfoBO inspectionInfoBO);

    Result dangeTimeoutPush(Integer id, Integer detailId);
}

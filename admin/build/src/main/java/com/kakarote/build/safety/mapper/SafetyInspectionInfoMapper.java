package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.SafetyInspectionInfoBO;
import com.kakarote.build.safety.entity.PO.SafetyInspectionInfo;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工安全--安全巡检信息 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-04-08
 */
public interface SafetyInspectionInfoMapper extends BaseMapper<SafetyInspectionInfo> {

    BasePage<SafetyInspectionInfo> selectByPage(BasePage<SafetyInspectionInfo> parse,
                                                @Param("data")SafetyInspectionInfoBO inspectionInfoBO);

    /**
     * 定时任务批量更新
     */
    int batchSaveInfo(@Param("list") List<SafetyInspectionInfo> inspectionPlanList);

    Map<String, Object> getPrintData(@Param("id") Integer id);

    //集团数据总览-隐患-安全隐患-四个块数据
    Map<String, Object> getCountMapper(@Param("projectId") Integer projectId);

    BasePage<Map<String, Object>> selectTimeoutList(BasePage<Object> parse, @Param("data") SafetyInspectionInfoBO inspectionInfoBO);

    SafetyInspectionInfo getInspectionInfo(@Param("id") Integer id);
}

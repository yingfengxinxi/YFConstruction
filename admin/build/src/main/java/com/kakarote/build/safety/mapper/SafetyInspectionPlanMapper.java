package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.SafetyInspectionPlanBO;
import com.kakarote.build.safety.entity.PO.SafetyInspectionInfo;
import com.kakarote.build.safety.entity.PO.SafetyInspectionPlan;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-04-07
 */
public interface SafetyInspectionPlanMapper extends BaseMapper<SafetyInspectionPlan> {

    BasePage<SafetyInspectionPlan> selectByPage(BasePage<SafetyInspectionPlan> parse,
                                                @Param("data")SafetyInspectionPlanBO inspectionPlanBO);

    /**
     * 定时任务查询计划列表
     * @return
     */
    List<SafetyInspectionPlan> selectPlanList();

    int batchUpdatePlan(@Param("list") List<SafetyInspectionPlan> inspectionPlanList);

    List<SafetyInspectionPlan> selectOptions();
}

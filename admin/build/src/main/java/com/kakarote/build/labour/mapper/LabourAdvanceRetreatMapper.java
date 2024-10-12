package com.kakarote.build.labour.mapper;

import com.kakarote.build.labour.entity.BO.LabourAdvanceRetreatBO;
import com.kakarote.build.labour.entity.PO.LabourAdvanceRetreat;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 人员进场退场管理 Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2022-04-20
 */
public interface LabourAdvanceRetreatMapper extends BaseMapper<LabourAdvanceRetreat> {

    BasePage<Map<String, Object>> selectPageList(BasePage<LabourAdvanceRetreat> page, @Param("data") LabourAdvanceRetreatBO advanceRetreatBO);
}

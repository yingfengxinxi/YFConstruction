package com.kakarote.build.labour.mapper;

import com.kakarote.build.labour.entity.BO.LabourSafetyOfficerBO;
import com.kakarote.build.labour.entity.PO.LabourSafetyOfficer;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 证件管理--安全从业人员证件台账 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-11-10
 */
public interface LabourSafetyOfficerMapper extends BaseMapper<LabourSafetyOfficer> {

    BasePage<Map> getList(BasePage<Object> parse, @Param("data") LabourSafetyOfficerBO labourSafetyOfficerBO);

    Map getInfoById(@Param("id") Integer id);

    BasePage<Map> getListNew(BasePage<Object> parse, @Param("data") LabourSafetyOfficerBO labourSafetyOfficerBO);
}

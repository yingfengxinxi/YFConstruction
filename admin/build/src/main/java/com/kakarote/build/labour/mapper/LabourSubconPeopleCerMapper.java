package com.kakarote.build.labour.mapper;

import com.kakarote.build.labour.entity.BO.LabourSpeworktypeBO;
import com.kakarote.build.labour.entity.BO.LabourSubconPeopleCerBO;
import com.kakarote.build.labour.entity.PO.LabourSpeworktype;
import com.kakarote.build.labour.entity.PO.LabourSubconPeopleCer;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 证件管理---分包管理人员证件台账 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-03-26
 */
public interface LabourSubconPeopleCerMapper extends BaseMapper<LabourSubconPeopleCer> {

    BasePage<LabourSubconPeopleCer> selectList(BasePage<LabourSubconPeopleCer> page, @Param("data") LabourSubconPeopleCerBO labourSubconPeopleCerBO);

    BasePage<Map> selectTabsByInfo(BasePage<LabourSpeworktype> page, @Param("data") LabourSpeworktypeBO labourSpeworktypeBO);


    BasePage<Map> selectTabsByInfoNew(BasePage<Object> parse, @Param("data") LabourSpeworktypeBO labourSpeworktypeBO);
}

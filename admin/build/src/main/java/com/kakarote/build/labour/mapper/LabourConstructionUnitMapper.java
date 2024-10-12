package com.kakarote.build.labour.mapper;

import com.kakarote.build.labour.entity.BO.LabourConstructionUnitBO;
import com.kakarote.build.labour.entity.PO.LabourConstructionUnit;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 建设单位管理 Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2022-04-20
 */
public interface LabourConstructionUnitMapper extends BaseMapper<LabourConstructionUnit> {

    BasePage<Map<String, Object>> selectPageList(BasePage<LabourConstructionUnit> page, @Param("data") LabourConstructionUnitBO constructionUnitBO);

    List<Map<String,Object>> selectUnitAll();

}

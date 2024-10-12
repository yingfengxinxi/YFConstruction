package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.BSafetyProductionBO;
import com.kakarote.build.safety.entity.PO.BSafetyProduction;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 安全生产评价 Mapper 接口
 * </p>
 *
 * @author zjc
 * @since 2022-06-08
 */
public interface BSafetyProductionMapper extends BaseMapper<BSafetyProduction> {

    BasePage<BSafetyProductionBO> selectDataList(BasePage<AdminMessage> parse, @Param("data") BSafetyProductionBO bSafetyProductionBO);
}

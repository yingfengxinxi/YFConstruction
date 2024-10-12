package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.SafetyMixPlantDataBO;
import com.kakarote.build.safety.entity.PO.SafetyMixPlantData;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 拌合站监控基本信息 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2022-08-30
 */
public interface SafetyMixPlantDataMapper extends BaseMapper<SafetyMixPlantData> {

    BasePage<SafetyMixPlantData> selectByPage(BasePage<Object> parse, @Param("data") SafetyMixPlantDataBO safetyMixPlantDataBO);
}

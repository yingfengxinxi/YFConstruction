package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.SafetyMixPlantBO;
import com.kakarote.build.safety.entity.PO.SafetyMixPlant;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 拌合站监控 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-06-18
 */
public interface SafetyMixPlantMapper extends BaseMapper<SafetyMixPlant> {

    BasePage<SafetyMixPlant> selectByPage(BasePage<Object> parse, @Param("data") SafetyMixPlantBO safetyMixPlantBO);
}

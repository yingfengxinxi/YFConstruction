package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.SafetyPerilBo;
import com.kakarote.build.safety.entity.PO.SafetyPeril;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 危大工程清单 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2021-03-29
 */
public interface SafetyPerilMapper extends BaseMapper<SafetyPeril> {

    BasePage<SafetyPeril> selectByPage(BasePage<SafetyPeril> parse, @Param("data") SafetyPerilBo safetyPerilBo);
}

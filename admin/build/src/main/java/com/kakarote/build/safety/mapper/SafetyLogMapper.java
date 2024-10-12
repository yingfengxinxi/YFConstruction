package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.BO.SafetyLogBO;
import com.kakarote.build.safety.entity.PO.SafetyLog;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 安全日志 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2021-04-06
 */
public interface SafetyLogMapper extends BaseMapper<SafetyLog> {

    BasePage<SafetyLog> selectList(BasePage<AdminMessage> parse, @Param("data")SafetyLogBO safetyLogBO);

}

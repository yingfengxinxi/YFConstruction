package com.kakarote.build.safety.service;

import com.kakarote.build.safety.entity.BO.SafetyLogBO;
import com.kakarote.build.safety.entity.PO.SafetyLog;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 安全日志 服务类
 * </p>
 *
 * @author wudw
 * @since 2021-04-06
 */
public interface ISafetyLogService extends BaseService<SafetyLog> {


    BasePage<SafetyLog> selectList(SafetyLogBO safetyLogBO);

}

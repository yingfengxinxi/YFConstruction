package com.kakarote.build.safety.service;

import com.kakarote.build.safety.entity.BO.SafetyPerilBo;
import com.kakarote.build.safety.entity.PO.SafetyPeril;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 危大工程清单 服务类
 * </p>
 *
 * @author wudw
 * @since 2021-03-29
 */
public interface ISafetyPerilService extends BaseService<SafetyPeril> {

    BasePage<SafetyPeril> selectByPage(SafetyPerilBo safetyPerilBo);

}

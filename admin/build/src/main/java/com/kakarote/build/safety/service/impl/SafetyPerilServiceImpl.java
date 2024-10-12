package com.kakarote.build.safety.service.impl;

import com.kakarote.build.safety.entity.BO.SafetyPerilBo;
import com.kakarote.build.safety.entity.PO.SafetyPeril;
import com.kakarote.build.safety.mapper.SafetyPerilMapper;
import com.kakarote.build.safety.service.ISafetyPerilService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 危大工程清单 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-03-29
 */
@Service
public class SafetyPerilServiceImpl extends BaseServiceImpl<SafetyPerilMapper, SafetyPeril> implements ISafetyPerilService {

    @Override
    public BasePage<SafetyPeril> selectByPage(SafetyPerilBo safetyPerilBo) {
        return this.getBaseMapper().selectByPage(safetyPerilBo.parse(),safetyPerilBo);
    }
}

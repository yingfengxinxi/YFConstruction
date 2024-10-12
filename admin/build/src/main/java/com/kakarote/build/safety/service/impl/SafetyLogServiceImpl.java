package com.kakarote.build.safety.service.impl;

import com.kakarote.build.safety.entity.BO.SafetyLogBO;
import com.kakarote.build.safety.entity.PO.SafetyLog;
import com.kakarote.build.safety.mapper.SafetyLogMapper;
import com.kakarote.build.safety.service.ISafetyLogService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 安全日志 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-04-06
 */
@Service
public class SafetyLogServiceImpl extends BaseServiceImpl<SafetyLogMapper, SafetyLog> implements ISafetyLogService {


    @Override
    public BasePage<SafetyLog> selectList(SafetyLogBO safetyLogBO) {

            BasePage<SafetyLog> list = getBaseMapper().selectList(safetyLogBO.parse(),safetyLogBO);
        return list;
    }
}

package com.kakarote.build.jg.service.impl;

import com.kakarote.build.jg.entity.BO.JgSafetyDangerRecordBO;
import com.kakarote.build.jg.entity.PO.JgSafetyDangerRecord;
import com.kakarote.build.jg.mapper.JgSafetyDangerRecordMapper;
import com.kakarote.build.jg.service.IJgSafetyDangerRecordService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 安全隐患记录 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-11-26
 */
@Service
public class JgSafetyDangerRecordServiceImpl extends BaseServiceImpl<JgSafetyDangerRecordMapper, JgSafetyDangerRecord> implements IJgSafetyDangerRecordService {

    @Override
    public BasePage<JgSafetyDangerRecord> selectByPage(JgSafetyDangerRecordBO jgSafetyDangerRecordBO) {
        return getBaseMapper().selectByPage(jgSafetyDangerRecordBO.parse(),jgSafetyDangerRecordBO);
    }
}

package com.kakarote.build.jg.service.impl;

import com.kakarote.build.jg.entity.BO.JgEnforceRecordBO;
import com.kakarote.build.jg.entity.PO.JgEnforceRecord;
import com.kakarote.build.jg.mapper.JgEnforceRecordMapper;
import com.kakarote.build.jg.service.IJgEnforceRecordService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 视频监控执法记录 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-11-25
 */
@Service
public class JgEnforceRecordServiceImpl extends BaseServiceImpl<JgEnforceRecordMapper, JgEnforceRecord> implements IJgEnforceRecordService {

    @Override
    public BasePage<JgEnforceRecord> selectByPage(JgEnforceRecordBO jgEnforceRecordBO) {
        jgEnforceRecordBO.setOrdersBd("create_time",false);
        return getBaseMapper().selectByPage(jgEnforceRecordBO.parse(),jgEnforceRecordBO);
    }
}

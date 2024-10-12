package com.kakarote.build.jg.service.impl;

import com.kakarote.build.jg.entity.BO.JgEducationTrainRecordBO;
import com.kakarote.build.jg.entity.PO.JgEducationTrainRecord;
import com.kakarote.build.jg.mapper.JgEducationTrainRecordMapper;
import com.kakarote.build.jg.service.IJgEducationTrainRecordService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 安全教育培训记录 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-11-23
 */
@Service
public class JgEducationTrainRecordServiceImpl extends BaseServiceImpl<JgEducationTrainRecordMapper, JgEducationTrainRecord> implements IJgEducationTrainRecordService {

    @Override
    public BasePage<JgEducationTrainRecord> selectByPage(JgEducationTrainRecordBO jgEducationTrainRecordBO) {
        jgEducationTrainRecordBO.setOrdersBd("create_time",false);
        return getBaseMapper().selectByPage(jgEducationTrainRecordBO.parse(),jgEducationTrainRecordBO);
    }
}

package com.kakarote.build.jg.service;

import com.kakarote.build.jg.entity.BO.JgEducationTrainRecordBO;
import com.kakarote.build.jg.entity.PO.JgEducationTrainRecord;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 安全教育培训记录 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-11-23
 */
public interface IJgEducationTrainRecordService extends BaseService<JgEducationTrainRecord> {

    BasePage<JgEducationTrainRecord> selectByPage(JgEducationTrainRecordBO jgEducationTrainRecordBO);
}

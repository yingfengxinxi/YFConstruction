package com.kakarote.build.jg.service;

import com.kakarote.build.jg.entity.BO.JgSafetyDangerRecordBO;
import com.kakarote.build.jg.entity.PO.JgSafetyDangerRecord;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 安全隐患记录 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-11-26
 */
public interface IJgSafetyDangerRecordService extends BaseService<JgSafetyDangerRecord> {

    BasePage<JgSafetyDangerRecord> selectByPage(JgSafetyDangerRecordBO jgSafetyDangerRecordBO);
}

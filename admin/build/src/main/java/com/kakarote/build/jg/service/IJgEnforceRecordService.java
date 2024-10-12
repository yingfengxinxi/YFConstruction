package com.kakarote.build.jg.service;

import com.kakarote.build.jg.entity.BO.JgEnforceRecordBO;
import com.kakarote.build.jg.entity.PO.JgEnforceRecord;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 视频监控执法记录 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-11-25
 */
public interface IJgEnforceRecordService extends BaseService<JgEnforceRecord> {

    BasePage<JgEnforceRecord> selectByPage(JgEnforceRecordBO jgEnforceRecordBO);
}

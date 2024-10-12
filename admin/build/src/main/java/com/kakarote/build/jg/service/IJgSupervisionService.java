package com.kakarote.build.jg.service;

import com.kakarote.build.jg.entity.BO.JgSupervisionBO;
import com.kakarote.build.jg.entity.PO.JgSupervision;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 现场安全监督 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-11-25
 */
public interface IJgSupervisionService extends BaseService<JgSupervision> {

    BasePage<JgSupervision> selectByPage(JgSupervisionBO jgSupervisionBO);
}

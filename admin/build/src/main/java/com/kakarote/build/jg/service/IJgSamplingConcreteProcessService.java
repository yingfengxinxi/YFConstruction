package com.kakarote.build.jg.service;

import com.kakarote.build.jg.entity.BO.JgSamplingConcreteProcessBO;
import com.kakarote.build.jg.entity.PO.JgSamplingConcrete;
import com.kakarote.build.jg.entity.PO.JgSamplingConcreteProcess;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzy
 * @since 2021-11-27
 */
public interface IJgSamplingConcreteProcessService extends BaseService<JgSamplingConcreteProcess> {

    BasePage<JgSamplingConcreteProcess> selectByPage(JgSamplingConcreteProcessBO samplingConcreteProcessBO);

    String add(JgSamplingConcreteProcess jgSamplingConcreteProcess);

    String edit(JgSamplingConcreteProcess jgSamplingConcreteProcess);

    List<JgSamplingConcreteProcess> getByPid(String pid);
}

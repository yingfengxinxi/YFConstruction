package com.kakarote.build.jg.service;

import com.kakarote.build.jg.entity.BO.JgSamplingConcreteBO;
import com.kakarote.build.jg.entity.PO.JgSamplingConcrete;
import com.kakarote.build.qual.entity.PO.BQualSamplingWitness;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzy
 * @since 2021-11-27
 */
public interface IJgSamplingConcreteService extends BaseService<JgSamplingConcrete> {

    BasePage<JgSamplingConcrete> selectByPage(JgSamplingConcreteBO samplingConcreteBO);

    String add(JgSamplingConcrete jgSamplingConcrete);

    String edit(JgSamplingConcrete jgSamplingConcrete);
}

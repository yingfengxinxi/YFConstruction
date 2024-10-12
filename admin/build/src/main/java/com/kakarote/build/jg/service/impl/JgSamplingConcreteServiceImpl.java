package com.kakarote.build.jg.service.impl;

import com.kakarote.build.jg.entity.BO.JgSamplingConcreteBO;
import com.kakarote.build.jg.entity.PO.JgSamplingConcrete;
import com.kakarote.build.jg.mapper.JgSamplingConcreteMapper;
import com.kakarote.build.jg.service.IJgSamplingConcreteService;
import com.kakarote.build.qual.entity.PO.BQualSamplingWitness;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-11-27
 */
@Service
public class JgSamplingConcreteServiceImpl extends BaseServiceImpl<JgSamplingConcreteMapper, JgSamplingConcrete> implements IJgSamplingConcreteService {

    @Override
    public BasePage<JgSamplingConcrete> selectByPage(JgSamplingConcreteBO samplingConcreteBO) {
        samplingConcreteBO.setOrdersBd("create_time",false);
        return getBaseMapper().selectByPage(samplingConcreteBO.parse(), samplingConcreteBO);
    }

    @Override
    public String add(JgSamplingConcrete jgSamplingConcrete) {

        jgSamplingConcrete.setCreateTime(new Date());
        this.save(jgSamplingConcrete);
        return"";
    }

    @Override
    public String edit(JgSamplingConcrete jgSamplingConcrete) {
        this.updateById(jgSamplingConcrete);
        return"";
    }
}

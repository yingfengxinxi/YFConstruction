package com.kakarote.build.jg.service.impl;

import com.kakarote.build.jg.entity.BO.JgSamplingConcreteProcessBO;
import com.kakarote.build.jg.entity.PO.JgSamplingConcreteProcess;
import com.kakarote.build.jg.mapper.JgSamplingConcreteProcessMapper;
import com.kakarote.build.jg.service.IJgSamplingConcreteProcessService;
import com.kakarote.build.qual.entity.PO.BQualSamplingProcess;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-11-27
 */
@Service
public class JgSamplingConcreteProcessServiceImpl extends BaseServiceImpl<JgSamplingConcreteProcessMapper, JgSamplingConcreteProcess> implements IJgSamplingConcreteProcessService {

    @Override
    public BasePage<JgSamplingConcreteProcess> selectByPage(JgSamplingConcreteProcessBO samplingConcreteProcessBO) {
        return getBaseMapper().selectByPage(samplingConcreteProcessBO.parse(), samplingConcreteProcessBO);
    }

    @Override
    public String add(JgSamplingConcreteProcess jgSamplingConcreteProcess) {
        jgSamplingConcreteProcess.setCreateTime(new Date());
        this.save(jgSamplingConcreteProcess);
        return"";
    }

    @Override
    public String edit(JgSamplingConcreteProcess jgSamplingConcreteProcess) {
        this.updateById(jgSamplingConcreteProcess);
        return"";
    }

    @Override
    public List<JgSamplingConcreteProcess> getByPid(String pid) {
        List<JgSamplingConcreteProcess>  list = getBaseMapper().selectDataListPid(pid);
        return list;
    }
}

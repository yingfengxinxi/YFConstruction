package com.kakarote.build.jg.service.impl;

import com.kakarote.build.jg.entity.BO.JgSupervisionBO;
import com.kakarote.build.jg.entity.PO.JgSupervision;
import com.kakarote.build.jg.mapper.JgSupervisionMapper;
import com.kakarote.build.jg.service.IJgSupervisionService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 现场安全监督 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-11-25
 */
@Service
public class JgSupervisionServiceImpl extends BaseServiceImpl<JgSupervisionMapper, JgSupervision> implements IJgSupervisionService {

    @Override
    public BasePage<JgSupervision> selectByPage(JgSupervisionBO jgSupervisionBO) {
        return getBaseMapper().selectByPage(jgSupervisionBO.parse(),jgSupervisionBO);
    }
}

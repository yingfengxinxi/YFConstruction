package com.kakarote.admin.demo.service.impl;

import com.kakarote.admin.demo.entity.BO.WfDemoBO;
import com.kakarote.admin.demo.entity.PO.WfDemo;
import com.kakarote.admin.demo.mapper.WfDemoMapper;
import com.kakarote.admin.demo.service.IWfDemoService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * DEMO表 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-03-15
 */
@Service
public class WfDemoServiceImpl extends BaseServiceImpl<WfDemoMapper, WfDemo> implements IWfDemoService {

    @Override
    public BasePage<WfDemo> selectByPage(WfDemoBO demoBO) {
        BasePage<WfDemo> serviceBasePage = baseMapper.selectByPage(demoBO.parse(), demoBO);
        return serviceBasePage;
    }
}

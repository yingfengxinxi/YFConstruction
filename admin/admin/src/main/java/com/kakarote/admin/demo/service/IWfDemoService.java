package com.kakarote.admin.demo.service;

import com.kakarote.admin.demo.entity.BO.WfDemoBO;
import com.kakarote.admin.demo.entity.PO.WfDemo;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * DEMO表 服务类
 * </p>
 *
 * @author wudw
 * @since 2021-03-15
 */
public interface IWfDemoService extends BaseService<WfDemo> {
    BasePage<WfDemo> selectByPage(WfDemoBO demoBO);
}

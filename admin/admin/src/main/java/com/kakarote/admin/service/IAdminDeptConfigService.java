package com.kakarote.admin.service;

import com.kakarote.admin.entity.BO.AdminDeptConfigBO;
import com.kakarote.admin.entity.PO.AdminDeptConfig;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2020-12-16
 */
public interface IAdminDeptConfigService extends BaseService<AdminDeptConfig> {

    BasePage<AdminDeptConfig> queryConfigList(AdminDeptConfigBO adminDeptConfigBO);

    Result addOrUpdateConfig(AdminDeptConfig adminDeptConfig);
}

package com.kakarote.admin.service;

import com.kakarote.admin.entity.PO.AdminInstrumentInfo;
import com.kakarote.admin.entity.PO.AdminMenu;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 仪表盘名称表 服务类
 * </p>
 *
 * @author author
 * @since 2020-12-08
 */
public interface IAdminInstrumentInfoService extends BaseService<AdminInstrumentInfo> {
//    public String getNameByModelId(Integer modelId);
    public  int update(AdminInstrumentInfo adminInstrumentInfo);
}

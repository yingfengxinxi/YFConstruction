package com.kakarote.admin.service;

import com.kakarote.admin.entity.BO.SysAreaBO;
import com.kakarote.admin.entity.PO.SysArea;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 区划表 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-03-18
 */
public interface ISysAreaService extends BaseService<SysArea> {

    /**
     * 查询区划列表
     */
    List<SysArea> selectAreaList(SysAreaBO sysAreaBO);

}

package com.kakarote.admin.service;

import com.kakarote.admin.entity.PO.AdminRoleMenuDp;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shz
 * @since 2022-09-07
 */
public interface IAdminRoleMenuDpService extends BaseService<AdminRoleMenuDp> {

    boolean updateRoleMenu(Map map);

    Map getByUser();

}

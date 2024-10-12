package com.kakarote.admin.service;

import com.kakarote.admin.entity.BO.AdminDeptBO;
import com.kakarote.admin.entity.BO.AdminUserBO;
import com.kakarote.admin.entity.PO.AdminDept;
import com.kakarote.admin.entity.PO.AdminDeptMenu;
import com.kakarote.admin.entity.VO.AdminDeptVO;
import com.kakarote.admin.entity.VO.AdminUserVO;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

public interface IAdminTenantService extends BaseService<AdminDeptMenu> {

    public List<AdminDeptVO> queryDeptList();

    public void saveDeptMenu(Integer deptId, List<Integer> menuIdList);

    public void updateDeptMenu(AdminDept adminDept);

    public List<AdminDept> getRoleByType();

    void copyDataOnNewTenant(Integer tenantId,String name);

    BasePage<AdminUserVO> queryUserList(AdminUserBO adminUserBO);

    void addTenant(AdminDeptBO adminDeptBO);
    void addTenantAdmin(AdminUserVO adminUserVO);
}

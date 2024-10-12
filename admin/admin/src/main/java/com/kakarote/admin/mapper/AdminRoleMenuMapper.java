package com.kakarote.admin.mapper;

import com.kakarote.admin.entity.PO.AdminDictData;
import com.kakarote.admin.entity.PO.AdminRoleMenu;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 角色菜单对应关系表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
public interface AdminRoleMenuMapper extends BaseMapper<AdminRoleMenu> {

    int insertFromDept(@Param("roleId") Integer roleId,@Param("deptId") Integer deptId);

    int copyWorkRoleMenu(@Param("tenantId") Integer tenantId);
}

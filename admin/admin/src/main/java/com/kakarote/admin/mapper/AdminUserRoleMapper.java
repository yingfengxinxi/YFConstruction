package com.kakarote.admin.mapper;

import com.kakarote.admin.entity.PO.AdminUserRole;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户角色对应关系表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
public interface AdminUserRoleMapper extends BaseMapper<AdminUserRole> {

    List<Integer> selectRoleTypeByUserId(@Param("userId") Long userId);
}

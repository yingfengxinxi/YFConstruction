package com.kakarote.admin.mapper;

import com.kakarote.admin.entity.PO.AdminDeptMenu;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminDeptMenuMapper extends BaseMapper<AdminDeptMenu> {
    public List<Integer> getDeptMenu(@Param("parentId") Integer parentId, @Param("deptId") Integer deptId);
    public List<Integer> getDeptMenu2(@Param("parentId") Integer parentId, @Param("deptId") Integer deptId);

}

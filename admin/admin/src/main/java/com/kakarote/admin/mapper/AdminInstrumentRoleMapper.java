package com.kakarote.admin.mapper;

import com.kakarote.admin.entity.BO.DeptVO;
import com.kakarote.admin.entity.PO.AdminInstrumentRole;
import com.kakarote.admin.entity.VO.AdminInstrumentRoleVO;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 仪表盘权限表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2020-12-08
 */
public interface AdminInstrumentRoleMapper extends BaseMapper<AdminInstrumentRole> {

    List<AdminInstrumentRoleVO> queryModelSort(@Param("roleId") Integer roleId);

    List<AdminInstrumentRoleVO> queryModelByRoleIds(@Param("roleIds") List<Integer> roleIds);
}

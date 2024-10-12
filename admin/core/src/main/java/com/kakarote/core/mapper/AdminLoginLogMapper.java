package com.kakarote.core.mapper;


import com.kakarote.core.entity.AdminLoginLog;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统登录日志表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
public interface AdminLoginLogMapper extends BaseMapper<AdminLoginLog> {
    public Integer queryLast(@Param("userName") String userName);
}

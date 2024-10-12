package com.kakarote.core.service;

import com.kakarote.core.entity.AdminLoginLog;
import com.kakarote.core.servlet.BaseService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统登录日志表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
public interface IAdminLoginLogService extends BaseService<AdminLoginLog> {

    Integer queryLast(String userName);
}

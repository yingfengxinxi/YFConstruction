package com.kakarote.core.service.impl;


import com.kakarote.core.entity.AdminLoginLog;
import com.kakarote.core.mapper.AdminLoginLogMapper;
import com.kakarote.core.service.IAdminLoginLogService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统登录日志表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@Service
public class AdminLoginLogServiceImpl extends BaseServiceImpl<AdminLoginLogMapper, AdminLoginLog> implements IAdminLoginLogService {

    @Override
    public Integer queryLast(String userName) {
        return getBaseMapper().queryLast(userName);
    }
}

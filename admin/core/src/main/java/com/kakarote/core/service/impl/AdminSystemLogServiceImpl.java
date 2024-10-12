package com.kakarote.core.service.impl;

import com.kakarote.core.entity.AdminSystemLog;
import com.kakarote.core.mapper.AdminSystemLogMapper;
import com.kakarote.core.service.IAdminSystemLogService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统日志表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@Service
public class AdminSystemLogServiceImpl extends BaseServiceImpl<AdminSystemLogMapper, AdminSystemLog> implements IAdminSystemLogService {

}

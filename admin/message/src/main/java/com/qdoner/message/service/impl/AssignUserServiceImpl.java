package com.qdoner.message.service.impl;

import com.qdoner.message.entity.PO.AssignUser;
import com.qdoner.message.mapper.AssignUserMapper;
import com.qdoner.message.service.IAssignUserService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-09-13
 */
@Service
public class AssignUserServiceImpl extends BaseServiceImpl<AssignUserMapper, AssignUser> implements IAssignUserService {

    @Override
    public List<Long> getAssignUser(Map data) {
        return getBaseMapper().getAssignUser(data);
    }
}

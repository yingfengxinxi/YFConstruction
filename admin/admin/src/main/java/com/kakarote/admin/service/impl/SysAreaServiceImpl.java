package com.kakarote.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.admin.entity.BO.SysAreaBO;
import com.kakarote.admin.entity.PO.SysArea;
import com.kakarote.admin.mapper.SysAreaMapper;
import com.kakarote.admin.service.ISysAreaService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 区划表 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-03-18
 */
@Service
public class SysAreaServiceImpl extends BaseServiceImpl<SysAreaMapper, SysArea> implements ISysAreaService {

    @Override
    public List<SysArea> selectAreaList(SysAreaBO sysAreaBO) {
        QueryWrapper wrapper = new QueryWrapper();
        if (sysAreaBO.getLevel() != null) {
            wrapper.eq("level", sysAreaBO.getLevel());
        }
        if (sysAreaBO.getParentId() != null){
            wrapper.eq("parent_id", sysAreaBO.getParentId());
        }
        wrapper.ne("tenant_id", 0);
        List<SysArea> list = getBaseMapper().selectList(wrapper);
        return list;
    }
}

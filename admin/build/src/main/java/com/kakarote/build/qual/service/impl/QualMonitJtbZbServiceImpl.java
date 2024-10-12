package com.kakarote.build.qual.service.impl;

import com.kakarote.build.qual.entity.PO.QualMonitJtbZb;
import com.kakarote.build.qual.mapper.QualMonitJtbZbMapper;
import com.kakarote.build.qual.service.IQualMonitJtbZbService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 检测检验--监测数据填报--子表 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-03-25
 */
@Service
public class QualMonitJtbZbServiceImpl extends BaseServiceImpl<QualMonitJtbZbMapper, QualMonitJtbZb> implements IQualMonitJtbZbService {

    @Override
    public List<Map> selectT(Integer pid) {
        return getBaseMapper().selectT(pid);
    }

    @Override
    public Integer deletByZHid(Integer pid) {
        return getBaseMapper().deletByZHid(pid);
    }
}

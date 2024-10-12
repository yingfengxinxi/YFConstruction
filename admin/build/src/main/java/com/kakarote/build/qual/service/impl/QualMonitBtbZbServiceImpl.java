package com.kakarote.build.qual.service.impl;

import com.kakarote.build.qual.entity.PO.QualMonitBtbZb;
import com.kakarote.build.qual.mapper.QualMonitBtbZbMapper;
import com.kakarote.build.qual.service.IQualMonitBtbZbService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标养室监测--监测数据填报--子表 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-03-25
 */
@Service
public class QualMonitBtbZbServiceImpl extends BaseServiceImpl<QualMonitBtbZbMapper, QualMonitBtbZb> implements IQualMonitBtbZbService {

    @Override
    public List<Map> selectT(Integer pid) {
        return getBaseMapper().selectT(pid);
    }

    @Override
    public Integer deletByZHid(Integer pid) {
        return getBaseMapper().deletByZHid(pid);
    }
}

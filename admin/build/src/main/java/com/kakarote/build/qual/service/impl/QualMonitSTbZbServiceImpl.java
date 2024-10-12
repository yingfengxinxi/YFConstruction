package com.kakarote.build.qual.service.impl;

import com.kakarote.build.qual.entity.BO.QualMonitSTbZbBO;
import com.kakarote.build.qual.entity.PO.QualMonitSTbZb;
import com.kakarote.build.qual.mapper.QualMonitSTbZbMapper;
import com.kakarote.build.qual.service.IQualMonitSTbZbService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 监测数据填报--子表 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-03-24
 */
@Service
public class QualMonitSTbZbServiceImpl extends BaseServiceImpl<QualMonitSTbZbMapper, QualMonitSTbZb> implements IQualMonitSTbZbService {

    @Override
    public BasePage<QualMonitSTbZb> selectList(QualMonitSTbZbBO qualMonitSTbZbBO) {
        BasePage<QualMonitSTbZb> qualMonitSTbZbBasePage = getBaseMapper().selectList(qualMonitSTbZbBO.parse(), qualMonitSTbZbBO);
        return qualMonitSTbZbBasePage;
    }

    @Override
    public List<QualMonitSTbZb> selectListNoPage(QualMonitSTbZbBO qualMonitSTbZbBO) {
        return getBaseMapper().selectListNoPage(qualMonitSTbZbBO);
    }

    @Override
    public List<Map> selectT(Integer pid) {
        List<Map> maps = getBaseMapper().selectT(pid);
        return maps;
    }

    @Override
    public Integer deletByZHid(Integer pid) {
        return getBaseMapper().deletByZHid(pid);
    }

    /**
     * 实测实量统计---超出范围，饼状图统计  全部
     * @return
     */
    @Override
    public List<Map> selectByOutRangeStaticsAll(Map map) {
        return getBaseMapper().selectByOutRangeStaticsAll(map);
    }

    @Override
    public List<Map> selectByOutRangeStaticsSelect(Map map) {
        return getBaseMapper().selectByOutRangeStaticsSelect(map);
    }


}

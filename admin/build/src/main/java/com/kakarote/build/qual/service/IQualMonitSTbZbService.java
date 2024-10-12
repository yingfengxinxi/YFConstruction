package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.QualMonitSTbZbBO;
import com.kakarote.build.qual.entity.PO.QualMonitSTbZb;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 监测数据填报--子表 服务类
 * </p>
 *
 * @author zy
 * @since 2021-03-24
 */
public interface IQualMonitSTbZbService extends BaseService<QualMonitSTbZb> {

    public BasePage<QualMonitSTbZb> selectList(QualMonitSTbZbBO qualMonitSTbZbBO);

    public List<QualMonitSTbZb> selectListNoPage(QualMonitSTbZbBO qualMonitSTbZbBO);

    public List<Map> selectT(Integer pid);

    Integer deletByZHid(Integer pid);

    //实测实量统计---超出范围，饼状图统计
    public List<Map> selectByOutRangeStaticsAll(Map map);

    //柱状图 根据code
    public List<Map> selectByOutRangeStaticsSelect(Map map);

}

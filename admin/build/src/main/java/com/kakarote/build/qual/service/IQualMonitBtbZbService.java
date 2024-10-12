package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.PO.QualMonitBtbZb;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标养室监测--监测数据填报--子表 服务类
 * </p>
 *
 * @author zy
 * @since 2021-03-25
 */
public interface IQualMonitBtbZbService extends BaseService<QualMonitBtbZb> {

    public List<Map> selectT(Integer pid);

    Integer deletByZHid(Integer pid);

}

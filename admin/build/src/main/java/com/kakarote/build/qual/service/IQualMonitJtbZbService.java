package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.PO.QualMonitJtbZb;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 检测检验--监测数据填报--子表 服务类
 * </p>
 *
 * @author zy
 * @since 2021-03-25
 */
public interface IQualMonitJtbZbService extends BaseService<QualMonitJtbZb> {

    public List<Map> selectT(Integer pid);

    Integer deletByZHid(Integer pid);

}

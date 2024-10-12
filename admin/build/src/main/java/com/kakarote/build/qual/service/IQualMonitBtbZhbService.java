package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.QualMonitSTbZhbBO;
import com.kakarote.build.qual.entity.BO.QualMonitsetBO;
import com.kakarote.build.qual.entity.PO.QualMonitBtbZhb;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 标养室监测--监测数据填报--主表 服务类
 * </p>
 *
 * @author zy
 * @since 2021-03-25
 */
public interface IQualMonitBtbZhbService extends BaseService<QualMonitBtbZhb> {

    public BasePage<QualMonitBtbZhb> selectList(QualMonitSTbZhbBO qualMonitSTbZhbBO);

    //主表新增，同步新增子表多条数据
    Boolean saveOrUpdateTB(QualMonitBtbZhb qualMonitSTbZhb, List<QualMonitsetBO> lists);

}

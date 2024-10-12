package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.QualMonitSTbZhbBO;
import com.kakarote.build.qual.entity.BO.QualMonitsetBO;
import com.kakarote.build.qual.entity.PO.QualMonitJtbZhb;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 检测检验--监测数据填报--主表 服务类
 * </p>
 *
 * @author zy
 * @since 2021-03-25
 */
public interface IQualMonitJtbZhbService extends BaseService<QualMonitJtbZhb> {

    public BasePage<QualMonitJtbZhb> selectList(QualMonitSTbZhbBO qualMonitSTbZhbBO);

    //主表新增，同步新增子表多条数据
    Boolean saveOrUpdateTB(QualMonitJtbZhb qualMonitSTbZhb, List<QualMonitsetBO> lists);

}

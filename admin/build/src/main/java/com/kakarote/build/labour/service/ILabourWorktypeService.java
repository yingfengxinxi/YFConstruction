package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.BO.LabourWorktypeBO;
import com.kakarote.build.labour.entity.PO.LabourWorktype;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 劳务管理--工种 服务类
 * </p>
 *
 * @author zy
 * @since 2021-03-15
 */
public interface ILabourWorktypeService extends BaseService<LabourWorktype> {

    public BasePage<LabourWorktype> selectList(LabourWorktypeBO labourWorktypeBO);

    public List<LabourWorktype> selcetIdsAndWorktype();
}

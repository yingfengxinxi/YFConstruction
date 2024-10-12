package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.BO.LabourSpeworktypeBO;
import com.kakarote.build.labour.entity.BO.LabourSubconPeopleCerBO;
import com.kakarote.build.labour.entity.PO.LabourSpeworktype;
import com.kakarote.build.labour.entity.PO.LabourSubconPeopleCer;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * 证件管理---分包管理人员证件台账 服务类
 * </p>
 *
 * @author zy
 * @since 2021-03-26
 */
public interface ILabourSubconPeopleCerService extends BaseService<LabourSubconPeopleCer> {

    public BasePage<LabourSubconPeopleCer> selectList(LabourSubconPeopleCerBO labourSubconPeopleCerBO);

    public BasePage<Map> selectTabsByInfo(LabourSpeworktypeBO labourSpeworktypeBO);


    BasePage<Map> selectTabsByInfoNew(LabourSpeworktypeBO labourSpeworktypeBO);

    Result addUser(Integer pid, Integer roleId);
}

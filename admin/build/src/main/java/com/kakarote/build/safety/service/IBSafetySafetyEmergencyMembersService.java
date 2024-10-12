package com.kakarote.build.safety.service;

import com.kakarote.build.safety.entity.BO.BSafetySafetyDataBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyData;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEmergencyMembers;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 应急班组成员管理 服务类
 * </p>
 *
 * @author author
 * @since 2021-03-22
 */
public interface IBSafetySafetyEmergencyMembersService extends BaseService<BSafetySafetyEmergencyMembers> {


    /**
     * 查询数据列表
     *
     */
    public List<BSafetySafetyEmergencyMembers> getByPId(String pid);

}

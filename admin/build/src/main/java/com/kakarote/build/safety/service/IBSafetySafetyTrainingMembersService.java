package com.kakarote.build.safety.service;

import com.kakarote.build.safety.entity.PO.BSafetySafetyEmergencyMembers;
import com.kakarote.build.safety.entity.PO.BSafetySafetyTrainingMembers;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 安全教育受教人员列表 服务类
 * </p>
 *
 * @author author
 * @since 2021-04-08
 */
public interface IBSafetySafetyTrainingMembersService extends BaseService<BSafetySafetyTrainingMembers> {

    /**
     * 查询数据列表
     *
     */
    public List<BSafetySafetyTrainingMembers> getByPId(String pid);

    /**
     * 主数据删除联动子数据删除
     */
    public String deleteByPId(String pid);

}

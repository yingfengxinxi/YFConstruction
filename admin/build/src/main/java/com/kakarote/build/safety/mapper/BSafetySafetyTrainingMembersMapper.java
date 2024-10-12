package com.kakarote.build.safety.mapper;

import com.kakarote.build.safety.entity.PO.BSafetySafetyEmergencyMembers;
import com.kakarote.build.safety.entity.PO.BSafetySafetyTrainingMembers;
import com.kakarote.core.servlet.BaseMapper;

import java.util.List;

/**
 * <p>
 * 安全教育受教人员列表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-04-08
 */
public interface BSafetySafetyTrainingMembersMapper extends BaseMapper<BSafetySafetyTrainingMembers> {

    List<BSafetySafetyTrainingMembers> selectDataList(String pid);
    Integer deleteByPId(String pid);
}

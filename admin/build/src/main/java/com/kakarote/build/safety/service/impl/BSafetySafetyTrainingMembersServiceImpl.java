package com.kakarote.build.safety.service.impl;

import com.kakarote.build.safety.entity.PO.BSafetySafetyEmergencyMembers;
import com.kakarote.build.safety.entity.PO.BSafetySafetyTrainingMembers;
import com.kakarote.build.safety.mapper.BSafetySafetyTrainingMembersMapper;
import com.kakarote.build.safety.service.IBSafetySafetyTrainingMembersService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 安全教育受教人员列表 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-04-08
 */
@Service
public class BSafetySafetyTrainingMembersServiceImpl extends BaseServiceImpl<BSafetySafetyTrainingMembersMapper, BSafetySafetyTrainingMembers> implements IBSafetySafetyTrainingMembersService {

    /**
     * 查询数据列表
     *
     */
    @Override
    public List<BSafetySafetyTrainingMembers> getByPId(String pid)
    {
        List<BSafetySafetyTrainingMembers>  list = getBaseMapper().selectDataList(pid);
        return list;
    }

    /**
     * 主数据删除联动子数据删除
     */
    @Override
    public String deleteByPId(String pid)
    {
       int k= getBaseMapper().deleteByPId(pid);
        return "1";
    }
}

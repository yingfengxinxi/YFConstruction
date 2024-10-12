package com.kakarote.build.safety.service.impl;

import com.kakarote.build.safety.entity.BO.BSafetySafetyEducationBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEducation;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEmergencyMembers;
import com.kakarote.build.safety.mapper.BSafetySafetyEmergencyMembersMapper;
import com.kakarote.build.safety.service.IBSafetySafetyEmergencyMembersService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 应急班组成员管理 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-03-22
 */
@Service
public class BSafetySafetyEmergencyMembersServiceImpl extends BaseServiceImpl<BSafetySafetyEmergencyMembersMapper, BSafetySafetyEmergencyMembers> implements IBSafetySafetyEmergencyMembersService {


    /**
     * 查询数据列表
     *
     */
    @Override
    public List<BSafetySafetyEmergencyMembers> getByPId(String pid)
    {
        List<BSafetySafetyEmergencyMembers>  list = getBaseMapper().selectDataList(pid);
        return list;
    }

}

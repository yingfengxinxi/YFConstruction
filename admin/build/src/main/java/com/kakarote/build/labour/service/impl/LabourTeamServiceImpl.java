package com.kakarote.build.labour.service.impl;


import com.kakarote.build.labour.entity.BO.LabourTeamBO;
import com.kakarote.build.labour.entity.PO.LabourTeam;
import com.kakarote.build.labour.mapper.LabourTeamMapper;
import com.kakarote.build.labour.service.ILabourTeamService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 劳务管理--班组 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-03-19
 */
@Service
public class LabourTeamServiceImpl extends BaseServiceImpl<LabourTeamMapper, LabourTeam> implements ILabourTeamService {

    @Override
    public BasePage<Map<String,Object>> selectList(LabourTeamBO teamBO) {
        BasePage<Map<String,Object>> list = getBaseMapper().selectList(teamBO.parse(), teamBO);
        return list;
    }

    @Override
    public List<LabourTeam> selcetIdsAndName() {
        List<LabourTeam> labourTeams = getBaseMapper().selcetIdsAndName();
        return labourTeams;
    }

    @Override
    public List<LabourTeam> selectListByName(HashMap<String, Object> params) {
        List<LabourTeam> list = getBaseMapper().selectListByName(params);
        return list;
    }

}

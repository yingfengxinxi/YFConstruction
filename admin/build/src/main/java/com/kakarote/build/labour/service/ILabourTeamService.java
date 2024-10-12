package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.BO.LabourTeamBO;
import com.kakarote.build.labour.entity.PO.LabourTeam;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 劳务管理--班组 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-03-19
 */
public interface ILabourTeamService extends BaseService<LabourTeam> {

    BasePage<Map<String,Object>> selectList(LabourTeamBO teamBO);

    public List<LabourTeam> selcetIdsAndName();

    /**
     * 查询班组列表 查询条件：班组名称、班组id
     * @param params
     * @return
     */
    List<LabourTeam> selectListByName(HashMap<String, Object> params);

}

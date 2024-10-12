package com.kakarote.build.labour.apipm.service;

import com.kakarote.build.labour.entity.BO.LabourTeamBO;
import com.kakarote.build.labour.entity.PO.LabourTeam;
import com.kakarote.core.common.Result;
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
public interface ITeamDataService extends BaseService<LabourTeam> {

    public Result teamDataAdd(Map<String, Object> parpam);

    public Result teamDataEdit(Map<String, Object> parpam);

    public Result teamDataDelete(Map<String, Object> parpam);

}

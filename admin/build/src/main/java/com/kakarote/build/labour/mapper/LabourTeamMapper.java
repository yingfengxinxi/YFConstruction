package com.kakarote.build.labour.mapper;

import com.kakarote.build.labour.entity.BO.LabourTeamBO;
import com.kakarote.build.labour.entity.PO.LabourSubcontractor;
import com.kakarote.build.labour.entity.PO.LabourTeam;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 劳务管理--班组 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-03-19
 */
public interface LabourTeamMapper extends BaseMapper<LabourTeam> {

    /**
     * 分页查询-获取班组数据列表
     */
    BasePage<Map<String,Object>> selectList(BasePage<LabourTeam> page, @Param("data") LabourTeamBO teamBO);

    List<LabourTeam> selcetIdsAndName();

    List<LabourTeam> selectListByName(@Param("data") HashMap<String, Object> params);


    LabourTeam selectByIdData(@Param("id")Integer id,@Param("tenantId")Integer tenantId,@Param("projectId")Integer projectId);
    int deleteDetailsGrp(@Param("id")Integer id,@Param("tenantId")Integer tenantId,@Param("projectId")Integer projectId);
    int saveFile(@Param("data")Map<String, Object> fileData);
    int deleteFileData(@Param("batchId")String batchId,@Param("tenantId")Integer tenantId);
}

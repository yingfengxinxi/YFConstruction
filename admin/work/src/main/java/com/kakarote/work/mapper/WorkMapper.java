package com.kakarote.work.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.work.entity.BO.WorkOwnerRoleBO;
import com.kakarote.work.entity.BO.WorkTaskTemplateBO;
import com.kakarote.work.entity.PO.Work;
import com.kakarote.work.entity.PO.WorkTask;
import com.kakarote.work.entity.VO.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目表 Mapper 接口
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface WorkMapper extends BaseMapper<Work> {
    void deleteTaskRelationByWorkId(@Param("workId") Integer workId);

    void leaveTaskOwnerUser(@Param("workId") Integer workId, @Param("userId") Long userId);

    List<WorkInfoVo> queryWorkNameList(@Param("userId") Long userId);

    List<WorkTaskTemplateClassVO> queryWorkTaskTemplateClass(@Param("taskId") Integer taskId);

    List<TaskInfoVO> queryTaskByClassId(@Param("data") WorkTaskTemplateBO workTaskTemplateBO, @Param("classId") Integer classId, @Param("userId") Long userId);

    WorkTaskStatsVO workStatistics(@Param("workId") Integer workId, @Param("workIdList") List<Integer> workIdList, @Param("userId") Long userId, @Param("mainUserId") Long mainUserId
            , @Param("tenantId") Long tenantId);

    List<WorkUserStatsVO> queryAllTaskManUser();

    List<Integer> queryWorkIdListByOwnerUser(@Param("userId") Long userId);

    WorkClassStatsVO queryWorkClassStats(@Param("workId") Integer workId, @Param("classId") Integer classId);

    List<WorkTask> queryWorkLabelByWorkId(@Param("workId") Integer workId);

    List<TaskInfoVO> archList(@Param("workId") Integer workId);

    public List<String> queryWorkAuthByUserId(@Param("roleId") Integer roleId, @Param("parentId") Integer parentId);

    public List<String> queryMenuByRoleId(@Param("roleId") Integer roleId);

    public List<WorkOwnerRoleBO> queryOwnerRoleList(@Param("workId") Integer workId);

    public BasePage<Map<String, Object>> queryProject(BasePage<Map<String, Object>> basePage, @Param("customerId") Integer customerId,@Param("userId") Integer userId);
}

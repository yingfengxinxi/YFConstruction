package com.kakarote.build.labour.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.labour.entity.BO.LabourInfocollectionBO;
import com.kakarote.build.labour.entity.BO.LabourWorktypeBO;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.labour.entity.PO.LabourWorktype;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 劳务管理--信息采集 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-03-17
 */
public interface LabourInfocollectionMapper extends BaseMapper<LabourInfocollection> {

    BasePage<LabourInfocollection> selectList(BasePage<LabourInfocollection> page, @Param("data") LabourInfocollectionBO labourInfocollectionBO);

    BasePage<Map<String, Object>> getTeamLeaderList(BasePage<LabourInfocollection> page, @Param("data") LabourInfocollectionBO labourInfocollectionBO);

    List<Map> selectInfoByMapNoPage(@Param("data")LabourInfocollectionBO labourInfocollectionBO);

    //2021-04-11新增给安全教育使用花名册查询时移除已存在的人员数据使用列表
    BasePage<LabourInfocollection> selectListSafetyTraining(BasePage<LabourInfocollection> page, @Param("data") LabourInfocollectionBO labourInfocollectionBO);

    //2021-04-15新增统计班组人数
    List<LabourInfocollection>   ByTeamName(String teamName);

    //2021-04-16新增查询人员id
    public LabourInfocollection selectInfoRecord(LabourInfocollection labourInfocollection);

    //2021-11-25新增查询当前租户下的全部人员
    public List<LabourInfocollection> selectInfoRecordAll(LabourInfocollection labourInfocollection);

    Map<String, Object> selectAttendanceToday(@Param("data") Map<String, Object> params);

    Map<String, Object> selectAttendanceTodayByProjectId(@Param("projectId")Integer projectId);

    List<Map<String, Object>> selectSceneWorkTypeList();

    List<Map<String, Object>> selectTeamList();

    List<Map<String, Object>> selectNationList();

    List<Map<String, Object>> selectGenderList();

    //查询零工的id和name 零工签证使用
    List<Map<String, Object>> selectOddIdAndName();


    List<Map<String, Object>> queryProjectsByIdCard(@Param("idCard")String idCard,@Param("projectId")Integer projectId);

    LabourInfocollection getByIdCard(@Param("idCard")String idCard,@Param("projectId")Integer projectId,@Param("tenantId")Integer tenantId);

    List<Map<String, Object>> selectAgeRange();

    Map<String, Object> selectHeadCount();

    Map<String, Object> selectEntryOrExitCount();

    List<Map<String, Object>> selectListByTeam(@Param("teamId") Integer teamId);

    //2021-12-20新增统计特殊职位人数
    List<LabourInfocollection>  byManageType(LabourInfocollection labourInfocollection );

    //2021-12-20新增统计特殊职位人数
    List<LabourInfocollection>  byManageType2(LabourInfocollection labourInfocollection );

    int updateWorkerContract(LabourInfocollection labourInfocollection );


    BasePage<Map<String,Object>> selectAttendanceDataList(BasePage<Object> parse,@Param("data")  Map<String,Object> param);

    BasePage<Map<String,Object>> byManageType3(BasePage<Object> parse,@Param("data")  LabourInfocollection labour);

    Map<String, Object> selectInfoByName(@Param("data") Map<String, Object> params);

    Map<String, Object> getInfocollectionByPhone(@Param("phone") Integer phone);

    @SqlParser(filter = true)
    LabourInfocollection getInfocollectionByNameAndPhone(@Param("name") String name,@Param("phone") String phone);
}

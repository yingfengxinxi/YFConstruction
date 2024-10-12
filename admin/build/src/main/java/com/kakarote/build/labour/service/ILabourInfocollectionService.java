package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.BO.LabourInfocollectionBO;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.labour.entity.PO.LabourSpeworktype;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 劳务管理--信息采集 服务类
 * </p>
 *
 * @author zy
 * @since 2021-03-17
 */
public interface ILabourInfocollectionService extends BaseService<LabourInfocollection> {

    public BasePage<LabourInfocollection> selectList(LabourInfocollectionBO labourInfocollectionBO);

    public BasePage<Map<String, Object>> getTeamLeaderList(LabourInfocollectionBO labourInfocollectionBO);

    Result saveInfo(MultipartFile file, LabourInfocollection labourInfocollection, LabourSpeworktype labourSpeworktype);

    Result saveOddJobInfo(MultipartFile file, LabourInfocollection labourInfocollection);

    List<Map> selectInfoByMapNoPage(LabourInfocollectionBO labourInfocollectionBO);

    //2021-04-11新增给安全教育使用花名册查询时移除已存在的人员数据使用列表
    public BasePage<LabourInfocollection> selectListSafetyTraining(LabourInfocollectionBO labourInfocollectionBO);

    //2021-04-15新增统计班组人数
    public List<LabourInfocollection>  ByTeamName(String teamName );

    //2021-04-16新增查询人员id
    public LabourInfocollection selectInfoRecord(LabourInfocollection labourInfocollection);

    //2021-11-25新增查询当前租户下的全部人员
    public List<LabourInfocollection> selectInfoRecordAll(LabourInfocollection labourInfocollection);

    Map<String, Object> selectAttendanceData(Map<String, Object> params);

    //集团数据今日施工在场人数
    List<Map> selectAttendanceDataByProjectIds(List<Map> list);

    List<Map<String, Object>> selectSceneWorkTypeList();

    List<Map<String, Object>> selectTeamList();

    Map<String, Object> selectNationAndGender();

    //查询零工的id和name 零工签证使用
    List<Map<String, Object>> selectOddIdAndName();

    //根据身份证查询项目履历信息
    List<Map<String, Object>> queryProjectsByIdCard(String idCard,Integer projectId);

    // 查询年龄分布数据
    List<Map<String, Object>> selectAgeRange();

    Map<String, Object> selectRealTimeNumber();

    List<Map<String, Object>> selectListByTeam(Integer teamId);


    //2021-12-20新增统计特殊职位人数
    public List<LabourInfocollection>  byManageType(LabourInfocollection labourInfocollection );
    //2021-12-20新增统计特殊职位人数
    public List<LabourInfocollection>  byManageType2(LabourInfocollection labourInfocollection );
    //2021-12-20新增统计特殊职位人数
    //2022-04-19添加分页
    BasePage<Map<String,Object>> byManageType3(BasePage<Object> parse,LabourInfocollection labour);
    BasePage<Map<String,Object>>  selectAttendanceDataList(Map<String,Object> param);


    Map<String, Object> selectInfoByName(Map<String, Object> params);

    Map<String, Object> getInfocollectionByPhone(Integer phone);
}

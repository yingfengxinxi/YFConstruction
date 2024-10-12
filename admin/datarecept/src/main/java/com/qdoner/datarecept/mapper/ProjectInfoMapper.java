package com.qdoner.datarecept.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.qdoner.datarecept.entity.BO.ProjectInfoBO;
import com.qdoner.datarecept.entity.PO.ProjectInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目信息表 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2021-03-16
 */
public interface ProjectInfoMapper extends BaseMapper<ProjectInfo> {

    @Select("select * from b_project_info where id = #{id}")
    ProjectInfo getByIdGrp(@Param("id") Integer id);

    BasePage<Map> queryPageList(BasePage<ProjectInfo> page, @Param("data") ProjectInfoBO projectInfoBO);

    BasePage<Map> queryPageListGrp(BasePage<ProjectInfo> page, @Param("data") ProjectInfoBO projectInfoBO);


    List<Map> selectTypeAndNameMaps();

    @SqlParser(filter=true)
    Map selectProgress();

    List<Map> slelctAll();
    List<Map> slelctAllAttendance(@Param("tenantId") String tenantId);
    Map<String,Object> ProjectNum(Map<String, Object> map);

    String selectDictValue(@Param("data") Map<String, Object> params);

    List<Map<String,Object>> projectInfoList();

    List<Map<String,Object>> queryProjectTreeList();

    List<Map<String, Object>> selectProjectStateCount();

    List<Map<String, Object>> projectInfoListNJ();

    List<Map<String,Object>>  projectInfoListByProjectState(@Param("data") Map<String, Object> params);

    Map<String, Object> selectProAccessQuantity();

    List<ProjectInfo> projectInfoListGetJk(@Param("data") Map<String, Object> parpam);

    BasePage<Map> queryPageListByEval(BasePage<Object> parse, @Param("data") ProjectInfoBO projectInfo);

    List<Map<String, Object>> queryProjectListByName(@Param("data") Map<String, Object> params);

    Map<String, Object> selectProjectInfo(@Param("id") Long id);

    Map getLocationInfoByIdGrp(Integer id);

    List<Map<String, Object>> projectInfoListByAlertType(@Param("data") Map<String, Object> params);


    List<Map<String, Object>> queryProjectList();
}

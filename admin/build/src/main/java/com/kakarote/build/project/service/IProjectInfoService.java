package com.kakarote.build.project.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.project.entity.BO.ProjectInfoBO;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目信息表 服务类
 * </p>
 *
 * @author wudw
 * @since 2021-03-16
 */
public interface IProjectInfoService extends BaseService<ProjectInfo> {

    ProjectInfo getByIdGrp(Integer id);

    Map getLocationInfoById(Integer id);

    BasePage<Map> queryPageList(ProjectInfoBO projectInfo);

    BasePage<Map> queryPageListAll(ProjectInfoBO projectInfo);

    public List<Map> queryMapListAll();

    public Map selectProgress();

    List<Map> slelctAll();

    Map<String,Object> ProjectNum(Map<String,Object> map);


    /**
     * excel项目导入
     * @param file file
     */
    public JSONObject excelImport(MultipartFile file);

    List<Map<String,Object>> projectInfoList();

    List<Map<String,Object>> projectInfoListByProjectState(Map<String, Object> params);

    List<Map<String,Object>> queryProjectTreeList();

    List<Map<String, Object>> selectProjectStateCount();
    List<Map> slelctAllAttendance( String tenantId);

    List<Map<String, Object>> projectInfoListNJ();

    /**
     * 查询监管平台接入总计接入项目数量、完工数量、正在监测项目数量
     * @return
     */
    Map<String, Object> selectProAccessQuantity();

    List<ProjectInfo> projectInfoListGetJk(Map<String, Object> parpam);

    BasePage<Map> queryPageListByEval(ProjectInfoBO projectInfo);

    List<Map<String, Object>> queryProjectListByName(Map<String, Object> params);

    Result syncAccount();

    Map selectProjectInfo();

    List<Map<String, Object>> projectInfoListByAlertType(Map<String, Object> params);


    List<Map<String, Object>> queryProjectList();

    List<Map<String, Object>> querySalesOfficeProjectList();
}

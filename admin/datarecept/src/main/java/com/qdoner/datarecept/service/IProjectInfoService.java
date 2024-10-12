package com.qdoner.datarecept.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import com.qdoner.datarecept.entity.BO.ProjectInfoBO;
import com.qdoner.datarecept.entity.PO.ProjectInfo;
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

    List<ProjectInfo> projectInfoListGetJk(Map<String, Object> parpam);
}

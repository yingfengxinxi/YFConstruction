package com.qdoner.datarecept.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.core.utils.UserUtil;
import com.qdoner.datarecept.entity.BO.ProjectInfoBO;
import com.qdoner.datarecept.entity.PO.ProjectInfo;
import com.qdoner.datarecept.mapper.ProjectInfoMapper;
import com.qdoner.datarecept.service.IProjectInfoService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 项目信息表 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-03-16
 */
@Service
public class ProjectInfoServiceImpl extends BaseServiceImpl<ProjectInfoMapper, ProjectInfo> implements IProjectInfoService {

    @Override
    public List<ProjectInfo> projectInfoListGetJk(Map<String, Object> parpam) {
        return getBaseMapper().projectInfoListGetJk(parpam);
    }
}

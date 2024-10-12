package com.kakarote.build.project.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceRecord;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.labour.service.ILabourInfocollectionService;
import com.kakarote.build.project.entity.BO.ProjectInfoBO;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.project.entity.PO.ProjectUser;
import com.kakarote.build.project.mapper.ProjectInfoMapper;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.build.project.service.IProjectUserService;
import com.kakarote.build.project.service.ISingleBuildingInfoService;
import com.kakarote.build.utils.DateUtil;
import com.kakarote.build.utils.HttpClientResult;
import com.kakarote.build.utils.HttpClientUtils;
import com.kakarote.build.wxapp.util.StringUtil;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.core.utils.UserUtil;
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
    @Autowired
    private ILabourInfocollectionService labourInfocollectionService;

    @Autowired
    private AdminService adminService;

    @Autowired
    IProjectUserService projectUserService;

    @Autowired
    private ISingleBuildingInfoService singleBuildingInfoService;

    @Override
    public ProjectInfo getByIdGrp(Integer id) {
        ProjectInfo projectInfo = getBaseMapper().getByIdGrp(id);
        // 查询项目经理列表
        List<SimpleUser> projectManagerList = adminService.queryUserByIds(TagUtil.toLongSet(projectInfo.getProjectManager())).getData();
        projectInfo.setProjectManagerList(projectManagerList);
        return projectInfo;
    }

    @Override
    public Map getLocationInfoById(Integer id) {
        return getBaseMapper().getLocationInfoByIdGrp(id);
    }

    @Override
    public BasePage<Map> queryPageList(ProjectInfoBO projectInfo) {
        BasePage<Map> list = getBaseMapper().queryPageList(projectInfo.parse(), projectInfo);
        return list;
    }

    @Override
    public BasePage<Map> queryPageListAll(ProjectInfoBO projectInfo) {
        projectInfo.setOrdersBd("sort",true);
        projectInfo.setOrdersBd("create_time",false);
        BasePage<Map> list = getBaseMapper().queryPageListGrp(projectInfo.parse(), projectInfo);
        return list;
    }

    /**
     * 集团数据 项目类型echarts
     *
     * @return
     */
    @Override
    public List<Map> queryMapListAll() {

        List<Map> list1 = getBaseMapper().selectTypeAndNameMaps();

        return list1;
    }

    /**
     * 查询项目进度
     *
     * @return
     */
    @Override
    public Map selectProgress() {
        Map map = getBaseMapper().selectProgress();
        Map maps = new HashMap();
        //项目总数
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.select("count(1) projectCount").ne("project_id", 0);
        Map<String, Object> map1 = getMap(queryWrapper1);
        //工程总造价
        QueryWrapper queryWrapper2 = new QueryWrapper();
        queryWrapper2.select("sum(total_cost) projectAllCost").ne("project_id", 0);
        Map<String, Object> map2 = getMap(queryWrapper2);
        //劳务总人数  count花名册
        QueryWrapper queryWrapper3 = new QueryWrapper();
        queryWrapper3.select("count(1) labourNum").ne("project_id", 0);
        Map<String, Object> map4 = labourInfocollectionService.getMap(queryWrapper3);
        //在建项目  项目状态在建
        QueryWrapper queryWrapper4 = new QueryWrapper();
        queryWrapper4.select("count(1) projectSta").eq("project_state", '2');
        queryWrapper4.ne("project_id", 0);
        Map<String, Object> map3 = getMap(queryWrapper4);

        maps.put("projectCount", map1.get("projectCount"));
        maps.put("projectAllCost", map2.get("projectAllCost"));
        maps.put("labourNum", map4.get("labourNum"));
        maps.put("projectSta", map3.get("projectSta"));

        map.put("rightFour", maps);
        return map;
    }

    @Override
    public List<Map> slelctAll() {
        return this.getBaseMapper().slelctAll();
    }

    @Override
    public List<Map> slelctAllAttendance(String tenantId) {
        return this.getBaseMapper().slelctAllAttendance(tenantId);
    }

    @Override
    public List<Map<String, Object>> projectInfoListNJ() {
        return getBaseMapper().projectInfoListNJ();
    }

    @Override
    public Map<String, Object> selectProAccessQuantity() {
        Map<String, Object> result = getBaseMapper().selectProAccessQuantity();
        return result;
    }

    @Override
    public Map<String, Object> ProjectNum(Map<String, Object> map) {
        return getBaseMapper().ProjectNum(map);
    }


    /**
     * excel导入员工
     *
     * @param file file
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject excelImport(MultipartFile file) {
        List<List<Object>> errList = new ArrayList<>();
        String filePath = getFilePath(file);
        AtomicReference<Integer> num = new AtomicReference<>(0);
        ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {

            if (rowIndex > 1) {
                ProjectInfo projectInfo = new ProjectInfo();
                Map<String, Object> selectDict = new HashMap<String, Object>();  //字典项查询用
                //施工单位名称
                Object companyName = rowList.get(0);
                if (companyName == null || companyName.equals("")) {
                    throw new CrmException(1001, "施工单位名称不能为空");
                }
                //项目名称
                Object projectName = rowList.get(1);
                if (projectName == null || projectName.equals("")) {
                    throw new CrmException(1001, "项目名称不能为空");
                }

                //施工许可证号
                Object projectConstructionPermitCode = rowList.get(2);
                if (projectConstructionPermitCode == null || projectConstructionPermitCode.equals("")) {
                    throw new CrmException(1001, projectName + "的施工许可证号不能为空");
                }

                //工程造价
                Object projectTotalCost = rowList.get(3);
                if (projectTotalCost == null || projectTotalCost.equals("")) {
                    throw new CrmException(1001, projectName + "的工程造价不能为空");
                }


                //工程面积
                Object projectTotalArea = rowList.get(4);
                if (projectTotalArea == null || projectTotalArea.equals("")) {
                    throw new CrmException(1001, projectName + "的工程面积不能为空");
                }
                projectInfo.setCreateTime(new Date()); //创建时间
                projectInfo.setCreateUserId(UserUtil.getUser().getUserId());//创建人
                projectInfo.setCompanyName(companyName.toString().trim());
                projectInfo.setProjectName(projectName.toString().trim());
                projectInfo.setConstructionPermitCode(projectConstructionPermitCode.toString().trim());

                /* selectDict.put("dictType","b_project_classify");
                selectDict.put("dictLabel",projectState);
                String State =getBaseMapper().selectDictValue(selectDict);//查询项目类型匹配对应的字典项并存入
                projectInfo.setProjectState(State);*/
                String totalCost = projectTotalCost.toString().trim(); //将工程造价转换为 String类型
                if (!isNumeric(totalCost)) {
                    throw new CrmException(1001, projectName + "的工程造价数据格式不对，请勿填写非数字内容");
                }
                BigDecimal bd = new BigDecimal(totalCost); //将工程造价转换为 BigDecimal类型
                projectInfo.setTotalCost(bd);//存入工程造价

                String totalArea = projectTotalArea.toString().trim();//将工程面积转换为 String类型
                if (!isNumeric(totalArea)) {
                    throw new CrmException(1001, projectName + "的工程面积数据格式不对，请勿填写非数字内容");
                }
                double area = Double.parseDouble(totalArea);//将工程面积转换为 Double类型
                projectInfo.setTotalArea(area);//存入工程面积

                Object commencementOrderDate = rowList.get(5); //实际开工日期
                if (commencementOrderDate == null || commencementOrderDate.equals("")) {
                    throw new CrmException(1001, projectName + "的实际开工日期为空");
                }
                String time = "";
                if (commencementOrderDate instanceof String) {
                    time = commencementOrderDate.toString().trim();
                    boolean legalDate = isLegalDate(time);
                    if (!legalDate) {
                        throw new CrmException(1001, projectName + "的实际开工日期不正确，应为：yyyy-MM-dd");
                    }
                } else if (commencementOrderDate instanceof Date) {
                    Date date = (Date) commencementOrderDate;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ");
                    time = simpleDateFormat.format(date);
                } else {
                    throw new CrmException(1001, projectName + "的实际开工日期格式不正确，应为：yyyy-MM-dd ");
                }
                projectInfo.setActualCommencementDate(StrToDate(time));


                Object plannedCompletionDate = rowList.get(6);
                if (plannedCompletionDate == null || plannedCompletionDate.equals("")) {
                    throw new CrmException(1001, projectName + "的计划竣工时间不能为空");
                }
                time = "";
                if (plannedCompletionDate instanceof String) {
                    time = plannedCompletionDate.toString().trim();
                    boolean legalDate = isLegalDate(time);
                    if (!legalDate) {
                        throw new CrmException(1001, projectName + "的计划竣工时间格式不正确，应为：yyyy-MM-dd ");
                    }
                } else if (plannedCompletionDate instanceof Date) {
                    Date date = (Date) plannedCompletionDate;

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ");
                    time = simpleDateFormat.format(date);
                } else {
                    throw new CrmException(1001, projectName + "的计划竣工时间格式不正确，应为：yyyy-MM-dd ");
                }
                projectInfo.setPlannedCompletionDate(StrToDate(time));

                // 判断项目地址是否为null
                String projectAddress = rowList.get(7).toString().trim();
                if (projectAddress != null && !projectAddress.equals("")) {
                    projectInfo.setProjectAddress(projectAddress);
                }
                // 判断经度是否为null
                String lng = rowList.get(8).toString().trim();
                if (lng != null && !lng.equals("")) {
                    projectInfo.setLng(lng);
                }
                // 判断纬度是否为null
                String lat = rowList.get(9).toString().trim();
                if (lat != null && !lat.equals("")) {
                    projectInfo.setLat(lat);
                }

                projectInfo.setProvince("2182"); // 默认放入 山东省
                projectInfo.setCity("2202");   // 默认放入 德州市
                projectInfo.setDistrict("2205"); // 默认放入 宁津县
                projectInfo.setProjectState("2"); // 默认放入 项目状态 在建

                save(projectInfo);
                projectInfo.setProjectId(projectInfo.getId());
                updateById(projectInfo);
            }
        });
        FileUtil.del(filePath);
        JSONObject result = new JSONObject().fluentPut("totalSize", num.get()).fluentPut("errSize", 0);
        if (errList.size() > 0) {
            BigExcelWriter writer = null;
            try {
                String token = IdUtil.simpleUUID();
                writer = ExcelUtil.getBigWriter(FileUtil.getTmpDirPath() + "/" + token);
                writer.merge(6, "项目管理导入模板(*)为必填项");
                for (int i = 0; i < 7; i++) {
                    writer.setColumnWidth(i, 20);
                }
                writer.setDefaultRowHeight(20);
                Cell cell = writer.getCell(0, 0);
                CellStyle cellStyle = cell.getCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                Font font = writer.createFont();
                font.setBold(true);
                font.setFontHeightInPoints((short) 16);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                writer.writeHeadRow(Arrays.asList("错误信息", "施工单位名称(*)", "项目名称(*)", "施工许可证编号(*)", "工程造价(万元)(*)", "工程面积(平)(*)", "实际开工日期(*)", "计划竣工时间(*)"));
                writer.write(errList);
                result.fluentPut("errSize", errList.size()).fluentPut("token", token);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> projectInfoList() {
        return getBaseMapper().projectInfoList();
    }

    @Override
    public List<Map<String, Object>> projectInfoListByProjectState(Map<String, Object> params) {
        return getBaseMapper().projectInfoListByProjectState(params);
    }

    @Override
    public List<Map<String, Object>> queryProjectTreeList() {
        return getBaseMapper().queryProjectTreeList();
    }

    @Override
    public List<Map<String, Object>> selectProjectStateCount() {
        return getBaseMapper().selectProjectStateCount();
    }


    private String getFilePath(MultipartFile file) {
        String dirPath = FileUtil.getTmpDirPath();
        try {
            InputStream inputStream = file.getInputStream();
            File fromStream = FileUtil.writeFromStream(inputStream, dirPath + "/" + IdUtil.simpleUUID() + file.getOriginalFilename());
            return fromStream.getAbsolutePath();
        } catch (IOException e) {
            throw new CrmException(SystemCodeEnum.SYSTEM_UPLOAD_FILE_ERROR);
        }
    }

    /**
     * 日期转换String 转 Date
     */
    public Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    //判断 时间格式
    private static boolean isLegalDate(String sDate) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(sDate);
            return sDate.equals(formatter.format(date));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isNumeric(String str) {
        // 该正则表达式可以匹配所有的数字 包括负数
        Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return false;//异常 说明包含非数字。
        }

        Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    @Override
    public List<ProjectInfo> projectInfoListGetJk(Map<String, Object> parpam) {
        return getBaseMapper().projectInfoListGetJk(parpam);
    }

    @Override
    public BasePage<Map> queryPageListByEval(ProjectInfoBO projectInfo) {
        projectInfo.setOrdersBd("a.sort",true);
        projectInfo.setOrdersBd("a.create_time",false);

        BasePage<Map> list = getBaseMapper().queryPageListByEval(projectInfo.parse(), projectInfo);
        return list;
    }

    @Override
    public List<Map<String, Object>> queryProjectListByName(Map<String, Object> params) {
        List<Map<String, Object>> list = getBaseMapper().queryProjectListByName(params);
        return list;
    }

    /**
     * @return
     */
    @Override
    public Result syncAccount() {
        //
        List<ProjectInfo> projectInfos = this.list();
        projectInfos.forEach(projectInfo -> {
            createAccount(projectInfo);
        });
        return Result.ok();
    }

    @Override
    public Map selectProjectInfo() {

        // 获取当前项目id
        Long projectId = UserUtil.getUser().getProjectId();

        // 查询项目信息
        Map<String, Object> map = getBaseMapper().selectProjectInfo(projectId);

        // 查询项目主体建筑数量
        Map<String, Object> buildingMap = singleBuildingInfoService.getBuildingNumberByPro();
        map.put("buildingNumber", buildingMap.get("buildingNumber"));
        return map;
    }

    @Override
    public List<Map<String, Object>> projectInfoListByAlertType(Map<String, Object> params) {
        return getBaseMapper().projectInfoListByAlertType(params);
    }

    @Override
    public List<Map<String, Object>> queryProjectList() {
        return getBaseMapper().queryProjectList();
    }

    @Override
    public List<Map<String, Object>> querySalesOfficeProjectList() {
        return getBaseMapper().querySalesOfficeProjectList();
    }


    /**
     * 创建账号
     * 部门名称 =》 项目简称
     * 账户登录账号 =》 项目简称首字母+项目ID
     */
    private void createAccount(ProjectInfo projectInfo) {
        String pName = projectInfo.getProjectAbbreviation();
        if (pName == null || pName.length() < 4) {//简称不得少于四个字
            pName = projectInfo.getProjectName();
        }
        //创建部门
        Map<String, Object> dept = new HashMap<>();
        dept.put("name", pName);
        dept.put("pid", projectInfo.getTenantId());
        dept.put("num", 10);
        dept.put("tenantId", projectInfo.getTenantId());
        Integer deptId = adminService.addDept(dept);
        if (deptId != -1) {//-1表示账号已存在  其他则为部门ID
            //创建账号
            Map<String, Object> user = new HashMap();
            user.put("username", StringUtil.getLowerCase(pName, false) + projectInfo.getProjectId());
            user.put("password", StringUtil.getLowerCase(pName, false) + projectInfo.getProjectId());
            user.put("realname", pName + "小程序账号");
            user.put("deptId", deptId);
            //todo 待优化 使用角色类型动态区分
            user.put("roleId", 180284);
            user.put("tenantId", projectInfo.getTenantId());
            user.put("projectId", projectInfo.getProjectId());//自动锁定项目
            Integer userId = adminService.addUser(user);
            //项目授权
            ProjectUser projectUser = new ProjectUser();
            projectUser.setId(UUID.randomUUID().toString());
            projectUser.setProjectId(projectInfo.getProjectId());
            projectUser.setTenantId(projectInfo.getTenantId());
            projectUser.setUserId(userId);
            projectUserService.save(projectUser);
        }
    }

}

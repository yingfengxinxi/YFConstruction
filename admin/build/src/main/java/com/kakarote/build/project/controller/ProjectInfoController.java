package com.kakarote.build.project.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kakarote.build.project.entity.BO.ProjectInfoBO;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author shz
 * @Description:项目管理Controller
 * @date 2021-3-16 9:58
 */
@RestController
@RequestMapping("/projectInfo")
public class ProjectInfoController {

    @Autowired
    private IProjectInfoService iProjectInfoService;

    /**
     * @param
     * @return
     * @Description:分页查询
     * @author shz
     * @date 2021-3-16 9:59
     */
    @PostMapping("/queryPageList")
    @ApiOperation("查询列表页数据")
    public Result<BasePage<Map>> queryPageList(@RequestBody ProjectInfoBO projectInfo) {
        BasePage<Map> mapBasePage = iProjectInfoService.queryPageListAll(projectInfo);
//        BasePage<Map> mapBasePage = iProjectInfoService.queryPageList(projectInfo);
        return R.ok(mapBasePage);
    }

    /**
     * @param
     * @return
     * @Description:新增项目
     * @author shz
     * @date 2021-3-16 11:37
     */
    @PostMapping("/add")
    @ApiOperation("新增项目")
    public Result<BasePage<Map<String, Object>>> add(@RequestBody ProjectInfo projectInfo) {
        projectInfo.setAppsecret(UUID.randomUUID().toString().replace("-", ""));
        iProjectInfoService.save(projectInfo);
        projectInfo.setProjectId(projectInfo.getId());
        iProjectInfoService.updateById(projectInfo);
        return R.ok();
    }

    @PostMapping("/getProjectInfoById/{id}")
    @ApiOperation("获取项目信息")
    public Result<ProjectInfo> getProjectInfoById(@PathVariable("id") Integer id) {
        return R.ok(iProjectInfoService.getByIdGrp(id));
    }

    @PostMapping("/updateProjectInfo")
    @ApiOperation("更新项目信息")
    public Result<ProjectInfo> updateProjectInfo(@RequestBody ProjectInfo projectInfo) {
        LambdaUpdateWrapper<ProjectInfo> updateWrapper = new LambdaUpdateWrapper();

        updateWrapper.ne(ProjectInfo::getProjectId, 0);
        updateWrapper.eq(ProjectInfo::getId, projectInfo.getId());
        iProjectInfoService.update(projectInfo, updateWrapper);
        return R.ok();
    }

    /**
     * @param
     * @return
     * @Description:分页查询
     * @author shz
     * @date 2021-3-16 9:59
     */
    @PostMapping("/queryPageListAll")
    @ApiOperation("查询列表页全部数据")
    @OperateLog(detail = "查询列表页全部数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map>> queryPageListAll(@RequestBody ProjectInfoBO projectInfo) {
        BasePage<Map> mapBasePage = iProjectInfoService.queryPageListAll(projectInfo);
        return R.ok(mapBasePage);
    }

    /**
     * @param
     * @return
     * @Description:集团数据展示 项目类型echarts
     * @author ZY
     * @date 2021-5-18
     */
    @PostMapping("/queryMapListAll")
    @ApiOperation("查询全部项目类型")
    @OperateLog(detail = "查询全部项目类型", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryMapListAll() {

        List<Map> list = iProjectInfoService.queryMapListAll();

        return R.ok(list);
    }

    /**
     * @param
     * @return
     * @Description:集团数据展示 项目明细
     * @author ZY
     * @date 2021-5-19
     */
    @PostMapping("/queryMapNoPageList")
    @ApiOperation("集团数据展示查询全部项目")
    @OperateLog(detail = "集团数据展示查询全部项目", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryMapNoPageList() {

        List<ProjectInfo> project_id = iProjectInfoService.
                query()
                .ne("project_id", 0)
                .orderByDesc("create_time")
                .list();

        return R.ok(project_id);
    }

    @PostMapping("/selectProgress")
    @ApiOperation("查询项目进度")
    @OperateLog(detail = "查询项目进度", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectProgress() {
        Map map = iProjectInfoService.selectProgress();
        return R.ok(map);
    }


    @PostMapping("/getThree/{id}")
    @ApiOperation("获取项目竣工时间总造价总面积信息")
    public Result<Map> getThree(@PathVariable("id") Integer id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.ne("project_id", 0);
        queryWrapper.eq("id", id);
        queryWrapper
                .select("date_format(planned_Completion_Date,'%Y-%m-%d') endTime", "total_area allAre", "total_cost allTost");

        Map map = iProjectInfoService.getMap(queryWrapper);

        return R.ok(map);
    }

    /**
     * @param
     * @return
     * @Description:查询全部项目
     * @author shz
     * @date 2021-7-20 10:30
     */
    @PostMapping("/WxslelctAll")
    @ApiOperation("查询全部项目")
    public Result WxslelctAll() {
        List<Map> list = iProjectInfoService.slelctAll();
        return R.ok(list);
    }

    /**
     * @param
     * @return
     * @Description:查询项目列表，下拉框使用
     * @author LZY
     * @date 2021-09-17
     */
    @PostMapping("/queryProjectList")
    @ApiOperation("查询项目列表，下拉框使用")
    @OperateLog(detail = "查询项目列表，下拉框使用", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryProjectList() {
        List<Map<String, Object>> projectList = iProjectInfoService.queryProjectList();
        return R.ok(projectList);
    }

    /**
     * @param
     * @return
     * @Description:查询项目列表，下拉框使用，允许查询售楼处数据。
     * @author LZY
     * @date 2022-11-10
     */
    @PostMapping("/querySalesOfficeProjectList")
    @ApiOperation("查询项目列表，下拉框使用")
    @OperateLog(detail = "查询项目列表，下拉框使用", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result querySalesOfficeProjectList() {
        List<Map<String, Object>> projectList = iProjectInfoService.querySalesOfficeProjectList();
        return R.ok(projectList);
    }

    @PostMapping("/queryProjectTreeList")
    @ApiOperation("查询项目与资产的树形菜单,视频监控查看使用")
    @OperateLog(detail = "查询项目与资产的树形菜单,视频监控查看使用", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryProjectTreeList() {
        List<Map<String, Object>> projectList = iProjectInfoService.queryProjectTreeList();
        return R.ok(projectList);
    }

    @PostMapping("/excelImport")
    @ApiOperation("excel导入考勤")
    public Result<JSONObject> excelImport(@RequestParam("file") MultipartFile file) {
        JSONObject object = iProjectInfoService.excelImport(file);
        return R.ok(object);
    }

    /**
     * @param
     * @return
     * @Description:查询项目列表，下拉框使用 只查询在建状态的数据
     * @author LZY
     * @date 2021-10-21
     */
    @PostMapping("/queryProjectListByState")
    @ApiOperation("查询项目列表，下拉框使用")
    @OperateLog(detail = "查询项目列表，下拉框使用", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryProjectListByState() {

        List<Map<String, Object>> projectList = iProjectInfoService.queryProjectList();
        return R.ok(projectList);
    }

    /**
    * @Description:获取当前项目平面图
    * @author shz
    * @date 2021-11-16 10:22
    * @param 
    * @return 
    */
    @PostMapping("/getPlaneInfo")
    @ApiOperation("查询项目列表，下拉框使用")
    @OperateLog(detail = "查询项目列表，下拉框使用", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getPlaneInfo() {
        UserInfo user =  UserUtil.getUser();
        ProjectInfo projectInfo = iProjectInfoService.getById(user.getProjectId());
        return R.ok(projectInfo);
    }

    /**
     * @param
     * @return
     * @Description:分页查询 评级申请使用
     * @author shz
     * @date 2021-3-16 9:59
     */
    @PostMapping("/queryPageListByEval")
    @ApiOperation("查询列表页数据")
    public Result<BasePage<Map>> queryPageListByEval(@RequestBody ProjectInfoBO projectInfo) {
        BasePage<Map> mapBasePage = iProjectInfoService.queryPageListByEval(projectInfo);
        return R.ok(mapBasePage);
    }

    /**
     * @param
     * @return
     * @Description:查询项目列表，下拉框使用
     * @author LZY
     * @date 2021-09-17
     */
    @PostMapping("/queryProjectListByName")
    @ApiOperation("查询项目列表，下拉框使用")
    @OperateLog(detail = "查询项目列表，下拉框使用", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryProjectListByName(@RequestBody Map<String, Object> params) {

//        QueryWrapper<ProjectInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.select("id, IFNULL(project_abbreviation,project_name) AS name").ne("project_id", 0).orderByAsc("sort").orderByDesc("create_time");
        List<Map<String, Object>> projectList = iProjectInfoService.queryProjectListByName(params);

        return R.ok(projectList);
    }

    /**
     *
     * @return
     */
    @GetMapping("/sync")
    @ApiOperation("自动同步项目账号")
    @OperateLog(detail = "自动同步项目账号", types = LogTypes.BUILD, behavior = LogBehavior.OTHER)
    public Result syncAccount(){
        return iProjectInfoService.syncAccount();
    }

    /**
     * 工地端-三维大屏查询项目信息和
     * @return
     */
    @PostMapping("/selectProjectInfo")
    @ApiOperation("获取项目竣工时间总造价总面积信息")
    public Result<Map> selectProjectInfo() {

        Map map = iProjectInfoService.selectProjectInfo();
        return R.ok(map);
    }

    @PostMapping("/getLocationInfoById/{id}")
    @ApiOperation("获取项目信息")
    public Result<Map> getLocationInfoById(@PathVariable("id") Integer id) {
        return R.ok(iProjectInfoService.getLocationInfoById(id));
    }
}


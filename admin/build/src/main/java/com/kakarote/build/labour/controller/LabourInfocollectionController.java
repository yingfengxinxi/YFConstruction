package com.kakarote.build.labour.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.labour.entity.BO.LabourInfocollectionBO;
import com.kakarote.build.labour.entity.BO.LabourWorktypeBO;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.labour.entity.PO.LabourSpeworktype;
import com.kakarote.build.labour.entity.PO.LabourWorktype;
import com.kakarote.build.labour.service.ILabourInfocollectionService;
import com.kakarote.build.labour.service.ILabourSpeworktypeService;
import com.kakarote.core.common.*;
import com.kakarote.core.config.MybatisPlusConfig;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.upload.FileServiceFactory;
import com.kakarote.core.servlet.upload.UploadEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 劳务管理--信息采集 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-03-17
 */
@RestController
@RequestMapping("/labourInfocollection")
public class LabourInfocollectionController {

    @Autowired
    private ILabourInfocollectionService labourInfocollectionService;
    @Autowired
    private ILabourSpeworktypeService labourSpeworktypeService;



    @ApiOperation(value = "查询所有采集信息")
    @RequestMapping("/query")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<LabourInfocollection>> query(){
        return R.ok(labourInfocollectionService.list());
    }

    @PostMapping("/list")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<LabourInfocollection>> list(@RequestBody LabourInfocollectionBO labourInfocollectionBO) {
        BasePage<LabourInfocollection> list = labourInfocollectionService.selectList(labourInfocollectionBO);
        return Result.ok(list);
    }

    @PostMapping("/queryById/{valveId}")
    @ApiOperation("根据主键查询信息")
    @OperateLog(detail = "根据主键查询信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryById(@PathVariable @NotNull Integer valveId){
        return R.ok(labourInfocollectionService.getById(valveId));
    }

    @PostMapping("/add")
    @ApiOperation("新增采集信息")
    @OperateLog(detail = "新增采集信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result add(@Validated
                      @RequestParam(value = "file",required = false)
                                    MultipartFile file,
                      @RequestParam (value = "form",required = false)
                      @ApiParam("信息采集") String message,
                      @RequestParam (value = "cerForm",required = false)
                                  String cerForm
    ) {
        LabourInfocollection labourInfocollection = JSON.parseObject(message, LabourInfocollection.class);
        //特种工--证书信息新增
        LabourSpeworktype labourSpeworktype = JSON.parseObject(cerForm, LabourSpeworktype.class);
        Result result = labourInfocollectionService.saveInfo(file, labourInfocollection, labourSpeworktype);
        return result;
    }


    @PostMapping("/update")
    @ApiOperation("修改采集信息")
    @OperateLog(detail = "修改采集信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody LabourInfocollection labourWorktype) {
        labourWorktype.setUpdateTime(new Date());
        return Result.ok(labourInfocollectionService.saveOrUpdate(labourWorktype));
    }


    @PostMapping("/delete/{id}")
    @ApiOperation("删除采集信息")
    @OperateLog(detail = "删除采集信息", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result remove(@PathVariable Integer id) {
        return Result.ok(labourInfocollectionService.removeById(id));
    }

    @PostMapping("/getTeamLeaderList")
    @ApiOperation("查询班组长信息列表")
    @OperateLog(detail = "查询班组长信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String, Object>>> getTeamLeaderList(@RequestBody LabourInfocollectionBO labourInfocollectionBO) {
        BasePage<Map<String, Object>> list = labourInfocollectionService.getTeamLeaderList(labourInfocollectionBO);
        return Result.ok(list);
    }

    @PostMapping("/addOddJob")
    @ApiOperation("新增零工信息")
    @OperateLog(detail = "新增零工信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result addOddJob(@Validated
                      @RequestParam(value = "file",required = false)
                              MultipartFile file,
                      @RequestParam (value = "form",required = false)
                      @ApiParam("信息采集") String message
    ) {

        LabourInfocollection labourInfocollection = JSON.parseObject(message, LabourInfocollection.class);

        Result result = labourInfocollectionService.saveOddJobInfo(file, labourInfocollection);
        return result;
    }

    @PostMapping("/selectInfoByMapNoPage")
    @ApiOperation("查询信息列表NoPage")
    @OperateLog(detail = "查询信息列表NoPage", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map>> selectInfoByMapNoPage(
            @RequestBody LabourInfocollectionBO labourInfocollectionBO) {
        List<Map> list = labourInfocollectionService.selectInfoByMapNoPage(labourInfocollectionBO);
        return Result.ok(list);
    }


    //2021-04-11新增给安全教育使用花名册查询时移除已存在的人员数据使用列表
    @PostMapping("/listSafetyTraining")
    @ApiOperation("查询信息列表")
    public Result<BasePage<LabourInfocollection>> listSafetyTraining(@RequestBody LabourInfocollectionBO labourInfocollectionBO) {
        BasePage<LabourInfocollection> list = labourInfocollectionService.selectListSafetyTraining(labourInfocollectionBO);
        return Result.ok(list);
    }


    @PostMapping("/selectAttendanceData")
    @ApiOperation("人员现场管理-查询今日考勤数据")
    @OperateLog(detail = "人员现场管理-查询今日考勤数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map> selectAttendanceData(@RequestBody Map<String, Object> params) {
        Map<String, Object> map = labourInfocollectionService.selectAttendanceData(params);
        return Result.ok(map);
    }
    @PostMapping("/selectAttendanceDataList")
    @ApiOperation("人员现场管理-查询今日考勤数据列表")
    @OperateLog(detail = "人员现场管理-查询今日考勤数据列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> selectAttendanceDataList(@RequestBody Map<String,Object> param) {
        BasePage<Map<String,Object>>  map = labourInfocollectionService.selectAttendanceDataList(param);
        return Result.ok(map);
    }
    @PostMapping("/selectSceneWorkTypeList")
    @ApiOperation("人员现场管理- 查询工种人数")
    @OperateLog(detail = "人员现场管理-查询工种人数", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectSceneWorkTypeList() {
        List<Map<String, Object>> list = labourInfocollectionService.selectSceneWorkTypeList();
        return Result.ok(list);
    }

    @PostMapping("/selectTeamList")
    @ApiOperation("人员现场管理- 查询班组人数")
    @OperateLog(detail = "人员现场管理-查询工种人数", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectTeamList() {
        List<Map<String, Object>> list = labourInfocollectionService.selectTeamList();
        return Result.ok(list);
    }

    @PostMapping("/selectNationAndGender")
    @ApiOperation("人员现场管理- 民族和性别")
    @OperateLog(detail = "人员现场管理-民族和性别", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>> selectNationAndGender() {
        Map<String, Object> map = labourInfocollectionService.selectNationAndGender();
        return Result.ok(map);
    }

    @PostMapping("/selectAttendanceDataByProject")
    @ApiOperation("集团数据今日施工在场人数-人员现场管理-查询今日考勤数据")
    @OperateLog(detail = "集团数据今日施工在场人数-人员现场管理-查询今日考勤数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map>> selectAttendanceDataByProject(@RequestBody List<Map> list) {
        List<Map> map = labourInfocollectionService.selectAttendanceDataByProjectIds(list);
        return Result.ok(map);
    }

    @PostMapping("/selectOddIdAndName")
    @ApiOperation("查询零工的id和name 零工签证使用")
    @OperateLog(detail = "查询零工的id和name 零工签证使用", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectOddIdAndName() {
        List<Map<String, Object>> list = labourInfocollectionService.selectOddIdAndName();
        return Result.ok(list);
    }

    @PostMapping("/queryByIdCard/{idCard}")
    @ApiOperation("根据身份证号查询信息")
    @OperateLog(detail = "根据身份证号查询信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryByIdCard(@PathVariable @NotNull String idCard){
        LambdaQueryWrapper<LabourInfocollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LabourInfocollection::getIdCard, idCard).ne(LabourInfocollection::getProjectId, "0")
                .last("limit 1").orderByDesc(LabourInfocollection::getId);
        LabourInfocollection labourInfocollection = labourInfocollectionService.getOne(queryWrapper);
        return R.ok(labourInfocollection);
    }

    @PostMapping("/queryEvaluationById/{valveId}")
    @ApiOperation("根据主键查询员工评价信息")
    @OperateLog(detail = "根据主键查询员工评价信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryEvaluationById(@PathVariable @NotNull Integer valveId){
        QueryWrapper queryWrapper =new QueryWrapper();
        queryWrapper.select("id","evaluation").eq("id",valveId);
        Map map = labourInfocollectionService.getMap(queryWrapper);
        return R.ok(map);
    }

    @PostMapping("/queryProjectsByIdCard/{idCard}/{projectId}")
    @ApiOperation("根据身份证查询项目履历信息")
    @OperateLog(detail = "根据身份证查询项目履历信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryProjectsByIdCard(@PathVariable @NotNull String idCard,@PathVariable @NotNull Integer projectId){
        List<Map<String, Object>> list = labourInfocollectionService.queryProjectsByIdCard(idCard,projectId);
        return R.ok(list);
    }

    @PostMapping("/selectAgeRange")
    @ApiOperation("大屏-查询年龄分布")
    @OperateLog(detail = "大屏-查询年龄分布", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectAgeRange() {
        List<Map<String, Object>> list = labourInfocollectionService.selectAgeRange();
        return Result.ok(list);
    }

    @PostMapping("/selectRealTimeNumber")
    @ApiOperation("大屏-现场实时人数")
    @OperateLog(detail = "大屏-现场实时人数", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>> selectRealTimeNumber() {
        Map<String, Object> map = labourInfocollectionService.selectRealTimeNumber();
        return Result.ok(map);
    }

    @PostMapping("/selectListByTeam/{teamId}")
    @ApiOperation("根据班组Id查询人员数据")
    @OperateLog(detail = "根据班组Id查询人员数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectListByTeam(@PathVariable Integer teamId) {
        List<Map<String, Object>> List = labourInfocollectionService.selectListByTeam(teamId);
        return Result.ok(List);
    }

    /**
     * 语音输入使用，根据关键字查询相关人员信息,该
     * @param params
     * @return
     */
    @PostMapping("/selectInfoByName")
    @ApiOperation("查询班组长信息列表")
    @OperateLog(detail = "查询班组长信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>> selectInfoByName(@RequestBody Map<String, Object> params) {
        Map<String, Object> personInfo = labourInfocollectionService.selectInfoByName(params);
        return Result.ok(personInfo);
    }

    @GetMapping("/getInfocollectionByPhone/{phone}")
    @ApiOperation("根据联系电话查询")
    @OperateLog(detail = "根据联系电话查询", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>>  getInfocollectionByPhone(@PathVariable Integer phone){
        Map<String, Object> map = labourInfocollectionService.getInfocollectionByPhone(phone);
        return Result.ok(map);
    }


    @PostMapping("/updateByIdCard")
    @ApiOperation("根据身份证号修改采集信息")
    @OperateLog(detail = "根据身份证号修改采集信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result updateByIdCard(@Validated @RequestBody LabourInfocollection labourWorktype) {
        if(labourWorktype.getIdCard() == null || "".equals(labourWorktype.getIdCard())){
            return Result.error(500,"身份证不能为空");
        }
        labourWorktype.setUpdateTime(new Date());
        QueryWrapper<LabourInfocollection> wrapper = new QueryWrapper<>();
        wrapper.eq("id_card",labourWorktype.getIdCard()).eq("tenant_id",labourWorktype.getTenantId()).eq("project_id",labourWorktype.getProjectId());
        return Result.ok(labourInfocollectionService.update(labourWorktype,wrapper));
    }
}


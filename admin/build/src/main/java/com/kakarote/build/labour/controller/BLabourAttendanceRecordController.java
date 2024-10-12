package com.kakarote.build.labour.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceBO;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceRecordBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendance;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceRecord;
import com.kakarote.build.labour.service.IBLabourAttendanceRecordService;
import com.kakarote.build.labour.service.IBLabourAttendanceService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 考勤记录管理 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-04-14
 */
@RestController
@RequestMapping("/bLabourAttendanceRecord")
public class BLabourAttendanceRecordController {


    @Autowired
    private IBLabourAttendanceRecordService iBLabourAttendanceRecordService;


    @RequestMapping("/list")
    @ApiOperation("查询安全教育培训管理列表")
    @OperateLog(detail = "查询安全教育培训管理列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BLabourAttendanceRecord>> list(@RequestBody BLabourAttendanceRecordBO bLabourAttendanceRecordBO)
    {
        BasePage<BLabourAttendanceRecord> list = iBLabourAttendanceRecordService.selectList(bLabourAttendanceRecordBO);
        return Result.ok(list);
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BLabourAttendanceRecord bLabourAttendanceRecord)
    {

        bLabourAttendanceRecord.setCreateBy(UserUtil.getUser().getRealname());
        bLabourAttendanceRecord.setCreateTime(new Date());
        iBLabourAttendanceRecordService.save(bLabourAttendanceRecord);
        return Result.ok();
    }

    /**
     * 根据数据主键ID查询信息
     *
     * @param id 数据主键
     * @return
     */
    @GetMapping(value = "/selectById/{id}")
    @ApiOperation("查询单条数据详细")
    @OperateLog(detail = "查询单条数据详细",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result getInfo(@PathVariable String id)
    {
        return Result.ok(iBLabourAttendanceRecordService.getById(id));
    }




    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BLabourAttendanceRecord bLabourAttendanceRecord)
    {
        bLabourAttendanceRecord.setUpdateBy(UserUtil.getUser().getRealname());
        bLabourAttendanceRecord.setUpdateTime(new Date());
        iBLabourAttendanceRecordService.updateById(bLabourAttendanceRecord);
        return Result.ok();
    }

    /**
     * 删除
     */
    @GetMapping(value = "/deleteDetails/{id}")
    @ApiOperation("删除数据详情")
    @OperateLog(detail = "删除数据详情",types= LogTypes.SYS ,behavior = LogBehavior.DELETE)
    public Result remove(@PathVariable String id)
    {
        return Result.ok( iBLabourAttendanceRecordService.removeById(id));
    }

    /**
     * 多条删除
     */
    @PostMapping("/delTrueByIds")
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            iBLabourAttendanceRecordService.removeById(id);
        }
        return Result.ok();
    }


    /**
     * 查询该人员的打卡数据
     *
     * @param id 数据主键
     * @return
     */
    @GetMapping(value = "/selectByMonth/{id}/{attendanceMonth}/{infoName}")
    @ApiOperation("查询单条数据详细")
    @OperateLog(detail = "查询单条数据详细",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result selectByMonth(@PathVariable String id,@PathVariable String attendanceMonth,@PathVariable String infoName)
    {
        BLabourAttendanceRecord bLabourAttendanceRecord=new BLabourAttendanceRecord();
        bLabourAttendanceRecord.setCreateBy(attendanceMonth);
        bLabourAttendanceRecord.setInfocollectionId(id);
        bLabourAttendanceRecord.setInfocollectionName(infoName);
        List<BLabourAttendanceRecord> list=   iBLabourAttendanceRecordService.selectByMonth(bLabourAttendanceRecord);
        return Result.ok(list);
    }

    @PostMapping("/excelImport")
    @ApiOperation("excel导入考勤")
    public Result<JSONObject> excelImport(@RequestParam("file") MultipartFile file) {
        JSONObject object = iBLabourAttendanceRecordService.excelImport(file);
        return R.ok(object);
    }

    @PostMapping("/getAttendanceDetailList")
    @ApiOperation("考勤明细-数据列表")
    @OperateLog(detail = "考勤明细-数据列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String, Object>>> getAttendanceDetailList(@RequestBody BLabourAttendanceRecordBO bLabourAttendanceRecordBO)
    {
        BasePage<Map<String, Object>> list = iBLabourAttendanceRecordService.getAttendanceDetailList(bLabourAttendanceRecordBO);
        return Result.ok(list);
    }

    @PostMapping("/selectEntryAndExit")
    @ApiOperation("查詢进场和出场数据")
    @OperateLog(detail = "考勤明细-数据列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>> selectEntryAndExit()
    {
        Map<String, Object> data = iBLabourAttendanceRecordService.selectEntryAndExit();
        return Result.ok(data);
    }

    @PostMapping("/selectPositionAttendance")
    @ApiOperation("各工种本日考勤率统计")
    @OperateLog(detail = "各工种本日考勤率统计",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectPositionAttendance(){
        return  R.ok(iBLabourAttendanceRecordService.selectPositionAttendance());
    }

    /**
     * 实时动态-该接口监管端使用，只查询管理人员数据
     * @return
     */
    @PostMapping("/selectDynamic")
    @ApiOperation("实时动态-该接口监管端使用，只查询管理人员数据")
    @OperateLog(detail = "实时动态-该接口监管端使用，只查询管理人员数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectDynamic(){
        return  R.ok(iBLabourAttendanceRecordService.selectDynamic());
    }


    @PostMapping("/AttendanceRate")
    @ApiOperation("单人缺勤率排行")
    @OperateLog(detail = "单人缺勤率排行",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result AttendanceRate(){
        return  R.ok(iBLabourAttendanceRecordService.AttendanceRate());
    }

    @PostMapping("/projectAttendanceRate")
    @ApiOperation("项目缺勤率")
    @OperateLog(detail = "项目缺勤率",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result projectAttendanceRate(){
        return  R.ok(iBLabourAttendanceRecordService.projectAttendanceRate());
    }

    @PostMapping("/projectAttendanceRateGo")
    @ApiOperation("项目30天出勤率")
    @OperateLog(detail = "项目30天出勤率",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result projectAttendanceRateGo(){
        return  R.ok(iBLabourAttendanceRecordService.projectAttendanceRateGo());
    }

    @PostMapping("/peopleNumber")
    @ApiOperation("人员详情统计")
    @OperateLog(detail = "人员详情统计",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result peopleNumber(){
        return  R.ok(iBLabourAttendanceRecordService.peopleNumber());
    }

    /**
     * @description 大屏人员详情统计图表点击弹窗，展示人员信息列表
     * @author wnj58 
     * @Params [params]
     * @updateTime 2022/4/19 9:41
     * @return com.kakarote.core.common.Result
     * @throws 
     */
    @PostMapping("/peopleInfoDetails")
    @ApiOperation("人员详情列表")
    @OperateLog(detail = "人员详情列表",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result peopleInfoDetails(@RequestBody Map<String,Object> params){
        return  R.ok(iBLabourAttendanceRecordService.peopleInfoDetails(params));
    }
    @PostMapping("/workersConstitute")
    @ApiOperation("建筑工人构成分析统计")
    @OperateLog(detail = "建筑工人构成分析统计",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result workersConstitute(){
        return  R.ok(iBLabourAttendanceRecordService.workersConstitute());
    }

    @PostMapping("/workerTypeConstitute")
    @ApiOperation("建筑工人工种分析统计")
    @OperateLog(detail = "建筑工人工种分析统计",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result workerTypeConstitute(){
        return  R.ok(iBLabourAttendanceRecordService.workerTypeConstitute());
    }

    @PostMapping("/wageConstitute")
    @ApiOperation("工资发放统计")
    @OperateLog(detail = "工资发放统计",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result wageConstitute(){
        return  R.ok(iBLabourAttendanceRecordService.wageConstitute());
    }

    @PostMapping("/defaultWageConstitute")
    @ApiOperation("拖欠工资统计")
    @OperateLog(detail = "拖欠工资统计",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result defaultWageConstitute(){
        return  R.ok(iBLabourAttendanceRecordService.defaultWageConstitute());
    }

    @PostMapping("/selectAttendanceNumber")
    @ApiOperation("近30天出勤数")
    @OperateLog(detail = "近30天出勤数", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectAttendanceNumber() {
        List<Map<String, Object>> List = iBLabourAttendanceRecordService.selectAttendanceNumber();
        return Result.ok(List);
    }

    /**
     * 实时动态-该接口项目端使用，查询管理人员和建筑工人数据
     * @return
     */
    @PostMapping("/selectDynamicNew")
    @ApiOperation("实时动态-该接口项目端使用，查询管理人员和建筑工人数据")
    @OperateLog(detail = "实时动态-该接口项目端使用，查询管理人员和建筑工人数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectDynamicNew(@RequestBody Map<String,Object> param){
        return  R.ok(iBLabourAttendanceRecordService.selectDynamicNew(param));
    }

    /**
    * @Description:通过用户id查询本日最后打卡数据
    * @author shz
    * @date 2022-4-29 11:46
    * @param 
    * @return 
    */
    @GetMapping(value = "/selectByUserId/{id}")
    @ApiOperation("通过用户id查询本日最后打卡数据")
    @OperateLog(detail = "通过用户id查询本日最后打卡数据",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result selectByUserId(@PathVariable Integer id){
        return  R.ok(iBLabourAttendanceRecordService.selectByUserId(id));
    }

    @PostMapping(value = "/addClock")
    @ApiOperation("用户打卡")
    @OperateLog(detail = "用户打卡",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result addClock(@RequestBody  BLabourAttendanceRecord record){
        iBLabourAttendanceRecordService.addClock(record);
        return R.ok();
    }
}


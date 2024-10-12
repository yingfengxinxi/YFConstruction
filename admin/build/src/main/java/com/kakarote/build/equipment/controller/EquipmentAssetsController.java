package com.kakarote.build.equipment.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.build.equipment.entity.BO.EquipmentAssetsBO;
import com.kakarote.build.equipment.entity.PO.EquipmentAssets;
import com.kakarote.build.equipment.service.IBEquipmentTowerService;
import com.kakarote.build.equipment.service.IEquipmentAssetsService;
import com.kakarote.build.equipment.service.IEquipmentTypeService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备资产 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
@RestController
@RequestMapping("/equipmentAssets")
public class EquipmentAssetsController {

    @Autowired
    private IEquipmentAssetsService assetsService;

    @Autowired
    private IBEquipmentTowerService towerService;
    @PostMapping("/list")
    @ApiOperation("查询设备资产列表")
    @OperateLog(detail = "查询设备资产列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<EquipmentAssets>> list(@RequestBody EquipmentAssetsBO equipmentAssetsBO){
        BasePage<EquipmentAssets> list = assetsService.selectByPage(equipmentAssetsBO);
        return Result.ok(list);
    }

    @PostMapping("/selectPlaneByType/{typeId}")
    @ApiOperation("查询设备资产列表")
    @OperateLog(detail = "查询设备资产列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<List<EquipmentAssets>> selectPlaneByType(@PathVariable Integer typeId){
        List<EquipmentAssets> list = assetsService.selectPlaneByType(typeId);
        return Result.ok(list);
    }

    @PostMapping("/listByLocationId")
    @ApiOperation("查询设备资产列表")
    @OperateLog(detail = "查询设备资产列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<List<Map<String,Object>>> listByLocationId(@RequestBody EquipmentAssetsBO equipmentAssetsBO){
        List<Map<String,Object>> list = assetsService.listByLocationId(equipmentAssetsBO.getLocationId());
        return Result.ok(list);
    }

    @PostMapping("/save")
    @ApiOperation("保存或更新设备资产信息")
    @OperateLog(detail = "保存或更新设备资产信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result save(@RequestBody EquipmentAssets equipmentAssets) {
        // 进行设备编号唯一校验
        LambdaQueryWrapper<EquipmentAssets> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EquipmentAssets::getAssetCode, equipmentAssets.getAssetCode());
        if (equipmentAssets.getAssetId() != null) {
            queryWrapper.ne(EquipmentAssets::getAssetId, equipmentAssets.getAssetId());
        }
        queryWrapper.last("limit 1");
        EquipmentAssets assets = assetsService.getOne(queryWrapper);
        if (assets != null) {
            throw new CrmException(500, "设备编号已存在");
        }
        if (equipmentAssets.getAssetId() == null) {
            assetsService.save(equipmentAssets);
        } else {
            equipmentAssets.setUpdateUserId(UserUtil.getUserId());
            assetsService.updateById(equipmentAssets);
        }
        return Result.ok(equipmentAssets.getAssetId());
    }

    @PostMapping("/get/{id}")
    @ApiOperation("查询设备资产详情")
    @OperateLog(detail = "查询设备资产详情", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result get(@PathVariable Integer id) {
        EquipmentAssets equipmentAssets = assetsService.getById(id);
        return Result.ok(equipmentAssets);
    }

    @PostMapping("/delete")
    @ApiOperation("删除设备资产详情")
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(detail = "删除设备资产详情", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@RequestBody EquipmentAssets assets) {
        return R.ok(assetsService.removeById(assets.getAssetId()));
    }

    @PostMapping("/selectEquList")
    @ApiOperation("查询设备资产列表")
    @OperateLog(detail = "查询设备资产列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<List<EquipmentAssets>> selectEquList(@RequestBody EquipmentAssetsBO equipmentAssetsBO){
        List<EquipmentAssets> list = assetsService.selectEquList(equipmentAssetsBO);
        return Result.ok(list);
    }

    /**
     * app自动喷淋控制器页面使用
     * @return
     */
    @PostMapping("/selectSprayList")
    @ApiOperation("查询喷淋设备列表")
    @OperateLog(detail = "查询喷淋设备列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectSprayList(){
        List<Map<String, Object>> list = assetsService.selectSprayList();
        return Result.ok(list);
    }

    /**
     * 塔吊监测页面使用
     * @return
     */
    @PostMapping("/selectTowerCrane")
    @ApiOperation("查询塔吊设备")
    @OperateLog(detail = "查询塔吊设备",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<List<EquipmentAssets>> selectTowerCrane(){
        List<EquipmentAssets> list = assetsService.selectTowerCrane();
        return Result.ok(list);
    }

    @PostMapping("/selectVideoMonitor")
    @ApiOperation("查询视频监控流地址")
    @OperateLog(detail = "查询视频监控流地址",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result selectVideoMonitor(@RequestBody Map<String,Object> map){
        Map<String,Object> list = assetsService.selectVideoMonitor(map);
        return Result.ok(list);
    }

    @PostMapping("/groupList")
    @ApiOperation("查询设备资产列表")
    @OperateLog(detail = "查询设备资产列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<EquipmentAssets>> groupList(@RequestBody EquipmentAssetsBO equipmentAssetsBO){
        BasePage<EquipmentAssets> list = assetsService.selectByPageGroup(equipmentAssetsBO);
        return Result.ok(list);
    }

    @PostMapping("/groupList2")
    @ApiOperation("查询设备资产列表,视频监控查看使用")
    @OperateLog(detail = "查询设备资产列表,视频监控查看使用",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result groupList2(@RequestBody EquipmentAssetsBO equipmentAssetsBO){
        List<Map<String,Object>> list = assetsService.groupList2(equipmentAssetsBO);
        return Result.ok(list);
    }

    /**
     * 设备监控中心：核对设备是否存在
     */
    @GetMapping("/check/{mn}")
    @ApiOperation("设备监控中心核对设备")
    public String checkEq(@PathVariable String mn){
        List<EquipmentAssets> list = assetsService.lambdaQuery().eq(EquipmentAssets::getAssetCode,mn)
                .eq(EquipmentAssets::getStatus,"1").isNotNull(EquipmentAssets::getTenantId)
                .isNotNull(EquipmentAssets::getProjectId).list();
        if(list.size() == 0){
            return "0";
        }
        return "1";
    }

    /**
     * 设备监控中心：接收、处理数据
     */
    @PostMapping("/saveEq")
    @ApiOperation("设备监控中心：接收、处理数据")
    public Result saveEq(@RequestBody JSONObject jsonObject){
        return assetsService.monitorSaveEqData(jsonObject);
    }

    @PostMapping("/getDeviceId/{assectId}")
    @ApiOperation("根据设备资产id，查询视频播放地址")
    @OperateLog(detail = "根据设备资产id，查询视频播放地址", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getDeviceId(@PathVariable String  assectId) {
        return Result.ok(assetsService.getDeviceId(assectId));
    }

    /**
     * 设备监控中心：接收设备数据
     * @param jsonObject
     *  {dataList:{},//测点数据（必含）
     *   statusList:{}//状态数据（不必含）
     *  }
     * @return
     */
    @PostMapping("/recept")
    @ApiOperation("设备监控中心：接收设备数据")
    public Result receptEqData(@RequestBody JSONObject jsonObject){
        return assetsService.receptEqData(jsonObject);
    }

    @RequestMapping("/equiStateList")
    @ApiOperation("设备状态列表查询")
    @OperateLog(detail = "设备状态列表查询",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result equiStateList(@RequestBody EquipmentAssetsBO equipmentAssetsBO){
        return  R.ok(assetsService.equiStateList(equipmentAssetsBO));
    }

    @RequestMapping("/equiStateListNew")
    @ApiOperation("设备状态列表查询")
    @OperateLog(detail = "设备状态列表查询",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result equiStateListNew(@RequestBody EquipmentAssetsBO equipmentAssetsBO){
        return  R.ok(assetsService.equiStateListNew(equipmentAssetsBO));
    }

    /**
     * 获取设备列表  关联监测类型
     */
    @GetMapping("/getEqListByMoType/{moType}")
    @ApiOperation("设备监控中心核对设备")
    public Result getEqListByMoType(@PathVariable String moType){
        List<EquipmentAssets> list = assetsService.getEqListByMoType(moType);
        return Result.ok(list);
    }

    /**
     * 获取设备列表  关联监测类型
     */
    @GetMapping("/selectAllOverview")
    @ApiOperation("查询总览设备列表")
    public Result selectAllOverview(){
        List<Map> list = assetsService.selectAllOverview();
        return Result.ok(list);
    }
    /**
     * @Description:设备报警统计
     * @author shz
     * @date 2021-12-27 15:34
     * @param
     * @return
     */
    @GetMapping("/selectAllAlertNew")
    @ApiOperation("设备报警统计")
    public Result selectAllAlertNew(){
        Map map = assetsService.selectAllAlertNew();
        return Result.ok(map);
    }

    /**
     * 获取设备列表  关联监测类型
     */
    @GetMapping("/selectTowerCraneOverview")
    @ApiOperation("查询塔机列表")
    public Result selectTowerCraneOverview(){
        List<Map> list = assetsService.selectTowerCraneOverview();
        return Result.ok(list);
    }
    /**
    * @Description:塔机设备报警统计
    * @author shz
    * @date 2021-12-27 15:34
    * @param 
    * @return 
    */
    @GetMapping("/selectTowerCraneAlertNew")
    @ApiOperation("塔机设备报警统计")
    public Result selectTowerCraneAlertNew(){
        Map map = assetsService.selectTowerCraneAlertNew();
        return Result.ok(map);
    }

    /**
    * @Description:查询塔机信息
    * @author shz
    * @date 2021-12-27 17:21
    * @param 
    * @return 
    */
    @GetMapping("/selectTowerCraneById/{id}")
    @ApiOperation("查询塔机信息")
    public Result selectTowerCraneById(@PathVariable String id){
        Map map = assetsService.selectTowerCraneById(id);
        return Result.ok(map);
    }

    /**
    * @Description:查询升降机列表
    * @author shz
    * @date 2021-12-30 9:33
    * @param 
    * @return 
    */
    @GetMapping("/selectElevatorOverview")
    @ApiOperation("查询升降机列表")
    public Result selectElevatorOverview(){
        List<Map> list = assetsService.selectElevatorOverview();
        return Result.ok(list);
    }

    /**
    * @Description:升降机设备报警统计
    * @author shz
    * @date 2021-12-30 9:33
    * @param 
    * @return 
    */
    @GetMapping("/selectElevatorAlertNew")
    @ApiOperation("升降机设备报警统计")
    public Result selectElevatorAlertNew(){
        Map map = assetsService.selectElevatorAlertNew();
        return Result.ok(map);
    }

    /**
    * @Description:查询升降机信息
    * @author shz
    * @date 2021-12-30 9:34
    * @param 
    * @return 
    */
    @GetMapping("/selectElevatorById/{id}")
    @ApiOperation("查询升降机信息")
    public Result selectElevatorById(@PathVariable String id){
        Map map = assetsService.selectElevatorById(id);
        return Result.ok(map);
    }

    @GetMapping("/selectPitOverview")
    @ApiOperation("查询基坑列表")
    public Result selectPitOverview(){
        List<Map> list = assetsService.selectPitOverview();
        return Result.ok(list);
    }

    @GetMapping("/selectFormworkOverview")
    @ApiOperation("查询高支模列表")
    public Result selectFormworkOverview(){
        List<Map> list = assetsService.selectFormworkOverview();
        return Result.ok(list);
    }

    @GetMapping("/selectPitAlertNew")
    @ApiOperation("基坑设备报警统计")
    public Result selectPitAlertNew(){
        Map map = assetsService.selectPitAlertNew();
        return Result.ok(map);
    }

    @GetMapping("/selectFormworkAlertNew")
    @ApiOperation("基坑设备报警统计")
    public Result selectFormworkAlertNew(){
        Map map = assetsService.selectFormworkAlertNew();
        return Result.ok(map);
    }

    @GetMapping("/selectHookOverview")
    @ApiOperation("吊钩监控列表")
    @OperateLog(detail = "吊钩监控列表",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectHookOverview(){
        List<Map> list = assetsService.selectHookOverview();
        return Result.ok(list);
    }
    /**
     * @description
     * @author wnj58 
     * @Params [map]
     * @updateTime 2022/3/4 17:05
     * @return com.kakarote.core.common.Result
     * @throws 
     */
    @PostMapping("/selectVideoAccessNum")
    @ApiOperation("统计当前视频接入工地数量，设备数量，宁津监管小程序使用")
    @OperateLog(detail = "统计当前视频接入工地数量，设备数量",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectVideoAccessNum(@RequestBody Map<String,Object> map){
        return R.ok(assetsService.videoAccessNum(map));
    }
    /**
     * @description 
     * @author wnj58 
     * @Params [map]
     * @updateTime 2022/3/8 15:51
     * @return com.kakarote.core.common.Result
     * @throws 
     */
    @PostMapping("/selectProjectAccessNum")
    @ApiOperation("分类统计接入项目数量，宁津监管小程序使用")
    @OperateLog(detail = "分类统计接入项目数量",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectProjectAccessNum(@RequestBody Map<String,Object> map){
        return R.ok(assetsService.selectProjectAccessNum(map));
    }
    /**
     * @description
     * @author wnj58
     * @Params [map]
     * @updateTime 2022/3/8 15:51
     * @return com.kakarote.core.common.Result
     * @throws
     */
    @PostMapping("/selectEquiAccessNum")
    @ApiOperation("分类统计接入设备数量，宁津监管小程序使用")
    @OperateLog(detail = "分类统计接入设备数量",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectEquiAccessNum(@RequestBody Map<String,Object> map){
        return R.ok(assetsService.selectEquiAccessNum(map));
    }
    /**
     * @description
     * @author wnj58 
     * @Params [map]
     * @updateTime 2022/3/8 16:15
     * @return com.kakarote.core.common.Result
     * @throws 
     */
    @PostMapping("/selectEquiAlarmNum")
    @ApiOperation("分类统计扬尘，AI报警数据，宁津监管小程序使用")
    @OperateLog(detail = "分类统计扬尘，AI报警数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectEquiAlarmNum(@RequestBody Map<String,Object> map){
        return R.ok(assetsService.selectEquiAlarmNum(map));
    }

    @PostMapping("/selectVideoEquiList")
    @ApiOperation("分类统计扬尘，AI报警数据，宁津监管小程序使用")
    @OperateLog(detail = "分类统计扬尘，AI报警数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectVideoEquiList(@RequestBody Map<String,Object> map){
        return R.ok(assetsService.selectVideoEquiList(map));
    }

    /**
     * type 1：预警 2：报警 3：环境 4：塔机 5：升降机 6：基坑 7：高支模 8：人员 9：监控 10：喷淋 11：隐患
     * @param params
     * @return
     */
    @PostMapping("/selectThreeDimensional")
    @ApiOperation("三维大屏-根据类型查询对应xyz坐标信息")
    @OperateLog(detail = "三维大屏-根据类型查询对应xyz坐标信息",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectThreeDimensional(@RequestBody Map<String,Object> params){
        return R.ok(assetsService.selectThreeDimensional(params));
    }

    @PostMapping("/selectByPageGroupCount")
    @ApiOperation("枣庄危大工程设备类型占比")
    @OperateLog(detail = "枣庄危大工程设备类型占比", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectByPageGroupCount(@RequestBody Map<String, Object> params) {
        return Result.ok(assetsService.selectByPageGroupCount(params));
    }

    /**
     * 根据监测类型，查询设备信息列表 该接口关联三维模型，未使用三维模型的项目不可用
     * @return
     */
    @PostMapping("/selectEquiListByThree")
    @ApiOperation("三维大屏-根据类型查询对应xyz坐标信息")
    @OperateLog(detail = "三维大屏-根据类型查询对应xyz坐标信息",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String, Object>>> selectEquiListByThree(@RequestBody EquipmentAssetsBO assetsBO){
        return R.ok(assetsService.selectEquiListByThree(assetsBO));
    }

    /**
    * @Description:获取所有项目下的塔机信息
    * @author shz
    * @date 2022-7-15 16:13
    * @param 
    * @return 
    */
    @PostMapping("/selectAllTower")
    @ApiOperation("三维大屏-查询项目下塔机信息")
    @OperateLog(detail = "三维大屏-查询项目下塔机信息",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public  Result<List<Map>> selectAllTower(){
        return R.ok(assetsService.selectAllTower());
    }

    /**
     * @Description:获取所有项目下的升降机信息
     * @author shz
     * @date 2022-7-15 16:13
     * @param
     * @return
     */
    @PostMapping("/selectAllElevator")
    @ApiOperation("三维大屏-查询项目下升降机信息")
    @OperateLog(detail = "三维大屏-查询项目下升降机信息",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public  Result<List<Map>> selectAllElevator(){
        return R.ok(assetsService.selectAllElevator());
    }

    /**
     * 查询接入设备的工地
     * @param equipmentAssetsBO
     * @return
     */
    @PostMapping("/selectProjectByEquipment")
    @ApiOperation("查询接入设备工地")
    @OperateLog(detail = "查询接入设备工地", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> selectProjectByEquipment(@RequestBody EquipmentAssetsBO equipmentAssetsBO) {
        return Result.ok(assetsService.selectProjectByEquipment(equipmentAssetsBO));
    }

    /**
     * 查询设备列表信息 监管端使用 返回项目名称
     * @param equipmentAssetsBO
     * @return
     */
    @PostMapping("/selectListByProject")
    @ApiOperation("监管端-查询设备列表")
    @OperateLog(detail = "监管端-查询设备列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<EquipmentAssets>> selectListByProject(@RequestBody EquipmentAssetsBO equipmentAssetsBO){
        BasePage<EquipmentAssets> list = assetsService.selectListByProject(equipmentAssetsBO);
        return Result.ok(list);
    }
}


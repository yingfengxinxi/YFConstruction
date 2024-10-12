package com.qdoner.datarecept.controller;


import com.kakarote.core.common.Result;
import com.qdoner.datarecept.ai.service.IAiDregCarService;
import com.qdoner.datarecept.env.service.IEnvPostLogService;
import com.qdoner.datarecept.equipment.service.IEquipmentPitDataAtBakService;
import com.qdoner.datarecept.qual.service.IQualStandardRearingRoomService;
import com.qdoner.datarecept.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
* @Description:设备数据对接接口
* @author shz
* @date 2022-10-21 11:18
*/
@RestController
@RequestMapping("/dataUpload")
public class ApiPostLogController {

    @Autowired
    private IBEquipmentElevatorDataAtService equipmentElevatorDataAtService;
    @Autowired
    private IBEquipmentTowerDataAtService equipmentTowerDataAtService;
    @Autowired
    private IBEquipmentTowerEffectService equipmentTowerEffectService;
    @Autowired
    private IBEquipmentElevatorEffectService equipmentElevatorEffectService;
    @Autowired
    private IBEquipmentDriverIdentService iEquipmentDriverIdentService;
    @Autowired
    private IAiVideoAlertService aiVideoAlertService;
    @Autowired
    private IQualStandardRearingRoomService standardRearingRoomService;

    @Autowired
    private IEnvPostLogService envPostLogService;

    @Autowired
    private IAiDregCarService dregCarService;

    @Autowired
    private IEquipmentPitDataAtBakService equipmentPitDataAtBakService;


    @PostMapping("/elevator/saveData")
    @ApiOperation("升降机实时数据上传接口")
    //@OperateLog(detail = "塔机实时数据上传接口", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result elevatorSaveData(@RequestBody List<Map<String, Object>> list) {
        return equipmentElevatorDataAtService.saveData(list);
    }

    @PostMapping("/tower/saveData")
    @ApiOperation("塔机实时数据上传接口")
    public Result towerSaveData(@RequestBody  List<Map<String, Object>>  list) {
        return equipmentTowerDataAtService.saveData(list);
    }

    @PostMapping("/towerEffect/saveData")
    @ApiOperation("塔机功效数据上传接口")
    public Result towerEffectSaveData(@RequestBody  List<Map<String, Object>>  list) {
        return equipmentTowerEffectService.saveData(list);
    }

    @PostMapping("/elevatorEffect/saveData")
    @ApiOperation("升降机功效数据上传接口")
    public Result elevatorEffectSaveData(@RequestBody  List<Map<String, Object>>  list) {
        return equipmentElevatorEffectService.saveData(list);
    }

    @PostMapping("/driverIdent/saveData")
    @ApiOperation("司机数据上传接口")
    public Result driverIdentSaveData(@RequestBody  List<Map<String, Object>>  list) {
        return iEquipmentDriverIdentService.saveData(list);
    }

    @PostMapping("/offLine/saveData")
    @ApiOperation("离线数据上传接口")
    public Result offLine(@RequestBody  List<Map<String, Object>>  list) {
        return iEquipmentDriverIdentService.offLine(list);
    }

    @PostMapping("/aiRecognition/saveData")
    @ApiOperation("AI识别数据上传接口")
    public Result aiRecognition(@RequestBody  List<Map<String, Object>>  list) {
        return aiVideoAlertService.aiRecognition(list);
    }

    @PostMapping("/standardRearinaRoom/saveData")
    @ApiOperation("标养室监测数据上传接口")
    public Result standardRearinaRoom(@RequestBody  List<Map<String, Object>>  list) {
        return standardRearingRoomService.standardRearinaRoom(list);
    }



    /**
     * @api {post} build/SiteDataUpload/data/async 环境监测数据传输
     * @apiDescription 环境监测数据传输
     * @apiVersion 1.0.0
     * @apiName dataAsync
     * @apiGroup async
     *
     *
     * @apiHeader {String} Content-Type=application/json
     * @apiParam {String} appid 项目id，由平台提供
     * @apiParam {String} timestamp 监控时间，格式：yyyyMMddhhmmss
     * @apiParam {String} sign 签名，按照约定的签名算法生成
     * @apiParam {Object[]} data 设备监测数据
     * @apiParamExample {json} data参数示例:
     * [
     *  {
     *      "monitorType": "110", // 监测类型
     *      "monitorValue": "123" // 监测值
     *  }
     * ]
     *
     *
     * @apiSuccess {Number} code 状态码
     * @apiSuccess {String} msg 响应信息
     * @apiSuccess {Object} data 返回数据
     *
     * @apiSuccessExample {json} Success-Response:
     *      {
     *          "code": 0,
     *          "msg": "success",
     *          "data": null
     *      }
     *
     * @apiError 10 签名校验失败
     * @apiError 30 appid不存在,请项目施工单位先在住建委云平台申请后再进行数据推送
     * @apiError 40 timestamp校验失败,时间戳与服务器时间误差不能超过1分钟,格式为yyyyMMddhhmmss
     * @apiError 97 参数格式错误,请根据文档核对
     * @apiError 98 缺少必要的参数,请根据文档核对!
     * @apiError 99 系统异常
     * @apiErrorExample {json} Error-Response:
     *      {
     *          "code": 50,
     *          "msg": "timestamp校验失败,时间戳与服务器时间误差不能超过1分钟,格式为yyyyMMddhhmmss",
     *          "data": null
     *      }
     *
     * @param map
     * @return
     */
    @PostMapping("/data/async")
    @ApiOperation("环境监测数据传输")
    public Result saveOrUpdate(@RequestBody Map<String, Object> map) {
        return envPostLogService.envMonitorDataAsync(map);
    }

    /**
     * 传输车辆清洗数据
     * @param list
     * @return
     */
    @PostMapping("/vehicleCleaningData")
    @ApiOperation("车辆清洗数据传输")
    public Result vehicleCleaningData(@RequestBody List<Map<String, Object>> list) {
        return envPostLogService.vehicleCleaningData(list);
    }

    /**
     * 传输裸土分析报警数据
     * @param list
     * @return
     */
    @PostMapping("/baseSoilAlarmData")
    @ApiOperation("传输裸土分析报警数据")
    public Result baseSoilAlarmData(@RequestBody List<Map<String, Object>> list) {
        return envPostLogService.baseSoilAlarmData(list);
    }

    @PostMapping("/dregCar/saveData")
    @ApiOperation("车记录实时数据上传接口")
    public Result dregCarSaveData(@RequestBody List<Map<String, Object>> list) {
        return dregCarService.saveData(list);
    }

    @PostMapping("/formwork/saveData")
    @ApiOperation("高支模实时数据上传接口")
    public Result formworkSaveData(@RequestBody List<Map<String,Object>> list) {
        return equipmentPitDataAtBakService.saveFormworkData(list);
    }

    @PostMapping("/pit/saveData")
    @ApiOperation("基坑设备实时数据接口")
    public Result pitSaveData(@RequestBody List<Map<String,Object>> list) {
        return equipmentPitDataAtBakService.saveData(list);
    }
}

package com.kakarote.build.api.controller;


import com.kakarote.build.ai.service.IAiDregCarService;
import com.kakarote.build.env.entity.PO.EnvPostLog;
import com.kakarote.build.env.service.IEnvPostLogService;
import com.kakarote.build.equipment.service.*;
import com.kakarote.build.equipment.service.impl.EquipmentTowerDataAtServiceImpl;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 扬尘在线监测设备数据 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-08-23
 */
@RestController
@RequestMapping("/SiteDataUpload")
public class ApiPostLogController {

    @Autowired
    private IEnvPostLogService envPostLogService;
    @Autowired
    private IEquipmentElevatorDataAtService equipmentElevatorDataAtService;
    @Autowired
    private IEquipmentTowerDataAtService equipmentTowerDataAtService;
    @Autowired
    private IEquipmentTowerEffectService equipmentTowerEffectService;
    @Autowired
    private IEquipmentElevatorEffectService equipmentElevatorEffectService;
    @Autowired
    private IEquipmentDriverIdentService iEquipmentDriverIdentService;
    @Autowired
    private IAiDregCarService dregCarService;
    @Autowired
    private IEquipmentFormworkDataAtService equipmentFormworkDataAtService;
    @Autowired
    private IEquipmentPitDataAtBakService equipmentPitDataAtBakService;
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
     * @api {post} build/SiteDataUpload/data/async/result 查询环境监测数据是否上传成功
     * @apiDescription 查询环境监测数据是否上传成功
     * @apiVersion 1.0.0
     * @apiName dataAsyncResult
     * @apiGroup async
     *
     * @apiHeader {String} [Content-Type=application/json]
     * @apiParam {String} appid 必填 项目id，由平台提供
     * @apiParam {String} timestamp 必填 监控时间，格式：yyyyMMddhhmmss
     * @apiParam {String} sign 必填 签名，按照约定的签名算法生成
     * @apiParam {String} data 必填 推送时间，格式：yyyyMMddhhmmss
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
     * @apiError 50 异步查询结果：保存失败
     * @apiErrorExample {json} Error-Response:
     *      {
     *          "code": 50,
     *          "msg": "异步查询结果：保存失败",
     *          "data": null
     *      }
     *
     *
     * @param map
     * @return
     */
    @PostMapping("/data/async/result")
    @ApiOperation("查询环境监测数据是否上传成功")
    public Result selectResult(@RequestBody Map<String, Object> map) {
        return envPostLogService.selectResult(map);
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

    @PostMapping("/elevator/saveData")
    @ApiOperation("升降机实时数据上传接口")
    //@OperateLog(detail = "塔机实时数据上传接口", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result elevatorSaveData(@RequestBody  List<Map<String, Object>>  list) {
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


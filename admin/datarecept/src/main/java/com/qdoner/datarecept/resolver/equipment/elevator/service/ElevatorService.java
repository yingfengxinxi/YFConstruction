package com.qdoner.datarecept.resolver.equipment.elevator.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.core.common.Result;
import com.kakarote.core.config.MybatisPlusConfig;
import com.kakarote.core.feign.admin.entity.AdminFile;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.build.BuildService;
import com.kakarote.core.feign.build.entity.LabourInfocollection;
import com.kakarote.core.servlet.upload.FileServiceFactory;
import com.kakarote.core.servlet.upload.UploadEntity;
import com.kakarote.core.utils.Base64Utils;
import com.qdoner.datarecept.entity.PO.*;
import com.qdoner.datarecept.service.*;
import com.qdoner.datarecept.utils.FeelTheBase;
import com.qdoner.datarecept.utils.HexEcodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 升降机 业务层
 */
@Slf4j
@Component
public class ElevatorService {
    @Autowired
    private IBMonitorPointAlertService pointAlertService;

    @Autowired
    private IBEquipmentElevatorDataAtService elevatorDataAtService;

    @Autowired
    private IEquipmentOfflineService offlineService;

    //人员身份认证信息
    @Autowired
    private IBEquipmentDriverIdentService driverIdentService;

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private AdminFileService fileService;

    @Autowired
    private IEquipmentTypeService equipmentTypeService;

    @Autowired
    private BuildService buildService;
    //实时工况数据

    /**
     * 报文实例
     * A5 5A 10 00 5B8D88 12 0C 07 0F 13 18 00 00 00 00 02 A7 21 10 05 DC 00 05 DC 00 00 00 13 14 A9 1F CC 33 C3 3C
     *
     * @param body 信息体 12 0C 07 0F 13 18 00 00 00 00 02 A7 21 00 05 DC 00 05 DC 00 00 00 00 00
     *             时间6byte+实时起重量2byte +
     *             重量百分比1byte +
     *             实时人数1byte +
     *             实时高度2byte +
     *             高度百分比（1byte）+
     *             实时速度1byte（高6bit速度，低2bit方向: 0停止，1上下2下）+
     *             实时倾斜度1(2byte) +
     *             倾斜百分比1(1byte) +
     *             实时倾斜度2(2byte) +
     *             倾斜百分比2(1byte)+
     *             驾驶员身份认证结果1byte +
     *             门锁状态1byte（0bit前门1bit后门，数值1代表开启，0代表关闭。2bit门锁异常指示，0无异常1有异常）+
     *             系统状态2byte（
     *             0-1bit重量，
     *             2-3bit高度限位，
     *             4-5bit超速，
     *             6-7bit人数，
     *             8-9bit倾斜，数值0代表正常，数值1代表预警，数值2代表报警。
     *             10bit前门锁状态11bit后门锁状态：数字0正常,数值1异常）
     */
    public void realTimeData(String body, JSONObject asset, boolean isWarn) {
        //时间 6
        String datetimeByte = body.substring(0, 12);
        Date datetime = HexEcodeUtil.analysisDate(datetimeByte);
        log.info("时间6=>" + datetime);
        String weightByte = body.substring(12, 16);
        int weight = FeelTheBase.decodeHEX(weightByte);
        log.info("实时起重量2byte=>" + weight);
        String weightPerByte = body.substring(16, 18);
        int weightPer = FeelTheBase.decodeHEX(weightPerByte);
        log.info("重量百分比1byte=>" + weightPer);
        String pepleNumByte = body.substring(18, 20);
        int pepleNum = FeelTheBase.decodeHEX(pepleNumByte);
        log.info("实时人数1byte=>" + pepleNum);
        String heightByte = body.substring(20, 24);
        double height = FeelTheBase.decodeHEX(heightByte) * 0.1;
        log.info("实时高度2byte=>" + height);
        String heightPerByte = body.substring(24, 26);
        int heightPer = FeelTheBase.decodeHEX(heightPerByte);
        log.info("高度百分比（1byte）=>" + heightPer);
        String speedByte = body.substring(26, 28);
        log.info("实时速度1byte 16Hex=>" + speedByte);
        String speed = HexEcodeUtil.hexStrToBinaryStr(speedByte);
        log.info("实时速度1byte 2Hex=>" + speed);
        Double realSpeed = HexEcodeUtil.Biannary2Decimal(Integer.parseInt(speed.substring(0, 6))) * 0.1;
        log.info("实时速度6bit 10Hex=>" + realSpeed);
        int direction = HexEcodeUtil.Biannary2Decimal(Integer.parseInt(speed.substring(6, 8)));
        log.info("实时方向2bit 10Hex=>" + direction);
        String qxByte = body.substring(28, 32);
        double qx = parseAngle(FeelTheBase.decodeHEX(qxByte));
        log.info("X实时倾斜度(2byte) =>" + qx);
        String qxPerByte = body.substring(32, 34);
        int qxPer = FeelTheBase.decodeHEX(qxPerByte);
        log.info("X倾斜百分比1(1byte)=>" + qxPer);
        String qx1Byte = body.substring(34, 38);
        double qx1 = parseAngle(FeelTheBase.decodeHEX(qx1Byte));
        log.info("Y实时倾斜度2(2byte)=>" + qx1);
        String qx1PerByte = body.substring(38, 40);
        int qx1Per = FeelTheBase.decodeHEX(qx1PerByte);
        log.info("Y倾斜百分比2(1byte)=>" + qx1Per);
        String driverByte = body.substring(40, 42);
        int driver = FeelTheBase.decodeHEX(driverByte);
        log.info("驾驶员身份认证结果1byte=>" + driver);
        String lock = body.substring(42, 44);
        log.info("门锁状态1byte 16Hex=>" + lock);
        String lockStatus2 = HexEcodeUtil.hexStrToBinaryStr(lock);
        log.info("门锁状态2byte 2Hex=>" + lockStatus2);
        String lockStatusReverse = reverse(lockStatus2);
        String beLockStatus = reverse(lockStatusReverse.substring(0, 1));
        int beLock = HexEcodeUtil.Biannary2Decimal(Integer.parseInt(beLockStatus));
        log.info("前门门锁状态 10Hex=>" + beLock);
        String afLockStatus = reverse(lockStatusReverse.substring(1, 2));
        int afLock = HexEcodeUtil.Biannary2Decimal(Integer.parseInt(afLockStatus));
        log.info("后门门锁状态 10Hex=>" + afLock);
        //实时
        String systemByte = body.substring(44, 48);
        log.info("系统状态2byte 16Hex=>" + systemByte);
        String systemStatus = HexEcodeUtil.hexStrToBinaryStr(systemByte);
        log.info("系统状态2byte 2Hex=>" + systemStatus);
        /**
         * 0-1bit重量，
         * 2-3bit高度限位，
         * 4-5bit超速，
         * 6-7bit人数，
         * 8-9bit倾斜，数值0代表正常，数值1代表预警，数值2代表报警。
         * 10bit前门锁状态、11bit后门锁状态：数字0正常,数值1异常
         */
        String statusReverse = reverse(systemStatus);
        String weightWarnStr = reverse(statusReverse.substring(0, 2));
        int weightWarn = HexEcodeUtil.Biannary2Decimal(Integer.parseInt(weightWarnStr));
        log.info("重量预警 10Hex=>" + weightWarn);
        String heightWarnStr = reverse(statusReverse.substring(2, 4));
        int heightWarn = HexEcodeUtil.Biannary2Decimal(Integer.parseInt(heightWarnStr));
        log.info("高度限位 10Hex=>" + heightWarn);
        String speedWarnStr = reverse(statusReverse.substring(4, 6));
        int speedWarn = HexEcodeUtil.Biannary2Decimal(Integer.parseInt(speedWarnStr));
        log.info("超速 10Hex=>" + speedWarn);
        String peopleWarnStr = reverse(statusReverse.substring(6, 8));
        int peopleWarn = HexEcodeUtil.Biannary2Decimal(Integer.parseInt(peopleWarnStr));
        log.info("人数 10Hex=>" + peopleWarn);
        String alarmWarnStr = reverse(statusReverse.substring(8, 10));
        int alarmWarn = HexEcodeUtil.Biannary2Decimal(Integer.parseInt(alarmWarnStr));
        log.info("倾斜 10Hex=>" + alarmWarn);
        String beDoorWarnStr = reverse(statusReverse.substring(10, 11));
        int beDoorWarn = HexEcodeUtil.Biannary2Decimal(Integer.parseInt(beDoorWarnStr));
        log.info("前门锁状态 10Hex=>" + beDoorWarn);
        String afDoorWarnStr = reverse(statusReverse.substring(11, 12));
        int afDoorWarn = HexEcodeUtil.Biannary2Decimal(Integer.parseInt(afDoorWarnStr));
        log.info("后门锁状态 10Hex=>" + afDoorWarn);
        try {
            //判断是否需要添加上线数据
            List<Map> lineList =  offlineService.getOfflineElevator(asset.getInteger("assetId"));
            if(lineList.size() == 0){
                EquipmentOffline equipmentOffline = new EquipmentOffline();
                equipmentOffline.setStatus(1);
                equipmentOffline.setEquipmentId(asset.getInteger("assetId"));
                equipmentOffline.setMonitorTime(new Date());
                equipmentOffline.setTenantId(asset.getInteger("tenantId"));
                equipmentOffline.setProjectId(asset.getInteger("projectId"));
                offlineService.save(equipmentOffline);
            }
            BEquipmentElevatorDataAt dataAt = new BEquipmentElevatorDataAt();
            //基础信息
            dataAt.setEquipmentId(asset.getInteger("assetId"));
            dataAt.setTenantId(asset.getInteger("tenantId"));
            dataAt.setProjectId(asset.getInteger("projectId"));
            dataAt.setWindSpeedAlarm("0");
            dataAt.setRoofFlushingAlarm("0");
            dataAt.setUpperLimitAlarm("0");
            dataAt.setLowerLimitAlarm("0");
            //
            dataAt.setMonitorTime(datetime);
            dataAt.setBeforeLock(String.valueOf(beLock));
            dataAt.setAfterLock(String.valueOf(afLock));
            dataAt.setXAngle(String.valueOf(qx));
            dataAt.setXAngleRatio(String.valueOf(qxPer));
            dataAt.setYAngle(String.valueOf(qx1));
            dataAt.setYAngleRatio(String.valueOf(qx1Per));
            dataAt.setHeight(height);//高度
            dataAt.setSpeed(String.valueOf(realSpeed));
            dataAt.setWeight(String.valueOf(weight));//载重
            dataAt.setWeightPercent(weightPer + 0.0);
            if (0 == direction) {
                dataAt.setLiftingDirection("1");//停
            }
            if (1 == direction) {
                dataAt.setLiftingDirection("2");//上
            }
            if (2 == direction) {
                dataAt.setLiftingDirection("3");//下
            }
            if (0 == driver) {//驾驶员未认证
                dataAt.setWarningState("1");
            } else {
                dataAt.setWarningState("0");
            }
            dataAt.setNumberOfPeople(pepleNum);
            //相关报警
            List<BMonitorPointAlert> warnList = new ArrayList<>();
            //重量
            if (0 == weightWarn) {//正常
                dataAt.setLoadAlarm("0");
                handleWarn("载重预警", String.valueOf(weight), asset);
            }
            if (1 == weightWarn) {//预警
                dataAt.setLoadAlarm("2");
                warnList.add(createWarnEntity("0", "载重预警", String.valueOf(weight), asset));
            }
            if (2 == weightWarn) {//报警
                dataAt.setLoadAlarm("1");
                warnList.add(createWarnEntity("1", "载重预警", String.valueOf(weight), asset));
            }
            //高度限位
            if (0 == heightWarn) {
                dataAt.setAltitudeAlarm("0");
                handleWarn("高度预警", String.valueOf(height), asset);
            }
            if (1 == heightWarn) {
                dataAt.setAltitudeAlarm("2");
                warnList.add(createWarnEntity("0", "高度预警", String.valueOf(height), asset));
            }
            if (2 == heightWarn) {
                dataAt.setAltitudeAlarm("1");
                warnList.add(createWarnEntity("1", "高度预警", String.valueOf(height), asset));
            }
            //超速
            if (0 == speedWarn) {
                dataAt.setSpeedWarning("0");
                handleWarn("速度预警", String.valueOf(realSpeed), asset);
            }
            if (1 == speedWarn) {
                dataAt.setSpeedWarning("2");
                warnList.add(createWarnEntity("0", "速度预警", String.valueOf(realSpeed), asset));
            }
            if (2 == speedWarn) {
                dataAt.setSpeedWarning("1");
                warnList.add(createWarnEntity("1", "速度预警", String.valueOf(realSpeed), asset));
            }
            //人数
            if (0 == peopleWarn) {
                dataAt.setNumberAlarm("0");
                handleWarn("人数预警", String.valueOf(pepleNum), asset);
            }
            if (1 == peopleWarn) {
                dataAt.setNumberAlarm("2");
                warnList.add(createWarnEntity("0", "人数预警", String.valueOf(pepleNum), asset));
            }
            if (2 == peopleWarn) {
                dataAt.setNumberAlarm("1");
                warnList.add(createWarnEntity("1", "人数预警", String.valueOf(pepleNum), asset));
            }
            //倾斜
            if (0 == alarmWarn) {
                dataAt.setTiltAlarm("0");
                handleWarn("倾斜预警", "X" + String.valueOf(qx) + "/Y" + String.valueOf(qx1), asset);
            }
            if (1 == alarmWarn) {
                dataAt.setTiltAlarm("2");
                warnList.add(createWarnEntity("0", "倾斜预警", "X" + String.valueOf(qx) + "/Y" + String.valueOf(qx1), asset));
            }
            if (2 == alarmWarn) {
                dataAt.setTiltAlarm("1");
                warnList.add(createWarnEntity("1", "倾斜预警", "X" + String.valueOf(qx) + "/Y" + String.valueOf(qx1), asset));
            }
            //前门锁状态
            if (0 == beDoorWarn) {
                dataAt.setBedoorWarn("0");
                handleWarn("前门锁预警", String.valueOf(beLock), asset);
            }
            if (1 == beDoorWarn) {
                dataAt.setBedoorWarn("2");
                warnList.add(createWarnEntity("0", "前门锁预警", String.valueOf(beLock), asset));
            }
            if (2 == beDoorWarn) {
                dataAt.setBedoorWarn("1");
                warnList.add(createWarnEntity("1", "前门锁预警", String.valueOf(beLock), asset));
            }
            //后门锁状态
            if (0 == weightWarn) {
                dataAt.setAfdoorWarn("0");
                handleWarn("后门锁预警", String.valueOf(afLock), asset);
            }
            if (1 == weightWarn) {
                dataAt.setAfdoorWarn("2");
                warnList.add(createWarnEntity("0", "后门锁预警", String.valueOf(afLock), asset));
            }
            if (2 == weightWarn) {
                dataAt.setAfdoorWarn("1");
                warnList.add(createWarnEntity("1", "后门锁预警", String.valueOf(afLock), asset));
            }
            //入库 比较变动
            List<BEquipmentElevatorDataAt> towerDataAts = elevatorDataAtService.lambdaQuery()
                    .eq(BEquipmentElevatorDataAt::getEquipmentId, asset.getInteger("assetId"))
                    .eq(BEquipmentElevatorDataAt::getTenantId, asset.getInteger("tenantId"))
                    .eq(BEquipmentElevatorDataAt::getProjectId, asset.getInteger("projectId"))
                    .orderByDesc(BEquipmentElevatorDataAt::getId).list();
            if (towerDataAts.size() > 0) {
                BEquipmentElevatorDataAt oldDataAt = towerDataAts.get(0);
                if (!oldDataAt.toString().equals(dataAt.toString())) {
                    elevatorDataAtService.save(dataAt);
                }
            } else {
                elevatorDataAtService.save(dataAt);
            }
            //报警
            if(warnList.size() > 0){
                pointAlertService.saveBatch(warnList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建报警实例
     *
     * @param type    报警类型
     * @param content 报警内容
     * @param data    报警值
     * @param asset   设备信息
     * @return
     */
    private BMonitorPointAlert createWarnEntity(String type, String content, String data, JSONObject asset) {
        BMonitorPointAlert pointAlert = new BMonitorPointAlert();
        pointAlert.setMonitorData(data);
        pointAlert.setContent(content);
        pointAlert.setAlertType(type);
        //
        pointAlert.setEquipmentId(asset.getInteger("assetId"));
        pointAlert.setTenantId(asset.getInteger("tenantId"));
        pointAlert.setProjectId(asset.getInteger("projectId"));
        pointAlert.setCreateTime(new Date());
        return pointAlert;
    }

    /**
     * 处理报警信息
     *
     * @param content 报警内容
     * @param data    恢复值
     * @param asset   设备信息
     */
    private void handleWarn(String content, String data, JSONObject asset) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("equipment_id", asset.getInteger("assetId"));
        queryWrapper.eq("content", content);
        queryWrapper.eq("tenant_id", asset.getInteger("tenantId"));
        queryWrapper.eq("project_id", asset.getInteger("projectId"));
        queryWrapper.isNull("handle_time");
        List<BMonitorPointAlert> list = pointAlertService.list(queryWrapper);
        for (BMonitorPointAlert monitorPointAlert : list) {
            DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            monitorPointAlert.setHandleTime(new Date());
            monitorPointAlert.setRecoveryData(data);
            monitorPointAlert.setHandleConclu("监测值已恢复");
            monitorPointAlert.setHandleState("1");
            monitorPointAlert.setHandleMeasures("报警解除");
        }
        if (list.size() > 0) {
            pointAlertService.updateBatchById(list);
        }
    }

    /**
     * 字符串反向
     *
     * @param str
     * @return
     */
    private String reverse(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * 处理实时数据倾斜度
     *
     * @return
     */
    private double parseAngle(int angle) {
        int difference = angle - 1500;
        if (difference == 0) {
            return 0.00;
        }
        double abs = Math.abs(difference) * 0.01;
        return abs;
    }



    /**
     * A5 5A 12 00 5B8D88 01 D6 EC B8 D5 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 31 00 00 00 00 00 00 00 47 33 32 30 33 38 32 31 39 38 33 30 35 30 32 38 31 33 38 E0 43 CC 33 C3 3C
     * （姓名）
     * （身份证320382198305028138）
     * 识别结果状态值(1byte) +
     * 用户名(32字节，字符型)+
     * 用户id(8字节ASCII码) +
     * 识别分数（1字节，准确百分比） +
     * 身份证号码(18字节ASCII码)
     *
     * @param body
     * @param asset
     */
    public void staffInfo(String body, JSONObject asset) {
        String status = body.substring(0, 2);
        int statusInt = FeelTheBase.decodeHEX(status);
        log.info("识别结果状态值(1byte)" + statusInt);
        String userName = HexEcodeUtil.toStringHex2(body.substring(2, 66));
        log.info("用户名(32字节，字符型)" + userName);
        String userId = HexEcodeUtil.toStringHex2(body.substring(66, 82));
        log.info("用户id(8字节ASCII码)" + userId);
        String score = body.substring(82, 84);
        int scoreInt = FeelTheBase.decodeHEX(score);
        log.info("识别分数（1字节，准确百分比）" + scoreInt);
        String idCard = HexEcodeUtil.toStringHex2(body.substring(84, 120));
        log.info("身份证号码(18字节ASCII码)" + idCard);

        // 判断设备类型
        EquipmentType equipmentType = equipmentTypeService.getById(asset.getIntValue("typeId"));
        String deviceType = null;
        if ("2".equals(equipmentType.getMonitorType())) { // 塔机
            deviceType = "1";
        } else if ("3".equals(equipmentType.getMonitorType())) { // 升降机
            deviceType = "2";
        }


        //入库
        try {
            //组装数据
            BEquipmentDriverIdent driverIdent = new BEquipmentDriverIdent();
            //基础信息
            driverIdent.setEquCode(asset.getString("platformEqCode"));
            driverIdent.setTenantId(asset.getInteger("tenantId"));
            driverIdent.setProjectId(asset.getInteger("projectId"));
            driverIdent.setCreateTime(new Date());
            driverIdent.setDeviceType(deviceType);//塔机
            driverIdent.setStartingDate(new Date());//上岗时间
            driverIdent.setIdCard(idCard);
            driverIdent.setName(userId);
            driverIdentService.save(driverIdent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //工作循环
    @Autowired
    private IBEquipmentElevatorEffectService elevatorEffectService;

    /**
     * 报文实例：
     * A5 5A 13 00 5B8D88 12 0C 07 0F 19 05 12 0C 07 0F 18 2E 12 0C 07 0F 18 32 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 02 AF 02 DE 00 2F 02 0F 00 00 00 00 00 00 00 DC 5D CC 33 C3 3C
     *
     * @param body 当前时间6byte+
     *             工作循环开始时间6byte+
     *             工作循环终止时间6byte+
     *             驾驶员姓名32byte+
     *             驾驶员身份证号18byte+
     *             <p>
     *             本次载重2byte +
     *             本次载重百分比1byte +
     *             本次起升起点高度2byte+
     *             本次起升终点高度2byte+
     *             本次起升的行程高度2byte+
     *             本次起升方向1byte+
     *             本次起升平均速度1byte+
     *             本次起升最大X向倾斜度2byte+
     *             本次起升最大Y向倾斜度2byte+
     *             本次装载人数1byte+
     *             本次起升警报状态2byte
     */
    public void workCycle(String body, JSONObject asset) {
        //时间 6
        String datetimeByte = body.substring(0, 12);
        Date datetime = HexEcodeUtil.analysisDate(datetimeByte);
        log.info("时间6=>" + datetime);
        String startDatetimeByte = body.substring(12, 24);
        Date startDatetime = HexEcodeUtil.analysisDate(startDatetimeByte);
        log.info("开始时间6=>" + startDatetime);
        String endDatetimeByte = body.substring(24, 36);
        Date endDatetime = HexEcodeUtil.analysisDate(endDatetimeByte);
        log.info("结束时间6=>" + endDatetime);
        String name = body.substring(36, 100);
        log.info("驾驶员姓名32byte=>" + name);
        String idcard = body.substring(100, 136);
        log.info("驾驶员身份证号18byte=>" + idcard);
        String weight = body.substring(136, 140);
        int weightInt = FeelTheBase.decodeHEX(weight);
        log.info("本次载重2byte=>" + weightInt);
        String weightPer = body.substring(140, 142);
        int weightPerInt = FeelTheBase.decodeHEX(weightPer);
        log.info("本次载重百分比1byte=>" + weightInt);
        String topHeight = body.substring(142, 146);
        Double topHeightInt = FeelTheBase.decodeHEX(topHeight) * 0.1;
        log.info("本次起升起点高度2byte=>" + topHeightInt);
        String endHeight = body.substring(146, 150);
        Double endHeightInt = FeelTheBase.decodeHEX(endHeight) * 0.1;
        log.info("本次起升终点高度2byte=>" + endHeightInt);
        String xcHeight = body.substring(150, 154);
        Double xcHeightInt = FeelTheBase.decodeHEX(xcHeight) * 0.1;
        log.info("本次起升的行程高度2byte=>" + xcHeightInt);
        String fx = body.substring(154, 156);
        int fxInt = FeelTheBase.decodeHEX(fx);
        log.info("本次起升方向1byte=>" + fxInt);
        String pjsd = body.substring(156, 158);
        Double pjsdInt = FeelTheBase.decodeHEX(pjsd) * 0.1;
        log.info("本次起升平均速度1byte=>" + pjsdInt);
        String maxX = body.substring(158, 162);
        Double maxXInt = FeelTheBase.decodeHEX(maxX) * 0.01;
        log.info("本次起升最大X向倾斜度2byte=>" + maxXInt);
        String maxY = body.substring(162, 166);
        Double maxYInt = FeelTheBase.decodeHEX(maxY) * 0.01;
        log.info("本次起升最大Y向倾斜度2byte=>" + maxYInt);
        String pNum = body.substring(166, 168);
        int pNumInt = FeelTheBase.decodeHEX(pNum);
        log.info("本次装载人数1byte=>" + pNumInt);
        String warn = body.substring(168, 172);
        int warnInt = FeelTheBase.decodeHEX(warn);
        log.info("本次起升警报状态2byte=>" + warnInt);
        try {
            BEquipmentElevatorEffect elevatorEffect = new BEquipmentElevatorEffect();
            //基础信息
            elevatorEffect.setAssetId(asset.getInteger("assetId"));
            elevatorEffect.setTenantId(asset.getInteger("tenantId"));
            elevatorEffect.setProjectId(asset.getInteger("projectId"));
            elevatorEffect.setCreateTime(new Date());
            //
            elevatorEffect.setStartTime(startDatetime);
            elevatorEffect.setEndTime(endDatetime);
            elevatorEffect.setWeight(weightInt + 0.0);
            elevatorEffect.setPercentRate(weightPerInt + 0.0);
            elevatorEffect.setNumberOfPeople(pNumInt);
            elevatorEffect.setSpeed(pjsdInt);
            elevatorEffect.setEarlyAlarm(0);//默认0
            elevatorEffect.setAlarm(warnInt);//报警码

            elevatorEffect.setAngle(maxXInt);
            elevatorEffect.setAngleY(maxYInt);
            elevatorEffect.setStartHeight(topHeightInt);
            elevatorEffect.setEndHeight(endHeightInt);
            elevatorEffect.setHeight(xcHeightInt);
            if (2 == fxInt) {//上
                elevatorEffect.setDirection("上行(2)");
            }
            if (1 == fxInt) {
                elevatorEffect.setDirection("下行(1)");
            }
            elevatorEffectService.save(elevatorEffect);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
    * @Description:图片信息
    * @author shz
    * @date 2022-9-30 16:26
    * @param 
    * @return 
    */
    public void staffImg(String body, JSONObject asset) {
        log.info("包号低位：" + body.substring(0, 2));
        log.info("包号高位：" + body.substring(2, 4));
        log.info("包号：" + FeelTheBase.decodeHEX(body.substring(2, 4)+body.substring(0, 2)));
        log.info("包长低位：" + body.substring(4, 6));
        log.info("包长高位：" + body.substring(6, 8));
        log.info("包长：" + FeelTheBase.decodeHEX(body.substring(6, 8)+body.substring(4, 6)));
        log.info("图像数据：" + body.substring(8, 1020));
        log.info("和校验：" + body.substring(1020, 1022));
        log.info("总包数16：" + body.substring(1022, 1024));
        log.info("总包数10：" + FeelTheBase.decodeHEX(body.substring(1022, 1024)));
        if(FeelTheBase.decodeHEX(body.substring(2, 4)+body.substring(0, 2)) == FeelTheBase.decodeHEX(body.substring(1022, 1024))){
            String ImgStr = "";
//            for(int i = 1;i<FeelTheBase.decodeHEX(body.substring(1022, 1024));i++){
//                Object redisStr =  redisTemplate.opsForValue().get("TCP_IMG_" + i);
//                if(redisStr == null||"".equals(redisStr)){
//                    return;
//                }
//                ImgStr+=redisStr;
//            }
            ImgStr += body.substring(8, 1020);
            log.info("总图像数据：" + ImgStr);
            String score = HexEcodeUtil.toStringHex2(ImgStr);
            log.info("baes64：" + score);
            String batchId = UUID.randomUUID().toString().trim().replaceAll("-", "");
            UploadEntity entity = new UploadEntity(MybatisPlusConfig.getNextId() + "", "", 0l, batchId,"0");
            entity = FileServiceFactory.build().uploadFile(Base64Utils.base64ToInputStream(getB64()), entity);
            AdminFile adminFile = new AdminFile();
            adminFile.setFileId(Long.valueOf(entity.getFileId()));
            adminFile.setName(entity.getName());
            adminFile.setSize(entity.getSize());
            adminFile.setPath(entity.getPath());
            adminFile.setBatchId(batchId);
            adminFile.setFileType("img");
            adminFile.setType(entity.getType());
            adminFile.setIsPublic(Integer.valueOf(entity.getIsPublic()));
            adminFile.setTenantId(asset.getString("tenantId"));
            fileService.add(adminFile);
            String assetCode = asset.getString("assetCode");
            QueryWrapper<BEquipmentDriverIdent> wrapper = new QueryWrapper();
            wrapper.eq("equ_code",assetCode).eq("tenant_id",asset.getString("tenantId")).eq("project_id",asset.getString("projectId")).orderByDesc("create_time").last("limit 1");
            BEquipmentDriverIdent driverIdent = driverIdentService.getOne(wrapper);
            LabourInfocollection infocollection = new LabourInfocollection();
            infocollection.setIdCard(driverIdent.getIdCard());
            infocollection.setImgCardUrl(entity.getPath());
            infocollection.setBatchId(batchId);
            infocollection.setTenantId(asset.getInteger("tenantId"));
            infocollection.setProjectId(asset.getInteger("projectId"));
            buildService.updateByIdCard(infocollection);
        }else {
            String id = "TCP_IMG_" + FeelTheBase.decodeHEX(body.substring(2, 4)+body.substring(0, 2));
            redisTemplate.opsForValue().set(id,body.substring(8, 1020),60, TimeUnit.SECONDS);
        }
    }

    public  String getB64(){
        String score = "";
        try {
            Path path = Paths.get("D:/aaa.txt");
            byte[] data = Files.readAllBytes(path);
            String str = new String(data, "utf-8");
            String body = str.replaceAll(" ", "");
            body = body.substring(14, body.length() - 1);//去头 帧头2、帧类型1、塔吊编号1、设备编号3
            body = body.substring(0, body.length() - 11);
            String userId = HexEcodeUtil.toStringHex2(body.substring(0, 36));
            score = HexEcodeUtil.toStringHex2(body.substring(36, body.length()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return score;
    }

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("D:/aaa.txt");
        byte[] data = Files.readAllBytes(path);
        String str = new String(data, "utf-8");
        String body = str.replaceAll(" ", "");
        body = body.substring(14, body.length() - 1);//去头 帧头2、帧类型1、塔吊编号1、设备编号3
        body = body.substring(0, body.length() - 11);
        String userId = HexEcodeUtil.toStringHex2(body.substring(0, 36));
        log.info("用户身份证(8字节ASCII码)" + userId);
        String score = HexEcodeUtil.toStringHex2(body.substring(36, body.length()));
        log.info("baes64" + score);
        Base64Utils.base64ToFile("D:/",score,"aaaaaa.jpg");
        System.out.println();
    }
}

package com.qdoner.datarecept.resolver.equipment.tower.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.qdoner.datarecept.entity.PO.*;
import com.qdoner.datarecept.service.*;
import com.qdoner.datarecept.utils.CRCUtils;
import com.qdoner.datarecept.utils.HexEcodeUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AnalysisService {

    @Autowired
    private IBEquipmentAssetsService equipmentAssetsService;

    @Autowired
    private IBEquipmentTowerService towerService;

    @Autowired
    private IBEquipmentTowerEffectService towerEffectService;

    @Autowired
    private IBEquipmentDriverIdentService driverIdentService;

    @Autowired
    private IBEquipmentTowerDataAtService dataAtService;

    @Autowired
    private IBMonitorPointAlertService pointAlertService;

    @Autowired
    private IEquipmentOfflineService offlineService;

    public byte[] register(byte[] bytes) {
        String str = HexEcodeUtil.ByteArrayToHexStr(bytes);
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();
        String time = ft.format(date);
        String huifu = str.substring(0, 4) + "61" + str.substring(6, 14) + "01" + time + "0F";
        String hf = huifu + CRCUtils.makeChecksum(huifu) + "0FCC33C33C";
        log.info(" 发送数据1: <<<<<<" + huifu);
        return HexEcodeUtil.hexItr2Arr(hf);
    }

    public byte[] register2(byte[] bytes) {
        String str = HexEcodeUtil.ByteArrayToHexStr(bytes);
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();
        String time = ft.format(date);
        String huifu = str.substring(0, 4) + "02" + str.substring(6, 14) + "01" + time + "0F";
        String hf = huifu + CRCUtils.makeChecksum(huifu) + "0FCC33C33C";
        log.info(" 发送数据1: <<<<<<" + huifu);
        return HexEcodeUtil.hexItr2Arr(hf);
    }

    /**
     * 实时数据信息
     *
     * @param bytes
     */
    public void realTimeData(byte[] bytes) {
        byte[] bytes1 = Arrays.copyOfRange(bytes, 0, 2);
        String str1 = HexEcodeUtil.ByteArrayToHexStr(bytes1);
        log.info("起始符：" + str1);
        byte[] bytes2 = Arrays.copyOfRange(bytes, 2, 3);
        String str2 = HexEcodeUtil.ByteArrayToHexStr(bytes2);
        log.info("命令字：" + str2);
        byte[] bytes3 = Arrays.copyOfRange(bytes, 3, 4);
        String str3 = HexEcodeUtil.ByteArrayToHexStr(bytes3);
        log.info("塔机编号：" + str3);
        byte[] bytes4 = Arrays.copyOfRange(bytes, 4, 7);
        String str4 = HexEcodeUtil.ByteArrayToHexStr(bytes4);
        log.info("主机编号：" + str4);
        byte[] bytes5 = Arrays.copyOfRange(bytes, 7, 8);
        String str5 = HexEcodeUtil.ByteArrayToHexStr(bytes5);
        log.info("版本号：" + str5);
        byte[] bytes6 = Arrays.copyOfRange(bytes, 8, 14);
        log.info("时间：" + HexEcodeUtil.ByteArrayToHexStr(bytes6));
        log.info("时间：" + HexEcodeUtil.hexEncode(bytes6));
        byte[] bytes7 = Arrays.copyOfRange(bytes, 14, 15);
        String str7 = HexEcodeUtil.ByteArrayToHexStr(bytes7);
        log.info("厂家及设备类型：" + str7);
        byte[] bytes8 = Arrays.copyOfRange(bytes, 15, 17);
        log.info("高度数据16：" + HexEcodeUtil.ByteArrayToHexStr(bytes8));
        log.info("高度数据10：" + HexEcodeUtil.hexEncode(bytes8));
        byte[] bytes9 = Arrays.copyOfRange(bytes, 17, 19);
        log.info("幅度数据16：" + HexEcodeUtil.ByteArrayToHexStr(bytes9));
        log.info("幅度数据10：" + HexEcodeUtil.hexEncode(bytes9));
        byte[] bytes10 = Arrays.copyOfRange(bytes, 19, 21);
        log.info("回转16：" + HexEcodeUtil.ByteArrayToHexStr(bytes10));
        log.info("回转10：" + HexEcodeUtil.hexEncode(bytes10));
        byte[] bytes11 = Arrays.copyOfRange(bytes, 21, 23);
        log.info("起重量数据16：" + HexEcodeUtil.ByteArrayToHexStr(bytes11));
        log.info("起重量数据10：" + HexEcodeUtil.hexEncode(bytes11));
        byte[] bytes12 = Arrays.copyOfRange(bytes, 23, 25);
        log.info("风速数据16：" + HexEcodeUtil.ByteArrayToHexStr(bytes12));
        log.info("风速数据10：" + HexEcodeUtil.hexEncode(bytes12));
        byte[] bytes13 = Arrays.copyOfRange(bytes, 25, 27);
        log.info("倾角数据16：" + HexEcodeUtil.ByteArrayToHexStr(bytes13));
        log.info("倾角数据10：" + HexEcodeUtil.hexEncode(bytes13));
        //无对应
        byte[] bytes14 = Arrays.copyOfRange(bytes, 27, 28);
        log.info("重量百分比16：" + HexEcodeUtil.ByteArrayToHexStr(bytes14));
        log.info("重量百分比10：" + HexEcodeUtil.hexEncode(bytes14));
        byte[] bytes15 = Arrays.copyOfRange(bytes, 28, 29);
        log.info("力矩百分比16：" + HexEcodeUtil.ByteArrayToHexStr(bytes15));
        log.info("力矩百分比10：" + HexEcodeUtil.hexEncode(bytes15));
        //无对应
        byte[] bytes16 = Arrays.copyOfRange(bytes, 29, 30);
        log.info("风速百分比16：" + HexEcodeUtil.ByteArrayToHexStr(bytes16));
        log.info("风速百分比10：" + HexEcodeUtil.hexEncode(bytes16));
        //无对应
        byte[] bytes17 = Arrays.copyOfRange(bytes, 30, 31);
        log.info("倾斜百分比16：" + HexEcodeUtil.ByteArrayToHexStr(bytes17));
        log.info("倾斜百分比10：" + HexEcodeUtil.hexEncode(bytes17));
        byte[] bytes18 = Arrays.copyOfRange(bytes, 31, 32);
        log.info("警报原因16：" + HexEcodeUtil.ByteArrayToHexStr(bytes18));
        log.info("警报原因10：" + HexEcodeUtil.hexEncode(bytes18));
        //无对应
        byte[] bytes19 = Arrays.copyOfRange(bytes, 32, 33);
        log.info("制动状态：" + HexEcodeUtil.toBinary(bytes19));
        try {
            //根据编号获取塔机
            byte[] towCodeByte = Arrays.copyOfRange(bytes, 4, 7);
            String towCode = HexEcodeUtil.ByteArrayToHexStr(towCodeByte);
            BEquipmentAssets assets = getAsset(towCode);
            if (assets == null) {
                return;
            }
            //判断是否需要添加上线数据
            List<Map> lineList =  offlineService.getOfflineTower(assets.getAssetId());
            if(lineList.size() == 0){
                EquipmentOffline equipmentOffline = new EquipmentOffline();
                equipmentOffline.setStatus(1);
                equipmentOffline.setEquipmentId(assets.getAssetId());
                equipmentOffline.setMonitorTime(new Date());
                equipmentOffline.setTenantId(assets.getTenantId());
                equipmentOffline.setProjectId(assets.getProjectId());
                offlineService.save(equipmentOffline);
                assets.setOfflineDate(new Date());
                assets.setOfflineStatus("1");
                equipmentAssetsService.updateById(assets);
            }
            BEquipmentTowerDataAt towerDataAt = new BEquipmentTowerDataAt();
            //基础信息
            towerDataAt.setEquipmentId(assets.getAssetId());
            towerDataAt.setTenantId(assets.getTenantId());
            towerDataAt.setProjectId(assets.getProjectId());
            //组装数据
            byte[] protocolVer = Arrays.copyOfRange(bytes, 7, 8);//版本号
            towerDataAt.setProtocolVer(HexEcodeUtil.ByteArrayToHexStr(protocolVer));
            byte[] timeByte = Arrays.copyOfRange(bytes, 8, 14);
            Date mDateTime = HexEcodeUtil.analysisDate(HexEcodeUtil.ByteArrayToHexStr(timeByte));
            towerDataAt.setMonitorTime(mDateTime);
            byte[] heightByte = Arrays.copyOfRange(bytes, 15, 17);//高度
            BigDecimal heightBig = new BigDecimal(Integer.parseInt(HexEcodeUtil.hexEncode(heightByte)) * 0.1);
            Double height = heightBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            towerDataAt.setHeight(height.toString());
            byte[] amplitudeByte = Arrays.copyOfRange(bytes, 17, 19);//幅度
            BigDecimal amplitudeBig = new BigDecimal(Integer.parseInt(HexEcodeUtil.hexEncode(amplitudeByte)) * 0.1);
            Double amplitude = amplitudeBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            towerDataAt.setAmplitude(amplitude.toString());
            byte[] rotationByte = Arrays.copyOfRange(bytes, 19, 21);//回转 转角
            BigDecimal rotationBig = new BigDecimal(Integer.parseInt(HexEcodeUtil.hexEncode(rotationByte)) * 0.1);
            Double rotation = rotationBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            towerDataAt.setRotation(rotation.toString());
            byte[] weightByte = Arrays.copyOfRange(bytes, 21, 23);//吊重
            BigDecimal weightBig = new BigDecimal(Integer.parseInt(HexEcodeUtil.hexEncode(weightByte)) * 0.001);
            Double weight = weightBig.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            towerDataAt.setWeight(weight.toString());
            byte[] windspeedByte = Arrays.copyOfRange(bytes, 23, 25);//风速
            BigDecimal windspeedBig = new BigDecimal(Integer.parseInt(HexEcodeUtil.hexEncode(windspeedByte)) * 0.1);
            Double windspeed = windspeedBig.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            towerDataAt.setWindspeed(windspeed.toString());
            byte[] obliguityByte = Arrays.copyOfRange(bytes, 25, 27);//倾角
            BigDecimal obliguityBig = new BigDecimal(Integer.parseInt(HexEcodeUtil.hexEncode(obliguityByte)) * 0.1);
            Double obliguity = obliguityBig.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            towerDataAt.setObliguity(Float.valueOf(obliguity.toString()));
            byte[] weightPercentageByte = Arrays.copyOfRange(bytes, 27, 28);//吊重百分比
            towerDataAt.setWeightPercentage(Float.valueOf(HexEcodeUtil.hexEncode(weightPercentageByte)));
            byte[] momentByte = Arrays.copyOfRange(bytes, 28, 29);//力矩百分比
            towerDataAt.setMoment(Float.valueOf(HexEcodeUtil.hexEncode(momentByte)));
            byte[] windPercentage = Arrays.copyOfRange(bytes, 29, 30);//风速百分比
            towerDataAt.setWindPercentage(Float.valueOf(HexEcodeUtil.hexEncode(windPercentage)));
            byte[] obliguityPercentage = Arrays.copyOfRange(bytes, 30, 31);//倾角百分比
            towerDataAt.setObliguityPercentage(Float.valueOf(HexEcodeUtil.hexEncode(obliguityPercentage)));
            String brakingStatus = HexEcodeUtil.toBinary(bytes19);
            towerDataAt.setBrakingStatus(brakingStatus);
            byte[] warnByte = Arrays.copyOfRange(bytes, 31, 32);//警报原因


            //todo 报警处理 == 07.13未处理
            String warn = HexEcodeUtil.hexEncode(warnByte);
            BMonitorPointAlert pointAlert = new BMonitorPointAlert();
            pointAlert.setEquipmentId(assets.getAssetId());
            pointAlert.setTenantId(assets.getTenantId());
            pointAlert.setProjectId(assets.getProjectId());
            pointAlert = copyPastry(pointAlert);
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("equipment_id",assets.getAssetId());
            queryWrapper.isNull("handle_time");
            queryWrapper.eq("project_id",assets.getProjectId());
            queryWrapper.eq("tenant_id",assets.getTenantId());
            List<BMonitorPointAlert> list = pointAlertService.list(queryWrapper);
            switch (warn){
                case "12":
                    towerDataAt.setWeightAlarm("1");
                    pointAlert.setMonitorData(weight+"T");
                    pointAlert.setContent("吊重报警");
                    pointAlert.setAlertType("1");
                    break;
                case "13":
                    towerDataAt.setMomentAlarm("1");
                    pointAlert.setMonitorData(Float.valueOf(HexEcodeUtil.hexEncode(momentByte))+"T·M");
                    pointAlert.setContent("力矩报警");
                    pointAlert.setAlertType("1");
                    break;
                case "14":
                    towerDataAt.setObliguityAlarm("1");
                    pointAlert.setMonitorData(obliguity+"度");
                    pointAlert.setContent("倾斜报警");
                    pointAlert.setAlertType("1");
                    break;
                case "15":
                    towerDataAt.setWindSpeedAlarm("1");
                    pointAlert.setMonitorData(windspeed+"M/S");
                    pointAlert.setContent("风速报警");
                    pointAlert.setAlertType("1");
                    break;
                case "111":
                case "112":
                case "113":
                case "114":
                    towerDataAt.setCollisionAlarm("1");
                    pointAlert.setMonitorData("");
                    pointAlert.setContent("碰撞报警");
                    pointAlert.setAlertType("1");
                    break;
                case "101":
                    towerDataAt.setMinRangeAlarm("1");
                    pointAlert.setMonitorData("");
                    pointAlert.setContent("幅度限位报警");
                    pointAlert.setAlertType("1");
                    break;
                case "102":
                    towerDataAt.setHeightAlarm("1");
                    pointAlert.setMonitorData("");
                    pointAlert.setContent("高度限位报警");
                    pointAlert.setAlertType("1");
                    break;
                case "103":
                    towerDataAt.setPosAngleAlarm("1");
                    pointAlert.setMonitorData("");
                    pointAlert.setContent("回转限位报警");
                    pointAlert.setAlertType("1");
                    break;
                case "22":
                    towerDataAt.setWeightAlarm("2");
                    pointAlert.setMonitorData(weight+"T");
                    pointAlert.setContent("吊重预警");
                    pointAlert.setAlertType("0");
                    break;
                case "23":
                    towerDataAt.setMomentAlarm("2");
                    pointAlert.setMonitorData(Float.valueOf(HexEcodeUtil.hexEncode(momentByte))+"T·M");
                    pointAlert.setContent("力矩预警");
                    pointAlert.setAlertType("0");
                    break;
                case "24":
                    towerDataAt.setObliguityAlarm("2");
                    pointAlert.setMonitorData(obliguity+"度");
                    pointAlert.setContent("倾斜预警");
                    pointAlert.setAlertType("0");
                    break;
                case "25":
                    towerDataAt.setWindSpeedAlarm("2");
                    pointAlert.setMonitorData(windspeed+"M/S");
                    pointAlert.setContent("风速预警");
                    pointAlert.setAlertType("0");
                    break;
                case "211":
                case "212":
                case "213":
                case "214":
                    towerDataAt.setCollisionAlarm("2");
                    pointAlert.setMonitorData("");
                    pointAlert.setContent("碰撞预警");
                    pointAlert.setAlertType("0");
                    break;
                case "201":
                    towerDataAt.setMinRangeAlarm("2");
                    pointAlert.setMonitorData("");
                    pointAlert.setContent("幅度限位预警");
                    pointAlert.setAlertType("0");
                    break;
                case "202":
                    towerDataAt.setHeightAlarm("2");
                    pointAlert.setMonitorData("");
                    pointAlert.setContent("高度限位预警");
                    pointAlert.setAlertType("0");
                    break;
                case "203":
                    towerDataAt.setPosAngleAlarm("2");
                    pointAlert.setMonitorData("");
                    pointAlert.setContent("回转限位预警");
                    pointAlert.setAlertType("0");
                    break;
                default:
                    for(BMonitorPointAlert monitorPointAlert:list){
                        DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        monitorPointAlert.setHandleTime(new Date());
                        monitorPointAlert.setRecoveryData("0");
                        monitorPointAlert.setHandleConclu("监测值已恢复");
                        monitorPointAlert.setHandleState("1");
                        monitorPointAlert.setHandleMeasures("报警解除");
                        pointAlertService.updateById(monitorPointAlert);
                    }
            }
            if(list.size() == 0){
                if(pointAlert.getMonitorData() != null){
                    pointAlertService.save(pointAlert);
                }
            }
            String monitorData = "";//报警检测值
            String content = "";//报警内容
            String alertType = "";//报警类型
            //比对数据
            List<BEquipmentTowerDataAt> towerDataAts = dataAtService.lambdaQuery()
                    .eq(BEquipmentTowerDataAt::getEquipmentId, assets.getAssetId())
                    .eq(BEquipmentTowerDataAt::getTenantId, assets.getTenantId())
                    .eq(BEquipmentTowerDataAt::getProjectId, assets.getProjectId())
                    .orderByDesc(BEquipmentTowerDataAt::getMonitorTime).list();
            if (towerDataAts.size() > 0) {
                BEquipmentTowerDataAt dataAt = towerDataAts.get(0);
                if (!dataAt.toString().equals(towerDataAt.toString())) {
                    dataAtService.save(towerDataAt);
                }
            } else {
                dataAtService.save(towerDataAt);
            }
            //检测预警
//            if ("0".equals(warn)) {//检查已报警数据  全部置为已处理
//                List<BMonitorPointAlert> pointAlerts = pointAlertService.lambdaQuery()
//                        .eq(BMonitorPointAlert::getTenantId, assets.getTenantId())
//                        .eq(BMonitorPointAlert::getProjectId, assets.getProjectId())
//                        .eq(BMonitorPointAlert::getEquipmentId, assets.getAssetId())
//                        .eq(BMonitorPointAlert::getHandleState, "0").list();
//                if (pointAlerts.size() > 0) {
//                    for (BMonitorPointAlert pointAlert : pointAlerts) {
//                        pointAlert.setHandleState("1");
//                        pointAlert.setHandleTime(mDateTime);
//                    }
//                    pointAlertService.updateBatchById(pointAlerts);
//                }
//            } else {//发送报警
//                BMonitorPointAlert alert = new BMonitorPointAlert();
//                alert.setEquipmentId(assets.getAssetId());
//                alert.setProjectId(assets.getProjectId());
//                alert.setTenantId(assets.getTenantId());
//                alert.setCreateTime(new Date());
//
//                alert.setMonitorData(monitorData);
//                alert.setContent(content);
//                alert.setAlertType(alertType);
//                pointAlertService.save(alert);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void basicsInfo(byte[] bytes) {
        byte[] bytes1 = Arrays.copyOfRange(bytes,0,2);
        String str1 =  HexEcodeUtil.ByteArrayToHexStr(bytes1);
        log.info("起始符："+str1);
        byte[] bytes2 = Arrays.copyOfRange(bytes,2,3);
        String str2 =  HexEcodeUtil.ByteArrayToHexStr(bytes2);
        log.info("命令字："+str2);
        byte[] bytes3 =  Arrays.copyOfRange(bytes,3,4);
        String str3 =  HexEcodeUtil.ByteArrayToHexStr(bytes3);
        log.info("塔机编号："+str3);
        byte[] bytes4 =  Arrays.copyOfRange(bytes,4,7);
        String str4 =  HexEcodeUtil.ByteArrayToHexStr(bytes4);
        log.info("主机编号："+str4);
        byte[] bytes5 =  Arrays.copyOfRange(bytes,7,9);
        log.info("坐标X16："+HexEcodeUtil.ByteArrayToHexStr(bytes5));
        log.info("坐标X10："+HexEcodeUtil.hexEncode(bytes5));
        byte[] bytes6 =  Arrays.copyOfRange(bytes,9,11);
        log.info("坐标Y16："+HexEcodeUtil.ByteArrayToHexStr(bytes6));
        log.info("坐标Y10："+HexEcodeUtil.hexEncode(bytes6));
        byte[] bytes7 =  Arrays.copyOfRange(bytes,11,13);
        log.info("前臂长16："+HexEcodeUtil.ByteArrayToHexStr(bytes7));
        log.info("前臂长10："+HexEcodeUtil.hexEncode(bytes7));
        byte[] bytes8 =  Arrays.copyOfRange(bytes,13,15);
        log.info("后壁长16："+HexEcodeUtil.ByteArrayToHexStr(bytes8));
        log.info("后壁长10："+HexEcodeUtil.hexEncode(bytes8));
        byte[] bytes9 =  Arrays.copyOfRange(bytes,15,17);
        log.info("塔帽高16："+HexEcodeUtil.ByteArrayToHexStr(bytes9));
        log.info("塔帽高10："+HexEcodeUtil.hexEncode(bytes9));
        byte[] bytes10 =  Arrays.copyOfRange(bytes,17,19);
        log.info("塔臂高16："+HexEcodeUtil.ByteArrayToHexStr(bytes10));
        log.info("塔臂高10："+HexEcodeUtil.hexEncode(bytes10));
        byte[] bytes11 =  Arrays.copyOfRange(bytes,19,21);
        log.info("最大吊重16："+HexEcodeUtil.ByteArrayToHexStr(bytes11));
        log.info("最大吊重10："+HexEcodeUtil.hexEncode(bytes11));
        byte[] bytes12 =  Arrays.copyOfRange(bytes,21,23);
        log.info("最大力矩16："+HexEcodeUtil.ByteArrayToHexStr(bytes12));
        log.info("最大力矩10："+HexEcodeUtil.hexEncode(bytes12));
        byte[] bytes13 =  Arrays.copyOfRange(bytes,23,24);
        log.info("产权状态16："+HexEcodeUtil.ByteArrayToHexStr(bytes13));
        log.info("产权状态10："+HexEcodeUtil.hexEncode(bytes13));
        byte[] bytes14 =  Arrays.copyOfRange(bytes,24,54);
        log.info("塔机型号16："+HexEcodeUtil.ByteArrayToHexStr(bytes14));
        log.info("塔机型号10："+HexEcodeUtil.hexEncode(bytes14));
        byte[] bytes15 =  Arrays.copyOfRange(bytes,54,84);
        log.info("生产厂商16："+HexEcodeUtil.ByteArrayToHexStr(bytes15));
        log.info("生产厂商10："+HexEcodeUtil.hexEncode(bytes15));
        byte[] bytes16 =  Arrays.copyOfRange(bytes,84,86);
        log.info("吊钩重量16："+HexEcodeUtil.ByteArrayToHexStr(bytes16));
        log.info("吊钩重量10："+HexEcodeUtil.hexEncode(bytes16));
        byte[] bytes17 =  Arrays.copyOfRange(bytes,86,88);
        log.info("绞接长度16："+HexEcodeUtil.ByteArrayToHexStr(bytes17));
        log.info("绞接长度10："+HexEcodeUtil.hexEncode(bytes17));
        byte[] bytes18 =  Arrays.copyOfRange(bytes,88,89);
        log.info("传感器安装状态16："+HexEcodeUtil.ByteArrayToHexStr(bytes18));
        log.info("传感器安装状态2："+HexEcodeUtil.toBinary(bytes18));
        byte[] bytes21 =  Arrays.copyOfRange(bytes,89,95);
        log.info("结束符16："+HexEcodeUtil.ByteArrayToHexStr(bytes21));
        log.info("结束符10："+HexEcodeUtil.hexEncode(bytes21));
        String towCode = HexEcodeUtil.ByteArrayToHexStr(bytes4);
        BEquipmentAssets assets = getAsset(towCode);
        if (assets == null) {
            return;
        }
        BEquipmentTower equipmentTower = new BEquipmentTower();
        equipmentTower.setEquipmentId(assets.getAssetId());
        equipmentTower.setTenantId(assets.getTenantId());
        equipmentTower.setProjectId(assets.getProjectId());
        Double foreArmLength = Integer.valueOf(HexEcodeUtil.hexEncode(bytes7)) *0.1;
        equipmentTower.setForeArmLength(foreArmLength+"");
        Double rearArmLength = Integer.valueOf(HexEcodeUtil.hexEncode(bytes8)) *0.1;
        equipmentTower.setRearArmLength(rearArmLength+"");
        Double maxWeight = Integer.valueOf(HexEcodeUtil.hexEncode(bytes11)) *0.1;
        equipmentTower.setMaxWeight(maxWeight+"");
        Double armHeight = Integer.valueOf(HexEcodeUtil.hexEncode(bytes10)) *0.1;
        equipmentTower.setArmHeight(armHeight.floatValue());
        Double ratedTorque = Integer.valueOf(HexEcodeUtil.hexEncode(bytes12)) *0.1;
        equipmentTower.setRatedTorque(ratedTorque.floatValue()+"");
        Double capHeight = Integer.valueOf(HexEcodeUtil.hexEncode(bytes9)) *0.1;
        equipmentTower.setCapHeight(capHeight.floatValue());

        String cgqzt = HexEcodeUtil.toBinary(bytes18);
        char[] chars = cgqzt.toCharArray();
        equipmentTower.setWeightSet(chars[4]+"");
        equipmentTower.setWindSpeedSet(chars[3]+"");
        equipmentTower.setRangeSet(chars[6]+"");
        equipmentTower.setHeightSet(chars[7]+"");
        equipmentTower.setAngleSet(chars[5]+"");
        equipmentTower.setObliguitySet(chars[2]+"");
        UpdateWrapper<BEquipmentTower> wrapper = new UpdateWrapper<>();
        wrapper.eq("equipment_id",equipmentTower.getEquipmentId());
        wrapper.eq("tenant_id",equipmentTower.getTenantId());
        if(equipmentTower.getProjectId() == null){
            wrapper.isNull("project_id");
        }else {
            wrapper.eq("project_id",equipmentTower.getProjectId());
        }
        towerService.update(equipmentTower,wrapper);
    }

    /**
     * 工作循环上报
     *
     * @param bytes
     */
    public void workCycle(byte[] bytes) {
        byte[] bytes1 = Arrays.copyOfRange(bytes, 0, 2);
        String str1 = HexEcodeUtil.ByteArrayToHexStr(bytes1);
        log.info("起始符：" + str1);
        byte[] bytes2 = Arrays.copyOfRange(bytes, 2, 3);
        String str2 = HexEcodeUtil.ByteArrayToHexStr(bytes2);
        log.info("命令字：" + str2);
        byte[] bytes3 = Arrays.copyOfRange(bytes, 3, 4);
        String str3 = HexEcodeUtil.ByteArrayToHexStr(bytes3);
        log.info("塔机编号：" + str3);
        byte[] bytes4 = Arrays.copyOfRange(bytes, 4, 7);
        String str4 = HexEcodeUtil.ByteArrayToHexStr(bytes4);
        log.info("主机编号：" + str4);
        byte[] bytes5 = Arrays.copyOfRange(bytes, 7, 8);
        log.info("版本号16：" + HexEcodeUtil.ByteArrayToHexStr(bytes5));
        log.info("版本号10：" + HexEcodeUtil.hexEncode(bytes5));
        byte[] bytes6 = Arrays.copyOfRange(bytes, 8, 9);
        log.info("厂家及设备类型16：" + HexEcodeUtil.ByteArrayToHexStr(bytes6));
        log.info("厂家及设备类型10：" + HexEcodeUtil.hexEncode(bytes6));
        byte[] bytes7 = Arrays.copyOfRange(bytes, 9, 15);
        log.info("开始时间16：" + HexEcodeUtil.ByteArrayToHexStr(bytes7));
        log.info("开始时间10：" + HexEcodeUtil.hexEncode(bytes7));
        byte[] bytes8 = Arrays.copyOfRange(bytes, 15, 21);
        log.info("结束时间16：" + HexEcodeUtil.ByteArrayToHexStr(bytes8));
        log.info("结束时间10：" + HexEcodeUtil.hexEncode(bytes8));
        byte[] bytes9 = Arrays.copyOfRange(bytes, 21, 23);
        log.info("本次循环最大钓重16：" + HexEcodeUtil.ByteArrayToHexStr(bytes9));
        log.info("本次循环最大钓重10：" + HexEcodeUtil.hexEncode(bytes9));
        //无对应
        byte[] bytes10 = Arrays.copyOfRange(bytes, 23, 25);
        log.info("本次循环最大力矩16：" + HexEcodeUtil.ByteArrayToHexStr(bytes10));
        log.info("本次循环最大力矩10：" + HexEcodeUtil.hexEncode(bytes10));
        byte[] bytes11 = Arrays.copyOfRange(bytes, 25, 27);
        log.info("最大高度16：" + HexEcodeUtil.ByteArrayToHexStr(bytes11));
        log.info("最大高度10：" + HexEcodeUtil.hexEncode(bytes11));
        byte[] bytes12 = Arrays.copyOfRange(bytes, 27, 29);
        log.info("最小高度16：" + HexEcodeUtil.ByteArrayToHexStr(bytes12));
        log.info("最小高度10：" + HexEcodeUtil.hexEncode(bytes12));
        byte[] bytes13 = Arrays.copyOfRange(bytes, 29, 31);
        log.info("最大幅度16：" + HexEcodeUtil.ByteArrayToHexStr(bytes13));
        log.info("最大幅度10：" + HexEcodeUtil.hexEncode(bytes13));
        byte[] bytes14 = Arrays.copyOfRange(bytes, 31, 33);
        log.info("最小幅度16：" + HexEcodeUtil.ByteArrayToHexStr(bytes14));
        log.info("最小幅度10：" + HexEcodeUtil.hexEncode(bytes14));
        byte[] bytes15 = Arrays.copyOfRange(bytes, 33, 35);
        log.info("起吊点角度16：" + HexEcodeUtil.ByteArrayToHexStr(bytes15));
        log.info("起吊点角度10：" + HexEcodeUtil.hexEncode(bytes15));
        //无对应
        byte[] bytes16 = Arrays.copyOfRange(bytes, 35, 37);
        log.info("起吊点幅度16：" + HexEcodeUtil.ByteArrayToHexStr(bytes16));
        log.info("起吊点幅度10：" + HexEcodeUtil.hexEncode(bytes16));
        //无对应
        byte[] bytes17 = Arrays.copyOfRange(bytes, 37, 39);
        log.info("起吊点高度16：" + HexEcodeUtil.ByteArrayToHexStr(bytes17));
        log.info("起吊点高度10：" + HexEcodeUtil.hexEncode(bytes17));
        byte[] bytes18 = Arrays.copyOfRange(bytes, 39, 41);
        log.info("卸掉点角度16：" + HexEcodeUtil.ByteArrayToHexStr(bytes18));
        log.info("卸掉点角度10：" + HexEcodeUtil.hexEncode(bytes18));
        //无对应
        byte[] bytes19 = Arrays.copyOfRange(bytes, 41, 43);
        log.info("卸掉点幅度16：" + HexEcodeUtil.ByteArrayToHexStr(bytes19));
        log.info("卸掉点幅度10：" + HexEcodeUtil.hexEncode(bytes19));
        //无对应
        byte[] bytes20 = Arrays.copyOfRange(bytes, 43, 45);
        log.info("卸掉点高度16：" + HexEcodeUtil.ByteArrayToHexStr(bytes20));
        log.info("卸掉点高度10：" + HexEcodeUtil.hexEncode(bytes20));
        byte[] bytes21 = Arrays.copyOfRange(bytes, 45, 51);
        log.info("结束符16：" + HexEcodeUtil.ByteArrayToHexStr(bytes21));
        log.info("结束符10：" + HexEcodeUtil.hexEncode(bytes21));
        try{
            //根据编号获取塔机
            byte[] towCodeByte = Arrays.copyOfRange(bytes, 4, 7);
            String towCode = HexEcodeUtil.ByteArrayToHexStr(towCodeByte);
            BEquipmentAssets assets = getAsset(towCode);
            if (assets == null) {
                return;
            }
            //组装数据
            BEquipmentTowerEffect towerEffect = new BEquipmentTowerEffect();
            //基础信息
            towerEffect.setAssetId(assets.getAssetId());
            towerEffect.setTenantId(assets.getTenantId());
            towerEffect.setProjectId(assets.getProjectId());
            towerEffect.setCreateTime(new Date());
            //报文信息
            byte[] startTimeByte = Arrays.copyOfRange(bytes, 9, 15);
            towerEffect.setStartTime(HexEcodeUtil.analysisDate(HexEcodeUtil.ByteArrayToHexStr(startTimeByte)));
            byte[] endTimeByte = Arrays.copyOfRange(bytes, 15, 21);
            towerEffect.setEndTime(HexEcodeUtil.analysisDate(HexEcodeUtil.ByteArrayToHexStr(endTimeByte)));
            byte[] weightByte = Arrays.copyOfRange(bytes, 21, 23);//吊重
            Double weight = Integer.parseInt(HexEcodeUtil.hexEncode(weightByte)) * 0.001;
            towerEffect.setWeight(weight);
            byte[] maxPercentByte = Arrays.copyOfRange(bytes, 21, 23);//最大力矩
            Double maxPercent = Integer.parseInt(HexEcodeUtil.hexEncode(maxPercentByte)) * 0.01;
            towerEffect.setMaxPercent(maxPercent);
            byte[] maxHByte = Arrays.copyOfRange(bytes, 25, 27);//最大高度
            Double maxH = Integer.parseInt(HexEcodeUtil.hexEncode(maxHByte)) * 0.1;
            towerEffect.setMaxHeight(maxH);
            byte[] minHByte = Arrays.copyOfRange(bytes, 27, 29);
            Double minH = Integer.parseInt(HexEcodeUtil.hexEncode(minHByte)) * 0.1;
            towerEffect.setMinHeight(minH);
            byte[] maxRByte = Arrays.copyOfRange(bytes, 29, 31);//幅度
            Double maxR = Integer.parseInt(HexEcodeUtil.hexEncode(maxRByte)) * 0.1;
            towerEffect.setMaxRadius(maxR);
            byte[] minRByte = Arrays.copyOfRange(bytes, 31, 33);
            Double minR = Integer.parseInt(HexEcodeUtil.hexEncode(minRByte)) * 0.1;
            towerEffect.setMinRadius(minR);
            byte[] startAngleByte = Arrays.copyOfRange(bytes, 33, 35);//起吊点角度
            Double startAngle = Integer.parseInt(HexEcodeUtil.hexEncode(startAngleByte)) * 0.1;
            towerEffect.setStartAngle(startAngle);
            byte[] endAngleByte = Arrays.copyOfRange(bytes, 39, 41);//卸吊点角度
            Double endAngle = Integer.parseInt(HexEcodeUtil.hexEncode(endAngleByte)) * 0.1;
            towerEffect.setEndAngle(endAngle);
            Float liftingPointRadius = Float.valueOf(Integer.parseInt(HexEcodeUtil.hexEncode(bytes16))) * 0.1F;//起吊点服度
            towerEffect.setLiftingPointRadius(liftingPointRadius);
            Float liftingPointHeight = Float.valueOf(Integer.parseInt(HexEcodeUtil.hexEncode(bytes17))) * 0.1F;//起吊点高度
            towerEffect.setLiftingPointHeight(liftingPointHeight);
            Float liftingPointRadiusUnload = Float.valueOf(Integer.parseInt(HexEcodeUtil.hexEncode(bytes19))) * 0.1F;//卸吊点服度
            towerEffect.setLiftingPointRadiusUnload(liftingPointRadiusUnload);
            Float liftingPointHeightUnload = Float.valueOf(Integer.parseInt(HexEcodeUtil.hexEncode(bytes20))) * 0.1F;//卸吊点高度
            towerEffect.setLiftingPointHeightUnload(liftingPointHeightUnload);
            //补充信息
            towerEffect.setMaxMomentEarlyAlarm(0);
            towerEffect.setEndEarlyAlarm(0);
            towerEffect.setStartEarlyAlarm(0);
            towerEffect.setMaxMomentAlarm(0);
            towerEffect.setEndAlarm(0);
            towerEffect.setStartAlarm(0);
            towerEffect.setStartPeccancy(0);
            towerEffect.setEndPeccancy(0);
            towerEffect.setMaxMomentPeccancy(0);
            //存入
            towerEffectService.save(towerEffect);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void staffInfo(byte[] bytes) {
        byte[] bytes1 = Arrays.copyOfRange(bytes, 0, 2);
        String str1 = HexEcodeUtil.ByteArrayToHexStr(bytes1);
        log.info("起始符：" + str1);
        byte[] bytes2 = Arrays.copyOfRange(bytes, 2, 3);
        String str2 = HexEcodeUtil.ByteArrayToHexStr(bytes2);
        log.info("命令字：" + str2);
        byte[] bytes3 = Arrays.copyOfRange(bytes, 3, 4);
        String str3 = HexEcodeUtil.ByteArrayToHexStr(bytes3);
        log.info("塔机编号：" + str3);
        byte[] bytes4 = Arrays.copyOfRange(bytes, 4, 7);
        String str4 = HexEcodeUtil.ByteArrayToHexStr(bytes4);
        log.info("主机编号：" + str4);
        //无对应
        byte[] bytes5 = Arrays.copyOfRange(bytes, 7, 8);
        log.info("识别结果状态值16：" + HexEcodeUtil.ByteArrayToHexStr(bytes5));
        log.info("识别结果状态值10：" + HexEcodeUtil.hexEncode(bytes5));
        //无对应
        byte[] bytes6 = Arrays.copyOfRange(bytes, 8, 40);
        log.info("用户名：" + HexEcodeUtil.toStringHex2(HexEcodeUtil.ByteArrayToHexStr(bytes6)));
        //无对应
        byte[] bytes7 = Arrays.copyOfRange(bytes, 40, 48);
        log.info("用户id：" + HexEcodeUtil.toStringHex2(HexEcodeUtil.ByteArrayToHexStr(bytes7)));
        //无对应
        byte[] bytes8 = Arrays.copyOfRange(bytes, 48, 49);
        log.info("识别分数16：" + HexEcodeUtil.ByteArrayToHexStr(bytes8));
        log.info("识别分数10：" + HexEcodeUtil.hexEncode(bytes8));
        byte[] bytes9 = Arrays.copyOfRange(bytes, 49, 67);
        log.info("身份证号码：" + HexEcodeUtil.toStringHex2(HexEcodeUtil.ByteArrayToHexStr(bytes9)));
        //入库
        try {
            //根据编号获取塔机
            byte[] towCodeByte = Arrays.copyOfRange(bytes, 4, 7);
            String towCode = HexEcodeUtil.ByteArrayToHexStr(towCodeByte);
            BEquipmentAssets assets = getAsset(towCode);
            if (assets == null) {
                return;

            }
            //组装数据
            BEquipmentDriverIdent driverIdent = new BEquipmentDriverIdent();
            //基础信息
            driverIdent.setEquCode(towCode);
            driverIdent.setTenantId(assets.getTenantId());
            driverIdent.setProjectId(assets.getProjectId());
            driverIdent.setCreateTime(new Date());
            driverIdent.setDeviceType("1");//塔机
            driverIdent.setStartingDate(new Date());//上岗时间
            byte[] idCard = Arrays.copyOfRange(bytes, 49, 67);
            driverIdent.setIdCard(HexEcodeUtil.toStringHex2(HexEcodeUtil.ByteArrayToHexStr(idCard)));

            driverIdentService.save(driverIdent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取设备
     *
     * @param code
     * @return
     */
    private BEquipmentAssets getAsset(String code) {
        List<BEquipmentAssets> equipmentAssetsList = equipmentAssetsService.lambdaQuery()
                .eq(BEquipmentAssets::getAssetCode, code)
                .eq(BEquipmentAssets::getStatus, "1")
                .isNotNull(BEquipmentAssets::getTenantId)
                .isNotNull(BEquipmentAssets::getProjectId)
                .or()
                .isNull(BEquipmentAssets::getProjectId)
                .list();

        BEquipmentAssets assets = null;
        if (equipmentAssetsList.size() > 0) {
            assets = equipmentAssetsList.get(0);
        } else {
            log.error("Tower Asset Error：invalid asset code =>" + code);
        }
        return assets;
    }

    public BMonitorPointAlert copyPastry(BMonitorPointAlert copyObj){
        BMonitorPointAlert mMonitorPointAlert = new BMonitorPointAlert();
        mMonitorPointAlert.setProjectId(copyObj.getProjectId());
        mMonitorPointAlert.setTenantId(copyObj.getTenantId());
        mMonitorPointAlert.setEquipmentId(copyObj.getEquipmentId());
        mMonitorPointAlert.setMonitorPointId(copyObj.getMonitorPointId());
        mMonitorPointAlert.setCreateTime(new Date());
        mMonitorPointAlert.setHandleState("0");
        return mMonitorPointAlert;
    }

    public static void main(String[] args) {
//        String str = "A55A3D0290F72F02111301080A1C211301080A1D2117D300C7012C008200000000000000000082000000000104B52DCC33C33C";
//        AnalysisService analysisService = new AnalysisService();
//        analysisService.workCycle(HexEcodeUtil.HexStrToByteArray(str));
        System.out.println(HexEcodeUtil.toStringHex2("333430383231313938383131313130303158"));
    }
}
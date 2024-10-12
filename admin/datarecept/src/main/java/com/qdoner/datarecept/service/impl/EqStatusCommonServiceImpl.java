package com.qdoner.datarecept.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.core.common.Result;
import com.kakarote.core.message.MessageEntity;
import com.kakarote.core.message.MessageType;
import com.qdoner.datarecept.entity.PO.BEquipmentAssets;
import com.qdoner.datarecept.entity.PO.BMonitorPointAlert;
import com.qdoner.datarecept.entity.PO.EquipmentOffline;
import com.qdoner.datarecept.job.FluoriteCloudJob;
import com.qdoner.datarecept.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class EqStatusCommonServiceImpl implements EqStatusCommonService {
    @Autowired
    private FluoriteCloudJob fluoriteCloudJob;

    @Autowired
    private IBEquipmentAssetsService assetsService;

    @Autowired
    private IBMonitorPointAlertService monitorPointAlertService;

    @Autowired
    private SendProjectMsgService sendProjectMsgService;

    @Autowired
    private IEquipmentOfflineService offlineService;

    /**
     * 1、判断当前设备状态是否变动
     * 2、变动更新资产主表状态及时间、插入启停记录表、离线触发报警、离线推送消息
     *
     * @param assetId   设备ID
     * @param newStatus 当前状态
     */
    @Override
    public Result handleOfflineSatus(Integer assetId, String newStatus) {
        //原来设备状态
        BEquipmentAssets asset = assetsService.getBaseMapper().selectById(assetId);
        if (asset == null) {
            return Result.error(-1, "不存在的设备");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startHandle(asset, newStatus);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return Result.ok();
    }

    private void startHandle(BEquipmentAssets asset, String newStatus) {
        String oldStatus = asset.getOfflineStatus();
        if (newStatus.equals(oldStatus)) {//状态未变动
            if ("1".equals(newStatus)) {//在线持续更新数据刷新时间
                asset.setOfflineDate(new Date());
            }
        } else {//状态变动
            asset.setOfflineStatus(newStatus);
            asset.setOfflineDate(new Date());
            //alert
            if ("1".equals(newStatus)) {//在线恢复报警数据  防止VideoJobAlertFlag变动出现报警无法恢复
                recoveryAlertData(asset);
            } else {//离线尝试报警
                if (fluoriteCloudJob.getVideoJobAlertFlag()) {
                    handleAlertData(asset);
                }
            }
            //msg
            sendMsg(asset);
            //上线下记录
            logOffLine(asset);
        }
        //更新库
        assetsService.updateById(asset);
    }

    private void logOffLine(BEquipmentAssets asset) {
        EquipmentOffline equipmentOffline = new EquipmentOffline();
        equipmentOffline.setEquipmentId(asset.getAssetId());
        Integer status = 1;
        if ("0".equals(asset.getOfflineStatus())) {
            status = 2;
        }
        if ("1".equals(asset.getOfflineStatus())) {
            status = 1;
        }
        equipmentOffline.setStatus(status);
        equipmentOffline.setProjectId(asset.getProjectId());
        equipmentOffline.setTenantId(asset.getTenantId());
        equipmentOffline.setMonitorTime(new Date());
        offlineService.save(equipmentOffline);
    }

    //发送通知
    private void sendMsg(BEquipmentAssets asset) {
        String content = "";
        String messageCode = "";
        if ("1".equals(asset.getStatus())) {
            content = "已上线";
            messageCode = MessageType.MONITOR_EQUI_ONLINE;
        } else {
            content = "已离线";
            messageCode = MessageType.MONITOR_EQUI_OFFLINE;
        }
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setTitle(String.valueOf("[" + asset.getProjectName() + "]" + asset.getAssetName() + "设备" + content));
        messageEntity.setData(null);
        messageEntity.setCode(messageCode);
        messageEntity.setProjectId(Long.parseLong(asset.getProjectId().toString()));
        messageEntity.setTenantId(asset.getTenantId());
        try {
            sendProjectMsgService.send(messageEntity, true);
        } catch (Exception e) {
            log.error("设备状态变更提醒失败=>{}", e);
        }

    }
    /**
    * @Description:设备报警通知推送
    * @author shz
    * @date 2023-5-8 16:17
    * @param 
    * @return 
    */
    public void sendMsgEquipment(BMonitorPointAlert pointAlert) {
        BEquipmentAssets asset = assetsService.getBaseMapper().selectById(pointAlert.getEquipmentId());
        String messageCode  = MessageType.MONITOR_EQUI_WARN;
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setTitle(String.valueOf("[" + asset.getProjectName() + "]" + asset.getAssetName() + "设备" + pointAlert.getContent()));
        messageEntity.setData(String.valueOf(asset.getAssetId()));
        messageEntity.setCode(messageCode);
        messageEntity.setProjectId(Long.parseLong(asset.getProjectId().toString()));
        messageEntity.setTenantId(asset.getTenantId());
        try {
            sendProjectMsgService.send(messageEntity, true);
        } catch (Exception e) {
            log.error("设备状态变更提醒失败=>{}", e);
        }

    }

    /**
     * 恢复报警
     *
     * @param asset
     */
    private void recoveryAlertData(BEquipmentAssets asset) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("equipment_id", asset.getAssetId());
        queryWrapper.eq("handle_state", "0");
        queryWrapper.eq("tenant_id", asset.getTenantId());
        queryWrapper.eq("project_id", asset.getProjectId());
        List<BMonitorPointAlert> list = monitorPointAlertService.list(queryWrapper);
        for (BMonitorPointAlert mpa : list) {
            mpa.setHandleTime(new Date());
            mpa.setHandleConclu("设备已恢复");
            mpa.setHandleState("1");
            mpa.setHandleMeasures("设备已上线");
            mpa.setHandleBy("admin");
            mpa.setProjectId(asset.getProjectId());
            mpa.setTenantId(asset.getTenantId());
            monitorPointAlertService.updateById(mpa);
        }
    }

    /**
     * @return void
     * @throws
     * @description 处理报警
     * @Params [设备信息]
     * @updateTime 2022/4/14 16:04
     */
    private void handleAlertData(BEquipmentAssets asset) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("equipment_id", asset.getAssetId());
        queryWrapper.eq("handle_state", "0");
        queryWrapper.eq("tenant_id", asset.getTenantId());
        queryWrapper.eq("project_id", asset.getProjectId());
        List<BMonitorPointAlert> list = monitorPointAlertService.list(queryWrapper);
        //如果设备离线，插入离线报警数据，判断之前是否存在离线报警数据，如果不存在插入，否则跳过
        if (list.size() == 0) {
            BMonitorPointAlert monitorPointAlert = new BMonitorPointAlert();
            monitorPointAlert.setAlertType("1");//默认 1警报提醒
            monitorPointAlert.setContent(String.valueOf(asset.getAssetName() + "-- 已离线"));
            monitorPointAlert.setEquipmentId(asset.getAssetId());
            monitorPointAlert.setProjectId(asset.getProjectId());
            monitorPointAlert.setTenantId(asset.getTenantId());
            monitorPointAlert.setHandleState("0");//默认未处理
            monitorPointAlertService.save(monitorPointAlert);
        }
    }

}

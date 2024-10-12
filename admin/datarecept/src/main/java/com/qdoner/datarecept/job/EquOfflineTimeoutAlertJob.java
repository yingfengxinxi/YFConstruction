package com.qdoner.datarecept.job;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.core.common.Result;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.message.MessageEntity;
import com.kakarote.core.message.MessageType;
import com.qdoner.datarecept.entity.PO.*;
import com.qdoner.datarecept.service.*;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class EquOfflineTimeoutAlertJob {

    @Autowired
    private IEquipmentOfflineService offlineService;
    @Autowired
    private IBEquipmentAssetsService assetsService;
    @Autowired
    private IBMonitorPointAlertService monitorPointAlertService;
    @Autowired
    private IBEquipmentDriverIdentService driverIdentService;

    @Autowired
    private SendProjectMsgService sendProjectMsgService;

    @Autowired
    private EqStatusCommonService eqStatusCommonService;

    @Autowired
    private AdminService adminService;

    //@Scheduled(cron = "* * */2 * * ?")
    //@Scheduled(cron = "*/30 * * * * ?")
    @XxlJob("updateEquStatus")
    public void mechanicalEquipmentOffline(){
        /*List<Map> listTower = offlineService.selectOfflineTower();
        List<Map> listElevator = offlineService.selectOfflineElevator();
        for(Map map:listTower){
            QueryWrapper<BEquipmentAssets> wrapper = new QueryWrapper<>();
            wrapper.eq("asset_id",map.get("equipmentId"));
            wrapper.eq("project_id",map.get("projectId"));
            wrapper.eq("tenant_id",map.get("tenantId"));
            BEquipmentAssets assets = assetsService.getOne(wrapper);
            EquipmentOffline equipmentOffline = new EquipmentOffline();
            equipmentOffline.setEquipmentId((Integer) map.get("equipmentId"));
            equipmentOffline.setStatus(2);
            equipmentOffline.setProjectId((Integer) map.get("projectId"));
            equipmentOffline.setTenantId((Integer) map.get("tenantId"));
            equipmentOffline.setMonitorTime(new Date());
            offlineService.save(equipmentOffline);
            assets.setOfflineDate(new Date());
            assets.setOfflineStatus("2");
            assetsService.updateById(assets);
            BMonitorPointAlert monitorPointAlert = new BMonitorPointAlert();
            monitorPointAlert.setAlertType("1");
            monitorPointAlert.setContent(String.valueOf("塔机设备" + assets.getAssetName() + "已离线"));
            monitorPointAlert.setEquipmentId(assets.getAssetId());
            monitorPointAlert.setProjectId(assets.getProjectId());
            monitorPointAlert.setTenantId(assets.getTenantId());
            monitorPointAlert.setHandleState("0");
            monitorPointAlertService.save(monitorPointAlert);
            QueryWrapper<BEquipmentDriverIdent> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("equ_code",assets.getAssetCode());
            queryWrapper.eq("project_id",map.get("projectId"));
            queryWrapper.eq("tenant_id",map.get("tenantId"));
            queryWrapper.eq("device_type","1");
            queryWrapper.isNull("ending_date");
            List<BEquipmentDriverIdent> driverIdents = driverIdentService.list(queryWrapper);
            for(BEquipmentDriverIdent driverIdent:driverIdents){
                driverIdent.setEndingDate(new Date());
            }
            if(driverIdents.size()>0){
                driverIdentService.updateBatchById(driverIdents);
            }
        }
        for(Map map:listElevator){
            QueryWrapper<BEquipmentAssets> wrapper = new QueryWrapper<>();
            wrapper.eq("asset_id",map.get("equipmentId"));
            wrapper.eq("project_id",map.get("projectId"));
            wrapper.eq("tenant_id",map.get("tenantId"));
            BEquipmentAssets assets = assetsService.getOne(wrapper);
            EquipmentOffline equipmentOffline = new EquipmentOffline();
            equipmentOffline.setEquipmentId((Integer) map.get("equipmentId"));
            equipmentOffline.setStatus(2);
            equipmentOffline.setProjectId((Integer) map.get("projectId"));
            equipmentOffline.setTenantId((Integer) map.get("tenantId"));
            equipmentOffline.setMonitorTime(new Date());
            offlineService.save(equipmentOffline);
            assets.setOfflineDate(new Date());
            assets.setOfflineStatus("2");
            assetsService.updateById(assets);
            BMonitorPointAlert monitorPointAlert = new BMonitorPointAlert();
            monitorPointAlert.setAlertType("1");
            monitorPointAlert.setContent(String.valueOf("升降机设备" + assets.getAssetName() + "已离线"));
            monitorPointAlert.setEquipmentId(assets.getAssetId());
            monitorPointAlert.setProjectId(assets.getProjectId());
            monitorPointAlert.setTenantId(assets.getTenantId());
            monitorPointAlert.setHandleState("0");
            monitorPointAlertService.save(monitorPointAlert);
            QueryWrapper<BEquipmentDriverIdent> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("equ_code",assets.getAssetCode());
            queryWrapper.eq("project_id",map.get("projectId"));
            queryWrapper.eq("tenant_id",map.get("tenantId"));
            queryWrapper.eq("device_type","2");
            queryWrapper.isNull("ending_date");
            List<BEquipmentDriverIdent> driverIdents = driverIdentService.list(queryWrapper);
            for(BEquipmentDriverIdent driverIdent:driverIdents){
                driverIdent.setEndingDate(new Date());
            }
            if(driverIdents.size()>0){
                driverIdentService.updateBatchById(driverIdents);
            }
        }*/

        // 新版设备状态更新
        // TODO 获取时间配置
        AdminConfig adminConfig = new AdminConfig();
        Result<List<AdminConfig>> Feignresult = adminService.queryConfigByNameAndTenantId("mechanicalEquipmentOffline");
        List<AdminConfig> adminConfigList = Feignresult.getData();
        if (adminConfigList != null && adminConfigList.size() > 0) {
            adminConfig = Feignresult.getData().get(0);
        } else {
            throw new CrmException(500, "adminConfig不能为空");
        }

        // 查询塔机数据
        List<Map<String,Object>> towerCraneDataList = assetsService.selectTowerCraneDataList(adminConfig.getValue());
        // 更新数据
        for (int i = 0; i < towerCraneDataList.size(); i++) {
            Map<String, Object> map = towerCraneDataList.get(i);
            eqStatusCommonService.handleOfflineSatus((Integer) map.get("assetId"), String.valueOf(map.get("offline")));
        }

        // 查询升降机数据
        List<Map<String,Object>> elvatorDataList = assetsService.selectElevatorDataList(adminConfig.getValue());
        // 更新数据
        for (int i = 0; i < elvatorDataList.size(); i++) {
            Map<String, Object> map = elvatorDataList.get(i);
            eqStatusCommonService.handleOfflineSatus((Integer) map.get("assetId"), String.valueOf(map.get("offline")));
        }
        // 查询监测数据
        List<Map<String,Object>> monitorDataList = assetsService.selectMonitorDataList(adminConfig.getValue());
        // 更新数据
        for (int i = 0; i < monitorDataList.size(); i++) {
            Map<String, Object> map = monitorDataList.get(i);
            eqStatusCommonService.handleOfflineSatus((Integer) map.get("assetId"), String.valueOf(map.get("offline")));
        }
        // 查询标养室数据
        List<Map<String,Object>> roomDataList = assetsService.selectRoomDataList(adminConfig.getValue());
        // 更新数据
        for (int i = 0; i < roomDataList.size(); i++) {
            Map<String, Object> map = roomDataList.get(i);
            eqStatusCommonService.handleOfflineSatus((Integer) map.get("assetId"), String.valueOf(map.get("offline")));
        }
    }

    private void updateEquStatus(List<Map<String, Object>> list) {
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            eqStatusCommonService.handleOfflineSatus((Integer) map.get("assetId"), String.valueOf(map.get("offline")));
        }
    }
}

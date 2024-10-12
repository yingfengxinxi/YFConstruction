package com.kakarote.build.equipment.job;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.api.service.SendProjectMsgService;
import com.kakarote.build.equipment.entity.PO.EquipmentAssets;
import com.kakarote.build.equipment.entity.PO.EquipmentDriverIdent;
import com.kakarote.build.equipment.entity.PO.EquipmentOffline;
import com.kakarote.build.equipment.entity.PO.MonitorPointAlert;
import com.kakarote.build.equipment.mapper.EquipmentAssetsMapper;
import com.kakarote.build.equipment.service.IEquipmentAssetsService;
import com.kakarote.build.equipment.service.IEquipmentDriverIdentService;
import com.kakarote.build.equipment.service.IEquipmentOfflineService;
import com.kakarote.build.equipment.service.impl.EquipmentMonitorPointServiceImpl;
import com.kakarote.build.equipment.service.impl.MonitorPointAlertServiceImpl;
import com.kakarote.build.utils.HttpClientResult;
import com.kakarote.build.utils.HttpClientUtils;
import com.kakarote.core.common.Result;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.message.MessageEntity;
import com.kakarote.core.message.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Component
@Slf4j
public class EquOfflineTimeoutAlertJob {

    @Autowired
    private AdminService adminService;

    @Autowired
    private EquipmentMonitorPointServiceImpl equipmentMonitorPointService;

    @Autowired
    private SendProjectMsgService sendProjectMsgService;

    @Autowired
    private MonitorPointAlertServiceImpl monitorPointAlertService;

    @Autowired
    private EquipmentAssetsMapper equipmentAssetsMapper;

    @Autowired
    private IEquipmentAssetsService assetsService;

    @Autowired
    private IEquipmentOfflineService offlineService;

    @Autowired
    private IEquipmentDriverIdentService driverIdentService;

    @Value("${wvp.config.urlPrefix}")
    private String wvpUrlPrefix;

    //@Scheduled(cron = "*/15 * * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void offlineTimeOutAlert() {
        log.info("==设备离线超时报警");
        // 获取设备离线小时数
        Result<List<AdminConfig>> result = adminService.queryConfigByNameAndTenantId("equOfflineHour");
        if (!result.hasSuccess()) {
            log.info("==设备离线小时数获取失败");
            throw new CrmException(500, "设备离线小时数获取失败");
        }
        List<AdminConfig> adminConfigList = result.getData();
        if (adminConfigList == null || adminConfigList.size() <= 0) {
            log.info("==设备离线小时数不存在");
            throw new CrmException(500, "设备离线小时数不存在");
        }
        Integer offlineTime = Integer.valueOf(adminConfigList.get(0).getValue());
        // 查询所有监测点的状态 取最新的状态数据 只查询状态记录表中有数据的监测点，未存在数据的监测点认为还未进行对接。
        List<Map<String, Object>> pointList = equipmentMonitorPointService.getBaseMapper().selectPointAndMonitorStatus(offlineTime);

        // 判断监测点状态
        for (Map<String, Object> point : pointList) {
            Integer projectId = (Integer) point.get("projectId");
            String content = "";
            Map<String, Object> params = new HashMap<>();
            params.put("pointId", point.get("pointId"));
            params.put("monitorAlertType", '2');
            params.put("tenantId", point.get("tenantId"));
            String dictType = "b_equipment_monitor_type";
            dictType += point.get("monitorType");
            // 查询当前监测点最新的报警数据
            MonitorPointAlert monitorPointAlert = null;
            monitorPointAlert = monitorPointAlertService.getBaseMapper().selectByPointIdAndMonitorAlertType(params);
            // 判断是否存在未处理的报警数据
            Boolean isExistAlert = false;
            if (monitorPointAlert != null && monitorPointAlert.getHandleTime() == null) {
                isExistAlert = true;
            }
            // isN 监测点状态，0：不等于N； 1：等于N；
            // isTimeout 是否离线超时 0:未离线超时；1：离线超时；
            if ("0".equals(point.get("isN"))) {
                // 如果已存在报警数据，并且报警数据未处理，不进行报警
                if (isExistAlert) {
                    continue;
                }
                // status!=N 报警
                // 查询监测点类型
                if (point.get("dictLabel") != null) {
                    content = (String) point.get("dictLabel");
                }
                content += (String) point.get("des");
            } else if ("1".equals(point.get("isTimeout"))) {
                // 如果已存在报警数据，并且报警数据未处理，不进行报警
                if (isExistAlert) {
                    continue;
                }
                // 判断当前监测点的最新上传时间，是否超过设备离线小时数。
                if (point.get("dictLabel") != null) {
                    content = (String) point.get("dictLabel");
                }
                content += "监测设备长时间未上传数据";
            } else {
                // 如果项目已经正常，并且最新一条数据处理时间为空，则对该数据进行处理。
                if (isExistAlert) {
                    monitorPointAlert.setHandleTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    monitorPointAlert.setHandleMeasures("恢复在线");
                    monitorPointAlert.setHandleConclu("恢复在线");
                    monitorPointAlertService.getBaseMapper().updateByIdAndTenantId(monitorPointAlert);
                }
                continue;
            }
            monitorPointAlert = new MonitorPointAlert();
            monitorPointAlert.setAlertType("1");
            monitorPointAlert.setContent(content);
            monitorPointAlert.setEquipmentId((Integer) point.get("equipmentId"));
            monitorPointAlert.setMonitorPointId((Integer) point.get("pointId"));
            monitorPointAlert.setProjectId(projectId);
            monitorPointAlert.setTenantId((Integer) point.get("tenantId"));
            monitorPointAlert.setHandleState("0");
            monitorPointAlert.setMonitorAlertType("2"); // 离线报警
            monitorPointAlertService.save(monitorPointAlert);
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setTitle(content);
            messageEntity.setData(String.valueOf(monitorPointAlert.getId()));
            messageEntity.setCode(MessageType.EQUI_OFFLINE_TIMEOUT);
            messageEntity.setProjectId(projectId.longValue());//获取到登录用户时可不填
            messageEntity.setTenantId((Integer) point.get("tenantId"));//获取到登录用户时可不填
            sendProjectMsgService.send(messageEntity, true);
        }
    }

//    @Scheduled(cron = "0 */1 * * * ?")
//    @Scheduled(cron = "*/15 * * * * ?")
    //wvp视频离线暂未启用
    public void equiStateMonitor() {
        log.info("==视频监控设备离线报警");
        List<Map<String, Object>> alarmList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("isGroup", "1");
        //使用流媒体设备，通道与设备资产关系表
        List<Map<String, Object>> deviceLists = equipmentAssetsMapper.videoMonitorDevices(params);
        for (Map<String, Object> device : deviceLists) {
            params.clear();
            params.put("deviceId", String.valueOf(device.get("deviceId")));
            List<Map<String, Object>> channelLists = equipmentAssetsMapper.videoMonitorDevices(params);

            //使用同步设备通道接口，查询当前设备状态
            String public_signURL = wvpUrlPrefix + "api/device/query/devices/" + String.valueOf(device.get("deviceId")) + "/status";
            Map<String, Object> map = new HashMap<>();
            HttpClientResult result = null;
            try {
                result = HttpClientUtils.doPostJSON(public_signURL, JSONObject.parseObject(JSON.toJSONString(map)));
                if ("200".equals(result.getCode())) {
                    JSONObject jsonObject = result.getContent().getJSONObject("result");

                    //当前不知result结构，先模拟JSON未知key循环
                    for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                        boolean flag = true;
                        //循环已知设备通道，筛选不存在通道
                        for (Map<String, Object> channel : channelLists) {
//                                    if(String.valueOf(device.get("deviceId")).equals(channel.get("deviceId"))){
                            if (entry.getKey().equals(channel.get("deviceChannelId"))) {
                                flag = false;
//                                System.out.println(entry.getKey()+" "+entry.getValue());
//                                //System.out.println(entry.getKey().getClass().toString());
                            }
//                                    }
                            //如果通道存在flag=false,如果不存在则为true 把当前通道添加进报警list
                            if (flag) {
                                alarmList.add(channel);
                            }
                        }
                    }
                } else {
                    throw new CrmException(999, "错误返回code==" + String.valueOf(result.getCode()));
                }
            } catch (Exception e) {
                //如果接口访问异常，则说明当前设备下的所有通道都不在线
                for (Map<String, Object> channel : channelLists) {
                    alarmList.add(channel);
                }
            }
        }
        this.saveAndSend(alarmList);
    }
    @Transactional(rollbackFor = Exception.class)
    public void saveAndSend(List<Map<String, Object>> alarmList){
        for (Map<String, Object> alaem : alarmList) {
            MonitorPointAlert monitorPointAlert = new MonitorPointAlert();
            monitorPointAlert.setAlertType("1");
            monitorPointAlert.setContent(String.valueOf("视频监控设备" + alaem.get("assetName") + "已离线"));
            monitorPointAlert.setEquipmentId(Integer.valueOf(String.valueOf(alaem.get("assetId"))));
            monitorPointAlert.setProjectId(Integer.valueOf(String.valueOf(alaem.get("projectId"))));
            monitorPointAlert.setTenantId(Integer.valueOf(String.valueOf(alaem.get("tenantId"))));
            monitorPointAlert.setHandleState("0");
            monitorPointAlertService.save(monitorPointAlert);
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setTitle(String.valueOf("视频监控设备" + alaem.get("assetName") + "已离线"));
            messageEntity.setData(String.valueOf(monitorPointAlert.getId()));
            messageEntity.setCode(MessageType.EQUI_OFFLINE_TIMEOUT);
            messageEntity.setProjectId(Integer.valueOf(String.valueOf(alaem.get("projectId"))).longValue());//获取到登录用户时可不填
            messageEntity.setTenantId(Integer.valueOf(String.valueOf(alaem.get("tenantId"))));//获取到登录用户时可不填
            sendProjectMsgService.send(messageEntity, true);
        }
    }

    /**
    * @Description:塔机升降机离线处理（以转移datarecept服务，当前定时停用）
    * @author shz
    * @date 2022-8-18 14:15
    */
//    @Scheduled(cron = "*/30 * * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void mechanicalEquipmentOffline(){
        List<Map> listTower = offlineService.selectOfflineTower();
        List<Map> listElevator = offlineService.selectOfflineElevator();
        for(Map map:listTower){
            QueryWrapper<EquipmentAssets> wrapper = new QueryWrapper<>();
            wrapper.eq("asset_id",map.get("equipmentId"));
            wrapper.eq("project_id",map.get("projectId"));
            wrapper.eq("tenant_id",map.get("tenantId"));
            EquipmentAssets assets = assetsService.getOne(wrapper);
            EquipmentOffline equipmentOffline = new EquipmentOffline();
            equipmentOffline.setEquipmentId((Integer) map.get("equipmentId"));
            equipmentOffline.setStatus(2);
            equipmentOffline.setProjectId((Integer) map.get("projectId"));
            equipmentOffline.setTenantId((Integer) map.get("tenantId"));
            equipmentOffline.setMonitorTime(new Date());
            offlineService.save(equipmentOffline);
            MonitorPointAlert monitorPointAlert = new MonitorPointAlert();
            monitorPointAlert.setAlertType("1");
            monitorPointAlert.setContent(String.valueOf("塔机设备" + assets.getAssetName() + "已离线"));
            monitorPointAlert.setEquipmentId(assets.getAssetId());
            monitorPointAlert.setProjectId(assets.getProjectId());
            monitorPointAlert.setTenantId(assets.getTenantId());
            monitorPointAlert.setHandleState("0");
            monitorPointAlertService.save(monitorPointAlert);
            QueryWrapper<EquipmentDriverIdent> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("equ_code",assets.getAssetCode());
            queryWrapper.eq("project_id",map.get("projectId"));
            queryWrapper.eq("tenant_id",map.get("tenantId"));
            queryWrapper.eq("device_type","1");
            queryWrapper.isNull("ending_date");
            List<EquipmentDriverIdent> driverIdents = driverIdentService.list(queryWrapper);
            for(EquipmentDriverIdent driverIdent:driverIdents){
                driverIdent.setEndingDate(new Date());
            }
            if(driverIdents.size()>0){
                driverIdentService.updateBatchById(driverIdents);
            }
        }
        for(Map map:listElevator){
            QueryWrapper<EquipmentAssets> wrapper = new QueryWrapper<>();
            wrapper.eq("asset_id",map.get("equipmentId"));
            wrapper.eq("project_id",map.get("projectId"));
            wrapper.eq("tenant_id",map.get("tenantId"));
            EquipmentAssets assets = assetsService.getOne(wrapper);
            EquipmentOffline equipmentOffline = new EquipmentOffline();
            equipmentOffline.setEquipmentId((Integer) map.get("equipmentId"));
            equipmentOffline.setStatus(2);
            equipmentOffline.setProjectId((Integer) map.get("projectId"));
            equipmentOffline.setTenantId((Integer) map.get("tenantId"));
            equipmentOffline.setMonitorTime(new Date());
            offlineService.save(equipmentOffline);
            MonitorPointAlert monitorPointAlert = new MonitorPointAlert();
            monitorPointAlert.setAlertType("1");
            monitorPointAlert.setContent(String.valueOf("升降机设备" + assets.getAssetName() + "已离线"));
            monitorPointAlert.setEquipmentId(assets.getAssetId());
            monitorPointAlert.setProjectId(assets.getProjectId());
            monitorPointAlert.setTenantId(assets.getTenantId());
            monitorPointAlert.setHandleState("0");
            monitorPointAlertService.save(monitorPointAlert);
            QueryWrapper<EquipmentDriverIdent> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("equ_code",assets.getAssetCode());
            queryWrapper.eq("project_id",map.get("projectId"));
            queryWrapper.eq("tenant_id",map.get("tenantId"));
            queryWrapper.eq("device_type","2");
            queryWrapper.isNull("ending_date");
            List<EquipmentDriverIdent> driverIdents = driverIdentService.list(queryWrapper);
            for(EquipmentDriverIdent driverIdent:driverIdents){
                driverIdent.setEndingDate(new Date());
            }
            if(driverIdents.size()>0){
                driverIdentService.updateBatchById(driverIdents);
            }
        }
    }
}

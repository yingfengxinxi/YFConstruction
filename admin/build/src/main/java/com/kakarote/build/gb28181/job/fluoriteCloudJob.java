package com.kakarote.build.gb28181.job;/**
 * @ClassName accessTokenJob
 * @Author wnj58
 * @Date 2021/12/8 17:43
 * @purpose
 * @Version 1.0
 **/

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.api.service.SendProjectMsgService;
import com.kakarote.build.equipment.entity.PO.MonitorPointAlert;
import com.kakarote.build.equipment.mapper.EquipmentAssetsMapper;
import com.kakarote.build.equipment.service.impl.MonitorPointAlertServiceImpl;
import com.kakarote.build.gb28181.entity.PO.DeviceProjectRelation;
import com.kakarote.build.gb28181.mapper.DeviceProjectRelationMapper;
import com.kakarote.build.gb28181.service.YsyDockingService;
import com.kakarote.build.utils.HttpClientResult;
import com.kakarote.build.utils.HttpClientUtils;
import com.kakarote.build.utils.ListUtil;
import com.kakarote.build.utils.VideoOfflineUtil;
import com.kakarote.core.common.Result;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.message.MessageEntity;
import com.kakarote.core.message.MessageType;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName fluoriteCloudJob 萤石云job
 * @Author wnj58
 * @Date 2021/12/8 17:43
 * @purpose
 * @Version 1.0
 **/
@Component
@Slf4j
public class fluoriteCloudJob {

    @Value("${ysy.config.appKey}")
    private String appKey;

    @Value("${ysy.config.aecret}")
    private String aecret;

    @Value("${ysy.config.urlPrefix}")
    private String ysyUrlPrefix;

    @Autowired
    private MonitorPointAlertServiceImpl monitorPointAlertService;

    @Autowired
    private SendProjectMsgService sendProjectMsgService;


    @Autowired
    private AdminService adminService;

    @Autowired
    private YsyDockingService YsyDockingService;

    @Autowired
    private DeviceProjectRelationMapper DeviceProjectRelationMapper;

    @Autowired
    private EquipmentAssetsMapper equipmentAssetsMapper;

    //常量储存token
    private String YSToken = "-1";
    //token过期时间
    private long TokenExpireTime = 0;
    //Url过期时间
    private long UrlExpireTime = 62208000;
    //视频监控离线报警是否插入报警表
    private boolean VideoJobAlertFlag = true;

    //@PostConstruct
    private void initAll() {
        accessTokenGet();
        getAlertFlag();
        equipmentStatusGet();
    }

    /**
     * 定时更新token
     */
//    @Scheduled(cron = "0 0 0/1 * * ?")
//    @Scheduled(cron = "0 0 8 * * ?")==>改为xxl-job
    private String accessTokenGet() {
        try {
            boolean flag = false;//是否刷新token
            //验证accessToken是否过期
            long thisTime = System.currentTimeMillis();
            if (TokenExpireTime == 0) {//初始刷新
                flag = true;
            } else {
                if (TokenExpireTime - thisTime <= 7200000) {
                    flag = true;
                }
            }
            //开始请求
            if (flag) {
                log.info(">>>>>YingShi AccessToken is refreshing at " + new Date());
                //拼装请求头
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                //拼装请求参数
                Map params = new HashMap();
                params.put("appKey", appKey);
                params.put("appSecret", aecret);

                String url = ysyUrlPrefix + "/api/lapp/token/get";
                HttpClientResult result = null;

                result = HttpClientUtils.doPost2(url, headers, params);
                if (result.getCode() == 200) {
                    JSONObject jsonObject = result.getContent();
                    if ("200".equals(String.valueOf(jsonObject.get("code")))) {
                        JSONObject ysyResult = (JSONObject) jsonObject.get("data");
                        YSToken = String.valueOf(ysyResult.get("accessToken"));
                        TokenExpireTime = Long.parseLong(ysyResult.get("expireTime").toString());
                        //存储
                        AdminConfig adminConfig = new AdminConfig();
                        Result<List<AdminConfig>> Feignresult = adminService.queryConfigByNameAndTenantId("yinshiToken");
                        List<AdminConfig> adminConfigList = Feignresult.getData();
                        if (adminConfigList != null && adminConfigList.size() > 0) {
                            adminConfig = adminConfigList.get(0);
                            adminConfig.setValue(YSToken);
                            adminConfig.setDescription(String.valueOf(TokenExpireTime));
                            adminService.updateAdminConfigNoTenantId(adminConfig);
                        } else {
                        }
                    } else {
                        throw new CrmException(Integer.valueOf(String.valueOf(jsonObject.get("code"))), String.valueOf(jsonObject.get("msg")));
                    }
                } else {
                    throw new CrmException(result.getCode(), "YingShi AccessToken get failed at " + new Date());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return YSToken;
    }

    //@Scheduled(cron = "0 0 0/1 * * ?")
    public void getAlertFlag() {
        log.info(">>>>>YingShi VideoJobAlertFlag is refreshing at " + new Date());
        Result<List<AdminConfig>> Feignresult = adminService.queryConfigByNameAndTenantId("videoJobAlertInsert");
        if (Feignresult.hasSuccess()) {
            List<AdminConfig> adminConfigList = Feignresult.getData();
            if (adminConfigList != null && adminConfigList.size() != 0) {
                //多条配置数据，如果有一项为禁止报警，则其他数据无效 （0：禁用，1：启用，默认为1）
                List<AdminConfig> newList = adminConfigList.stream()
                        .filter(m -> m.getStatus() == 1 && "0".equals(m.getValue())).collect(Collectors.toList());
                if (newList.size() > 0) {
                    VideoJobAlertFlag = false;
                } else {
                    VideoJobAlertFlag = true;
                }
            }
        }
    }

    /**
     * 从萤石云获取设备状态
     * 启动项目执行一次，每90秒执行一次，启动时默认设备全部离线
     * i 启用线程数
     */
    //@XxlJob("getVideoEquipmentStatus")
    public void equipmentStatusGet() {
        Integer i = 10;
        String param = XxlJobHelper.getJobParam();
        if(!StringUtils.isEmpty(param)){
            JSONObject paramJson = JSONObject.parseObject(param);
            Integer time = paramJson.getInteger("time");
            if(time != null){
                i = time;
            }
        }
        //获取所有NVR设备序列号
        List<DeviceProjectRelation> videoDevice = DeviceProjectRelationMapper.getVideoDeviceAll();
        //分组
        List<List<DeviceProjectRelation>> videoDeviceGroup = ListUtil.averageAssign(videoDevice, i);
        log.info("videoDeviceGroup size =>{}", videoDeviceGroup.size());
        for (List<DeviceProjectRelation> oneGroup : videoDeviceGroup) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (DeviceProjectRelation device : oneGroup) {
                            center(device);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    //@Transactional(rollbackFor = Exception.class)  无需回滚
    public void center(DeviceProjectRelation device) throws Exception {
        long startTime = System.currentTimeMillis();
        HttpClientResult result;
        //拼装请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        //拼装请求参数
        Map params = new HashMap();
        String deviceSerial = device.getDeviceSerial();
        String proejectName = device.getProjectName();
        params.put("accessToken", accessTokenGet());
        params.put("deviceSerial", deviceSerial);
        //
        List<DeviceProjectRelation> channelListAll = new ArrayList<>();
        //开始请求 NVR 设备在线状态
        result = HttpClientUtils.doPost2(ysyUrlPrefix + "/api/lapp/device/info", headers, params);
        if (result.getCode() == 200) {
            JSONObject jsonObject = result.getContent();
            if ("200".equals(String.valueOf(jsonObject.get("code")))) {
                JSONObject ysyResult = (JSONObject) jsonObject.get("data");
                //NVR设备状态
                String nvrStatus = String.valueOf(ysyResult.get("status"));//status=1 在线
                if (!nvrStatus.equals(device.getNvrStatus())) {//NVR变动
                    device.setNvrStatusChange(true);
                    device.setNvrStatus(nvrStatus);
                }
                //NVR下所有IPC
                Map<String, Object> map = new HashMap<>();
                map.put("deviceSerial", deviceSerial);
                map.put("isRefreshIPC", "1");
                List<DeviceProjectRelation> channelList = DeviceProjectRelationMapper.videoMonitorDevices(map);
                if ("1".equals(nvrStatus)) {//在线
                    //最新状态
                    JSONArray channelInfoList = judgeCameraOnline(deviceSerial);
                    for (DeviceProjectRelation ipc : channelList) {
                        String oldStatus = ipc.getStatus();
                        //循环萤石云状态列表，更新本地设备状态
                        String newStatus = "";
                        for (int i = 0; i < channelInfoList.size(); i++) {
                            JSONObject channelInfo = (JSONObject) channelInfoList.get(i);
                            if (ipc.getDeviceChannelId().equals(String.valueOf(channelInfo.get("superDevChannel")))) {
                                newStatus = String.valueOf(channelInfo.get("status"));
                                break;
                            }
                        }
                        if (oldStatus.equals(newStatus)) {//未变动
                            if ("1".equals(newStatus)) {//在线记录更新时间
                                ipc.setCreateTime(new Date());
                            }
                        } else {
                            ipc.setCreateTime(new Date());//记录变动时间
                            ipc.setStatusChange(true);//变动记录标识
                        }
                        ipc.setStatus(newStatus);
                        ipc.setNvrStatus(nvrStatus);
                    }
                } else {//全部设置离线
                    for (DeviceProjectRelation ipc : channelList) {
                        String oldStatus = ipc.getStatus();
                        if ("1".equals(oldStatus)) {//原在线
                            ipc.setStatusChange(true);
                            ipc.setCreateTime(new Date());
                        }
                        ipc.setStatus("0");
                        ipc.setNvrStatus(nvrStatus);
                    }
                }
                //立即入库
                if (channelList.size() > 0) {
                    //更新设备通道号及播放地址信息，仅设备为启用时才更新，否则忽略更新
                    DeviceProjectRelationMapper.updateBatchById(channelList);
                    equipmentAssetsMapper.updateOnlineBatchById(channelList);
                }
                //报警
                alertCenter(device, channelList);
            } else {
                if ("20001".equals(String.valueOf(jsonObject.get("code")))) {
                    log.info(proejectName + "，监控设备：" + deviceSerial + "-->code: " + String.valueOf(jsonObject.get("code")) + ",mag: " + String.valueOf(jsonObject.get("msg")));
                } else if ("10002".equals(String.valueOf(jsonObject.get("code")))) {
                    log.info(proejectName + "，监控设备：" + deviceSerial + "-->code: " + String.valueOf(jsonObject.get("code")) + ",mag: " + String.valueOf(jsonObject.get("msg")));
                } else {
                    log.info(proejectName + "，监控设备：" + deviceSerial + "-->code: " + String.valueOf(jsonObject.get("code")) + ",mag: " + String.valueOf(jsonObject.get("msg")));
                }
            }
        } else {
            throw new CrmException(result.getCode(), "萤石云获取设备状态失败");
        }
        long endTime = System.currentTimeMillis();
        log.info(">>>>>YingShi EquipmentStatus[" + device.getProjectName() + "] finished in " + (endTime - startTime));
    }

    /**
     * 报警处置
     * 无关结果，开启线程处理
     */
    private void alertCenter(DeviceProjectRelation nvr, List<DeviceProjectRelation> ipcs) {
        try {
            //录像机报警
            if (nvr.getNvrStatusChange()) {
                //仅状态发生变化时才推送消息,且每个设备只发送一次
                String nvrContent = "";
                String messageCode = "";
                if ("1".equals(nvr.getNvrStatus())) {
                    nvrContent = "已上线";
                    messageCode = MessageType.MONITOR_EQUI_ONLINE;
                } else {
                    nvrContent = "已离线";
                    messageCode = MessageType.MONITOR_EQUI_OFFLINE;
                }
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setTitle(String.valueOf(nvr.getProjectName() + "监控设备" + nvrContent));
                messageEntity.setData(null);
                messageEntity.setCode(messageCode);
                messageEntity.setProjectId(1L);//获取到登录用户时可不填
                messageEntity.setTenantId(1);//获取到登录用户时可不填
                sendProjectMsgService.send(messageEntity, true);
            }
            //IPC报警
            if (VideoJobAlertFlag) {
                for (DeviceProjectRelation ipc : ipcs) {
                    if (ipc.getStatusChange()) {
                        handleAlertData(ipc, "1", "0", ipc.getStatus());
                    }
                }
            }
        } catch (Exception e) {
            log.error(">>>>>YingShi SendMessage[{}] error ->{}", nvr.getProjectName(), e.getMessage());
        }
    }

    /**
     * @return void
     * @throws
     * @description
     * @author wnj58
     * @Params [alaem 报警数据, alertType 报警类型, handleState 处理状态, stuts 设备在线状态]
     * @updateTime 2022/4/14 16:04
     */
    private void handleAlertData(DeviceProjectRelation alaem, String alertType, String handleState, String stuts) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("equipment_id", alaem.getBusinessId());
        queryWrapper.eq("handle_state", "0");
        List<MonitorPointAlert> list = monitorPointAlertService.list(queryWrapper);
        //如果设备在线，处理旧报警数据，判断之前是否存在离线数据，如果存在处理，否则跳过
        if ("1".equals(stuts)) {
            log.info("==旧报警数据处理");
            for (MonitorPointAlert mpa : list) {
                DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                mpa.setHandleTime(dft.format(new Date()));
                mpa.setHandleConclu("设备已恢复");
                mpa.setHandleState("1");
                mpa.setHandleMeasures("设备已上线");
                mpa.setHandleBy("admin");
                monitorPointAlertService.updateById(mpa);
            }
        } else {
//           如果设备离线，插入离线报警数据，判断之前是否存在离线报警数据，如果不存在插入，否则跳过
            if (list.size() == 0) {
                log.info("==没有未处理数据，保存报警数据");
                MonitorPointAlert monitorPointAlert = new MonitorPointAlert();
                monitorPointAlert.setAlertType(alertType);
                monitorPointAlert.setContent(String.valueOf(alaem.getAssetName() + "-- 已离线"));
                monitorPointAlert.setEquipmentId(alaem.getAssetId());
                monitorPointAlert.setProjectId(alaem.getProjectId());
                monitorPointAlert.setTenantId(alaem.getTenantId());
                monitorPointAlert.setHandleState(handleState);
                monitorPointAlertService.save(monitorPointAlert);
            } else {
                log.info("==已有未处理数据不报警");
            }
        }
    }

    /**
     * 刷新url
     */
    //@XxlJob("UrlRefresh")
    public void refreshVedioUrl() {
        long expireTime = UrlExpireTime;
        long startTime = System.currentTimeMillis();
        //拼装请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        //拼装请求参数
        Map params = new HashMap();
        params.put("accessToken", accessTokenGet());
        //查询所有需更新IPC
        Map<String, Object> map = new HashMap<>();
        map.put("isRefreshIPC", "1");
        //存放待更新数据
        List<Map> nvrList = handleNvrIpcData(DeviceProjectRelationMapper.videoMonitorDevices(map));
        for (Map nvr : nvrList) {
            HttpClientResult result;
            params.put("deviceSerial", nvr.get("deviceSerial"));
            JSONArray ysyIpcArray = null;
            try {
                //https://open.ys7.com/help/55#device_select-api6
                result = HttpClientUtils.doPost2(ysyUrlPrefix + "/api/lapp/device/camera/list", headers, params);
                if (result.getCode() == 200) {
                    JSONObject jsonObjectTemp = result.getContent();
                    if ("200".equals(String.valueOf(jsonObjectTemp.get("code")))) {
                        ysyIpcArray = (JSONArray) jsonObjectTemp.get("data");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<DeviceProjectRelation> ipcList = (List<DeviceProjectRelation>) nvr.get("ipcs");
            for (DeviceProjectRelation ipc : ipcList) {
                //从ipc列表中循环查找萤石云通道号DeviceChannelNo
                for (int i = 0; i < ysyIpcArray.size(); i++) {
                    JSONObject temp = (JSONObject) ysyIpcArray.get(i);
                    if (temp != null) {

                        //从萤石云获取的ipc序列号
                        String ipcSerial_cloud = String.valueOf(temp.get("ipcSerial"));
                        //本地注册的ipc序列号
                        String deviceChannelId = String.valueOf(ipc.getDeviceChannelId());
                        String ipcSerial_local = deviceChannelId.substring(deviceChannelId.length() - 9, deviceChannelId.length());
                        if (ipcSerial_cloud.equals(ipcSerial_local)) {
                            ipc.setDeviceChannelNo(String.valueOf(temp.get("channelNo")));
                            break;
                        }
                    }
                }
                //获取设备ezopen协议播放地址
                params.put("protocol", "1");//流播放协议，1-ezopen、2-hls、3-rtmp、4-flv，默认为1，
                params.put("quality", "2");//视频清晰度，1-高清（主码流）、2-流畅（子码流）
                params.put("type", "1");//ezopen协议地址的类型，1-预览，2-本地录像回放，3-云存储录像回放，默认为1，
                params.put("gbchannel", ipc.getDeviceChannelId());//国标设备的通道编号
                try {
                    result = HttpClientUtils.doPost2(ysyUrlPrefix + "/api/lapp/v2/live/address/get", headers, params);
                    if (result.getCode() == 200) {
                        JSONObject jsonObjectTemp = result.getContent();
                        if ("200".equals(String.valueOf(jsonObjectTemp.get("code")))) {
                            JSONObject ysyResultTemp = (JSONObject) jsonObjectTemp.get("data");
                            String ezopenUrl = String.valueOf(ysyResultTemp.get("url"));
                            ipc.setUrl(ezopenUrl);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //获取设备萤石云 的http://协议播放地址
                params.put("expireTime", expireTime);
                params.put("protocol", "4");//流播放协议，1-ezopen、2-hls、3-rtmp、4-flv，默认为1，
                params.put("quality", "2");//视频清晰度，1-高清（主码流）、2-流畅（子码流）

                try {
                    result = HttpClientUtils.doPost2(ysyUrlPrefix + "/api/lapp/v2/live/address/get", headers, params);
                    if (result.getCode() == 200) {
                        JSONObject jsonObjectTemp = result.getContent();
                        if ("200".equals(String.valueOf(jsonObjectTemp.get("code")))) {
                            JSONObject ysyResultTemp = (JSONObject) jsonObjectTemp.get("data");
                            String ysyHttpUrl = String.valueOf(ysyResultTemp.get("url"));
                            ipc.setYsyFlvUrl(ysyHttpUrl);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //获取萤石云m3u8格式的视频流地址
                params.put("expireTime", expireTime);
                params.put("protocol", "2");//流播放协议，1-ezopen、2-hls、3-rtmp、4-flv，默认为1，
                params.put("quality", "2");//视频清晰度，1-高清（主码流）、2-流畅（子码流）
                try {
                    result = HttpClientUtils.doPost2(ysyUrlPrefix + "/api/lapp/v2/live/address/get", headers, params);
                    if (result.getCode() == 200) {
                        JSONObject jsonObjectTemp = result.getContent();
                        if ("200".equals(String.valueOf(jsonObjectTemp.get("code")))) {
                            JSONObject ysyResultTemp = (JSONObject) jsonObjectTemp.get("data");
                            String ysyHttpUrl = String.valueOf(ysyResultTemp.get("url"));
                            ipc.setWvpOpenUrl(ysyHttpUrl);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //
                ipc.setUrlRetime(new Date());
            }
            if (ipcList.size() > 0) {
                try {
                    DeviceProjectRelationMapper.updateBatchById(ipcList);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        long endTime = System.currentTimeMillis();
        log.info(">>>>>YingShi WebUrl finished in " + (endTime - startTime));
    }

    /**
     * 加工ipc数据
     * Map:
     * nvr:
     * ips:
     */
    private List<Map> handleNvrIpcData(List<DeviceProjectRelation> ipcList) {
        List<Map> nvrList = new ArrayList<>();
        //记录已处理NVR标识
        Map<String, String> nvrIds = new HashMap();
        for (DeviceProjectRelation ipc : ipcList) {
            String deviceSerial = ipc.getDeviceSerial();
            if ("1".equals(nvrIds.get(deviceSerial))) {
                continue;
            }
            Map nvr = new HashMap();
            nvr.put("deviceSerial", deviceSerial);
            nvr.put("nvr", ipc);
            List<DeviceProjectRelation> ipcs = new ArrayList<>();
            for (DeviceProjectRelation one : ipcList) {
                if (one.getDeviceSerial().equals(deviceSerial)) {
                    ipcs.add(one);
                }
            }
            nvr.put("ipcs", ipcs);
            nvrList.add(nvr);
            nvrIds.put(deviceSerial, "1");
        }
        return nvrList;
    }

    /**
     * @param
     * @return
     * @Description:摄像头在线转态更新
     * @author shz
     * @date 2022-10-14 16:19
     */
    //@XxlJob("offlineUpdates")
    //@Transactional(rollbackFor = Exception.class)
    public void offlineUpdates() {
        log.info("执行开始:" + (new Date()));
        List<Integer> list = DeviceProjectRelationMapper.getProject();
        for (Integer id : list) {
            YsyDockingService.runOfflineUpdates(id);
        }
        log.info("执行结束:" + (new Date()));
    }

    /**
     * @param
     * @return
     * @Description:传入萤石云设备序列号，判断录像机下摄像头状态
     * @author shz
     * @date 2023-1-6 11:33
     */
    public JSONArray judgeCameraOnline(String deviceSerial) {
        JSONArray list = new JSONArray();
        List<DeviceProjectRelation> deviceList = DeviceProjectRelationMapper.getVideoDeviceByDeviceSerial(deviceSerial);
        for (DeviceProjectRelation device : deviceList) {
            boolean bo = VideoOfflineUtil.openMedia(device.getWvpOpenUrl());
            JSONObject json = new JSONObject();
            json.put("superDevChannel", device.getDeviceChannelId());
            if (bo) {
                json.put("status", 1);
            } else {
                json.put("status", 0);
            }
            list.add(json);
        }
        return list;
    }


    public static void main(String[] args) {
        Map<String, String> headers_get = new HashMap<>();
        headers_get.put("Content-Type", "application/x-www-form-urlencoded");
        headers_get.put("accessToken", "at.8nmj7sfbc4rlkeowbnj6vgh73pfmcane-7p2kdehc14-1t4fllj-k9a1h8a1k");
        headers_get.put("deviceSerial", "33011081992397348766:33011036991117755118");

        try {
            HttpClientResult result = HttpClientUtils.doGet2("https://open.ys7.com/api/v3/open/device/metadata/channel/status", headers_get);
            if (result.getCode() == 200) {
                JSONObject jsonObjectTemp = result.getContent();
                System.out.println(jsonObjectTemp);
                JSONObject jsonObjectTemp2 = jsonObjectTemp.getJSONObject("result");
                System.out.println(jsonObjectTemp2);
                if ("200".equals(String.valueOf(jsonObjectTemp2.get("code")))) {
                    JSONObject ysyResultTemp = (JSONObject) jsonObjectTemp2.get("data");
                    JSONArray channelInfoList = ysyResultTemp.getJSONArray("channelInfoList");
                    System.out.println(channelInfoList);
                    for (int i = 0; i < channelInfoList.size(); i++) {
                        System.out.println(channelInfoList.get(i));
                        JSONObject channelInfo = (JSONObject) channelInfoList.get(i);
                        if ("1".equals(String.valueOf(channelInfo.get("superDevChannel")))) {
                            System.out.println(String.valueOf(channelInfo.get("status")));
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.qdoner.datarecept.job;/**
 * @ClassName accessTokenJob
 * @Author wnj58
 * @Date 2021/12/8 17:43
 * @purpose
 * @Version 1.0
 **/

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.core.common.Result;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.message.MessageEntity;
import com.kakarote.core.message.MessageType;
import com.qdoner.datarecept.entity.PO.BMonitorPointAlert;
import com.qdoner.datarecept.entity.PO.DeviceProjectRelation;
import com.qdoner.datarecept.mapper.BEquipmentAssetsMapper;
import com.qdoner.datarecept.mapper.DeviceProjectRelationMapper;
import com.qdoner.datarecept.service.EqStatusCommonService;
import com.qdoner.datarecept.service.IBMonitorPointAlertService;
import com.qdoner.datarecept.service.SendProjectMsgService;
import com.qdoner.datarecept.service.YsyDockingService;
import com.qdoner.datarecept.utils.HttpClientResult;
import com.qdoner.datarecept.utils.HttpClientUtils;
import com.qdoner.datarecept.utils.ListUtil;
import com.qdoner.datarecept.utils.VideoOfflineUtil;
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
public class FluoriteCloudJob {

    @Value("${ysy.config.appKey}")
    private String appKey;

    @Value("${ysy.config.aecret}")
    private String aecret;

    @Value("${ysy.config.urlPrefix}")
    private String ysyUrlPrefix;

    @Autowired
    private AdminService adminService;

    @Autowired
    private YsyDockingService YsyDockingService;

    @Autowired
    private DeviceProjectRelationMapper DeviceProjectRelationMapper;

    @Autowired
    private EqStatusCommonService statusCommonService;

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
        //equipmentStatusGet();
        //refreshVedioUrl();
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

//    @Scheduled(cron = "0 0 0/1 * * ?")
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
     * 获取 是否离线报警
     *
     * @return
     */
    public boolean getVideoJobAlertFlag() {
        return VideoJobAlertFlag;
    }

    /**
     * 从萤石云获取设备状态
     * 启动项目执行一次，每90秒执行一次，启动时默认设备全部离线
     * i 启用线程数
     */
    @XxlJob("getVideoEquipmentStatus")
    public void equipmentStatusGet() {
        Integer i = 40;
        String param = XxlJobHelper.getJobParam();
        if (!StringUtils.isEmpty(param)) {
            JSONObject paramJson = JSONObject.parseObject(param);
            Integer time = paramJson.getInteger("time");
            if (time != null) {
                i = time;
            }
        }
        //获取所有NVR设备序列号
        List<DeviceProjectRelation> videoDevice = DeviceProjectRelationMapper.getVideoDeviceAll(null);
        //分组
        List<List<DeviceProjectRelation>> videoDeviceGroup = ListUtil.averageAssign(videoDevice, i);
        log.info("videoDeviceGroup size =>{}", videoDeviceGroup.size());
        for (List<DeviceProjectRelation> oneGroup : videoDeviceGroup) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        long startTime = System.currentTimeMillis();
                        for (DeviceProjectRelation device : oneGroup) {
                            center(device);
                        }
                        long endTime = System.currentTimeMillis();
                        log.info(">>>>>YingShi EquipmentStatus Thread finished in " + (endTime - startTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    //@Transactional(rollbackFor = Exception.class)  无需回滚
    public void center(DeviceProjectRelation device) throws Exception {
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
                    //2023-02-16 获取最新状态前 需核查nvr在线,但ipc视频流地址为空的设备
                    for (DeviceProjectRelation ipc : channelList) {
                        if (StringUtils.isEmpty(ipc.getWvpOpenUrl())
                                || !ipc.getWvpOpenUrl().startsWith("http")
                                || StringUtils.isEmpty(ipc.getYsyFlvUrl())
                                || !ipc.getYsyFlvUrl().startsWith("http")
                                || StringUtils.isEmpty(ipc.getUrl())
                                || !ipc.getYsyFlvUrl().startsWith("ezopen:")
                                ) {
                            this.refreshNvrUrl(deviceSerial);
                            break;//刷新一次即可
                        }
                    }
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
                }
                //状态更新
                alertCenter(channelList);
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
    }

    /**
     * 报警处置
     * 无关结果，开启线程处理
     */
    private void alertCenter(List<DeviceProjectRelation> ipcs) {
        for (DeviceProjectRelation ipc : ipcs) {
            statusCommonService.handleOfflineSatus(Integer.parseInt(ipc.getBusinessId()), ipc.getStatus());
        }
    }

    /**
     * 刷新全部url
     */
    @XxlJob("UrlRefresh")
    public void refreshVedioUrl() {
        long startTime = System.currentTimeMillis();
        //查询所有需更新IPC
        Map<String, Object> map = new HashMap<>();
        map.put("isRefreshIPC", "1");
        map.put("isGroup", "1");
        //存放待更新数据
        List<DeviceProjectRelation> nvrList = DeviceProjectRelationMapper.videoMonitorDevices(map);
        for (DeviceProjectRelation nvr : nvrList) {
            refreshNvrUrl(nvr.getDeviceSerial());
        }
        long endTime = System.currentTimeMillis();
        log.info(">>>>>YingShi WebUrl finished in " + (endTime - startTime));
    }

    /**
     * 刷新nvr下ipc的url
     *
     * @param deviceSerial
     */
    private void refreshNvrUrl(String deviceSerial) {
        long expireTime = UrlExpireTime;
        //拼装请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        //拼装请求参数
        Map params = new HashMap();
        params.put("accessToken", accessTokenGet());
        //查询所有需更新IPC
        Map<String, Object> map = new HashMap<>();
        map.put("isRefreshIPC", "1");
        map.put("deviceSerial", deviceSerial);
        //存放待更新数据
        List<DeviceProjectRelation> ipcList = DeviceProjectRelationMapper.videoMonitorDevices(map);
        HttpClientResult result;
        //nvr
        params.put("deviceSerial", deviceSerial);
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
        //ipcs
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
                    } else {
                        ipc.setUrl(jsonObjectTemp.getString("msg"));
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
                    } else {
                        ipc.setYsyFlvUrl(jsonObjectTemp.getString("msg"));
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
                    } else {
                        ipc.setWvpOpenUrl(jsonObjectTemp.getString("msg"));
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


    /**
     * @param
     * @return
     * @Description:摄像头在线转态更新
     * @author shz
     * @date 2022-10-14 16:19
     */
    //@XxlJob("offlineUpdates")
    @Transactional(rollbackFor = Exception.class)
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
//            boolean bo = VideoOfflineUtil.checkVideoOnline(device.getWvpOpenUrl());// TODO 新读流方式待测试
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

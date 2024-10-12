package com.kakarote.build.gb28181.service.impl;/**
 * @ClassName ysyDockingServiceImpl
 * @Author wnj58
 * @Date 2021/12/6 18:17
 * @purpose
 * @Version 1.0
 **/

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.equipment.entity.PO.BEquipmentCertificate;
import com.kakarote.build.equipment.entity.PO.EquipmentAssets;
import com.kakarote.build.equipment.mapper.BEquipmentCertificateMapper;
import com.kakarote.build.equipment.mapper.EquipmentAssetsMapper;
import com.kakarote.build.equipment.service.IBEquipmentCertificateService;
import com.kakarote.build.equipment.service.IEquipmentAssetsService;
import com.kakarote.build.gb28181.entity.PO.DeviceProjectRelation;
import com.kakarote.build.gb28181.mapper.DeviceProjectRelationMapper;
import com.kakarote.build.gb28181.service.YsyDockingService;
import com.kakarote.build.utils.DateUtil;
import com.kakarote.build.utils.HttpClientResult;
import com.kakarote.build.utils.HttpClientUtils;
import com.kakarote.build.utils.VideoOfflineUtil;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @ClassName ysyDockingServiceImpl
 * @Author wnj58
 * @Date 2021/12/6 18:17
 * @purpose
 * @Version 1.0
 **/
@Service
@Slf4j
public class YsyDockingServiceImpl extends BaseServiceImpl<DeviceProjectRelationMapper, DeviceProjectRelation>  implements YsyDockingService {

    @Value("${wvp.config.urlPrefix}")
    private String wvpUrlPrefix;

    @Value("${ysy.config.appKey}")
    private String appKey;

    @Value("${ysy.config.aecret}")
    private String aecret;

    @Value("${ysy.config.urlPrefix}")
    private String ysyUrlPrefix;

    @Value("${ysy.config.accessToken}")
    private String accessToken;


    @Autowired
    private AdminService adminService;
    @Autowired
    private EquipmentAssetsMapper equipmentAssetsMapper;
    @Autowired
    private IEquipmentAssetsService equipmentAssetsService;

    @Override
    public Result playback(Map<String, Object> params) {
        String url = "/api/lapp/v2/live/address/get";
        Result result = ysyPostInit(params, url);
        return result;
    }

    @Override
    public Result getAccessToken(Map<String, Object> map) {
        Map params = new HashMap();
        try {
            AdminConfig adminConfig = null;
            boolean flag = false;
            //获取token并验证是否过期
            Result<AdminConfig> Feignresult = adminService.queryFirstConfigByName("yinshiToken");
            if (Feignresult.hasSuccess()) {
                adminConfig = Feignresult.getData();
                if (adminConfig.getValue() != null) {
                    long expireTime = Long.valueOf(String.valueOf(adminConfig.getDescription())).longValue();
                    long thisTime = System.currentTimeMillis();
                    //如果差值不小于0 则有效
                    if (expireTime - thisTime > 0) {
                        params.put("accessToken", adminConfig.getValue());
                        flag = false;
                    } else {
                        flag = true;
                    }
                } else {
                    flag = true;
                }

            } else {
                flag = true;
            }

            if (flag) {
                log.info("==本地萤石云AccessToken已过期，重新获取");
                //拼装请求头
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                //拼装请求参数
                Map param = new HashMap();
                param.put("appKey", appKey);
                param.put("appSecret", aecret);

                String url = ysyUrlPrefix + "/api/lapp/token/get";
                HttpClientResult result = null;

                result = HttpClientUtils.doPost2(url, headers, param);
                if (result.getCode() == 200) {
                    JSONObject jsonObject = result.getContent();
                    if ("200".equals(String.valueOf(jsonObject.get("code")))) {
                        JSONObject ysyResult = (JSONObject) jsonObject.get("data");
                        String token = String.valueOf(ysyResult.get("accessToken"));
                        String expireTime = String.valueOf(ysyResult.get("expireTime"));
                        //存储
                        adminConfig.setValue(token);
                        params.put("accessToken", adminConfig.getValue());
                        adminConfig.setDescription(expireTime);
                        adminService.updateAdminConfigNoTenantId(adminConfig);
                    } else {
                        throw new CrmException(Integer.valueOf(String.valueOf(jsonObject.get("code"))), String.valueOf(jsonObject.get("msg")));
                    }
                } else {
                    throw new CrmException(result.getCode(), "萤石云获取AccessToken失败");
                }
            } else {
                System.out.println("token未过期");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(999, "系统内部错误，accessToken获取失败");
        }
        return R.ok(params);
    }

    @Override
    public Result ptzControl(Map<String, Object> params) {

        try{
            if (params == null) {
                throw new CrmException(400, "参数不能为空");
            }
            if (!str(String.valueOf(params.get("deviceId")))) {
                throw new CrmException(400, "设备id不能为空");
            }
            if (!str(String.valueOf(params.get("channelId")))) {
                throw new CrmException(400, "通道id不能为空");
            }
            if (!str(String.valueOf(params.get("type")))) {
                throw new CrmException(400, "控制指令类型不能为空");
            }
            if (!str(String.valueOf(params.get("speed")))) {
                throw new CrmException(400, "控制速度不能为空");
            }
            List<Map<String,Object>> isAllowControl = equipmentAssetsMapper.getIsAllowControl(params);
            if (isAllowControl.size() >= 1) {
                Map temp=isAllowControl.get(0);
                String deviceSerial = String.valueOf(temp.get("deviceSerial"));
                String channelNo = String.valueOf(temp.get("deviceChannelNo"));
                String direction = "";
                String urlType = "";
                switch (String.valueOf(params.get("type"))) {
                    case "left":
                        direction = "2";
                        urlType = "start";
                        break;
                    case "right":
                        direction = "3";
                        urlType = "start";
                        break;
                    case "up":
                        direction = "0";
                        urlType = "start";
                        break;
                    case "down":
                        direction = "1";
                        urlType = "start";
                        break;
                    case "upleft":
                        direction = "4";
                        urlType = "start";
                        break;
                    case "upright":
                        direction = "6";
                        urlType = "start";
                        break;
                    case "downleft":
                        direction = "5";
                        urlType = "start";
                        break;
                    case "downright":
                        direction = "7";
                        urlType = "start";
                        break;
                    case "zoomin":
                        direction = "8";
                        urlType = "start";
                        break;
                    case "zoomout":
                        direction = "9";
                        urlType = "start";
                        break;
                    case "focalfar":
                        direction = "11";
                        urlType = "start";
                        break;
                    case "focalnear":
                        direction = "10";
                        urlType = "start";
                        break;
                    case "stop":
                        direction = "";
                        urlType = "stop";
                        break;
                    default:
                        break;
                }
                String speedStr=String.valueOf(params.get("speed"));
                Integer speed= StringUtils.isNumeric(speedStr)?Integer.valueOf(speedStr)>150?2:1:1;

                Map<String,Object> map=new HashMap<>();
                map.put("deviceSerial",deviceSerial);
                map.put("channelNo",channelNo);

               if(!"stop".equals(String.valueOf(params.get("type")))){
                   map.put("direction",direction);
                   map.put("speed",speed);
               }
                String url = "/api/lapp/device/ptz/" + urlType;
                Result result = ysyPostInit(map, url);
                return result;
            } else {
                return R.error(412, "设备不支持云台控制");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return R.error(999, "未知错误");
    }

    @Override
    public Result wvpPtzControl(Map<String, Object> map) {
        if (map == null) {
            throw new CrmException(400, "参数不能为空");
        }
        if (!str(String.valueOf(map.get("deviceId")))) {
            throw new CrmException(400, "设备id不能为空");
        }
        if (!str(String.valueOf(map.get("channelId")))) {
            throw new CrmException(400, "通道id不能为空不能为空");
        }
        if (!str(String.valueOf(map.get("type")))) {
            throw new CrmException(400, "控制指令类型不能为空");
        }
        if (!str(String.valueOf(map.get("speed")))) {
            throw new CrmException(400, "控制速度不能为空");
        }
        List<Map<String,Object>> isAllowControl = equipmentAssetsMapper.getIsAllowControl(map);
        if (isAllowControl.size() >= 1) {
            String deviceId = String.valueOf(map.get("deviceId"));
            String channelId = String.valueOf(map.get("channelId"));
            String command = String.valueOf(map.get("type"));
            String controSpeed = String.valueOf(map.get("speed"));
            String wvpPtzUrl = wvpUrlPrefix + "api/ptz/control/" + deviceId + "/" + channelId +
                    "?command=" + command + "&horizonSpeed=" + controSpeed + "&verticalSpeed=" + controSpeed + "&zoomSpeed=" + controSpeed;

            //拼装请求头
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            //拼装请求参数
            Map<String, Object> params = new HashMap<>();
            HttpClientResult result = null;
            try {
                result = HttpClientUtils.doPost3(wvpPtzUrl, headers, params);
                if (200 == result.getCode()) {
                    String str = result.getStringContent();
                    if ("success".equals(str)) {
                        return R.ok(str);
                    } else {
                        return R.error(400, "");
                    }
                } else {
                    return R.error(result.getCode(), "无效请求");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return R.error(412, "设备不支持云台控制");
        }

        return R.error(999, "未知错误");
    }

    @Override
    public Result ysyPostInit(Map<String, Object> params, String URL) {
        String url = ysyUrlPrefix + URL;
        HttpClientResult result = null;
        AdminConfig adminConfig=null;
        try {
            //拼装请求头
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            //拼装请求参数
            Result<List<AdminConfig>> Feignresult = adminService.queryConfigByNameAndTenantId("yinshiToken");
            if (Feignresult.hasSuccess()) {
                List<AdminConfig> adminConfigList = Feignresult.getData();
                if (adminConfigList != null && adminConfigList.size() != 0) {
                    //对accessToken过期时间倒序，取最新的一条
                    Collections.sort(adminConfigList, Comparator.comparing(AdminConfig :: getDescription).reversed());
                    adminConfig = adminConfigList.get(0);
                    if (adminConfig.getValue() != null) {
                        params.put("accessToken", adminConfig.getValue());
                    }
                }
            }
            result = HttpClientUtils.doPost2(url, headers, params);
            if (200 == result.getCode()) {
                JSONObject jsonObject = result.getContent();
                if ("200".equals(jsonObject.get("code"))) {
                    return R.ok(jsonObject.get("data"));
                } else {
                    return R.error(Integer.valueOf(String.valueOf(jsonObject.get("code"))), String.valueOf(jsonObject.get("msg")));
                }
            } else {
                return R.error(result.getCode(), "目的地不可达");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CrmException(99, "系统内部错误");
        }
    }

    @Override
    public Result ysyPostInit2(Map<String, Object> params, String URL) {
        String url = ysyUrlPrefix + URL;
        HttpClientResult result = null;
        try {
            //拼装请求头
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            //拼装请求参数


            result = HttpClientUtils.doPost2(url, headers, params);
            if (200 == result.getCode()) {
                JSONObject jsonObject = result.getContent();
                if ("200".equals(jsonObject.get("code"))) {
                    return R.ok(jsonObject.get("data"));
                } else {
                    return R.error(Integer.valueOf(String.valueOf(jsonObject.get("code"))), String.valueOf(jsonObject.get("msg")));
                }
            } else {
                return R.error(result.getCode(), "目的地不可达");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CrmException(99, "系统内部错误");
        }
    }

    @Autowired
    private IEquipmentAssetsService iEquipmentAssetsService;

    @Override
    public List<String> wvpOpenUrl(Map<String, Object> map) {
        List<Map<String, Object>> equimentList = iEquipmentAssetsService.equimentList(map);
        List<String> openUrl = new ArrayList<>();
        for (Map<String, Object> equi : equimentList) {
            String deviceChannelId = String.valueOf(equi.get("deviceChannelId"));
            String url = "http://47.94.169.80:80/rtp/" + deviceChannelId + "_" + deviceChannelId + "/hls.m3u8";
            openUrl.add(url);
        }
        return openUrl;
    }

    private static boolean str(String str) {
        return str != null && str != "" && str.length() != 0 && !"null".equals(str) && !"undefined".equals(str);
    }

    @Async
    public void runOfflineUpdates(Integer id){
        List<DeviceProjectRelation> videoDevice = getBaseMapper().getVideoByProject(id);
        for(DeviceProjectRelation relation:videoDevice){
            if(relation.getIsEnable() != null &&"1".equals(relation.getIsEnable())){
                if(VideoOfflineUtil.openMedia(relation.getWvpOpenUrl())){
                    relation.setStatus("1");
                    EquipmentAssets assets = new EquipmentAssets();
                    assets.setAssetId(Integer.valueOf(relation.getBusinessId()));
                    assets.setOfflineDate(new Date());
                    assets.setOfflineStatus("1");
                    QueryWrapper<EquipmentAssets> wrapper = new QueryWrapper<>();
                    wrapper.eq("asset_id",relation.getBusinessId());
                    wrapper.eq("tenant_id",relation.getTenantId());
                    wrapper.eq("project_id",relation.getProjectId());
                    equipmentAssetsService.update(assets,wrapper);
                }else {
                    EquipmentAssets assets = new EquipmentAssets();
                    assets.setAssetId(Integer.valueOf(relation.getBusinessId()));
                    assets.setOfflineDate(new Date());
                    assets.setOfflineStatus("0");
                    QueryWrapper<EquipmentAssets> wrapper = new QueryWrapper<>();
                    wrapper.eq("asset_id",relation.getBusinessId());
                    wrapper.eq("tenant_id",relation.getTenantId());
                    wrapper.eq("project_id",relation.getProjectId());
                    equipmentAssetsService.update(assets,wrapper);
                    relation.setStatus("0");
                }
            }else if("0".equals(relation.getIsEnable())){
                relation.setStatus("0");
            }else {
                relation.setStatus("0");
            }
        }
        int updateNum = getBaseMapper().updateBatchById(videoDevice);
        if (updateNum != 1) {
            throw new CrmException(500, "设备信息批量更新失败");
        }
    }
}

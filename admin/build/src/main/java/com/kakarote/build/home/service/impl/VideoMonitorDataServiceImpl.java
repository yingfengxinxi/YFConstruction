package com.kakarote.build.home.service.impl;/**
 * @ClassName videoMonitorServiceImpl
 * @Author wnj58
 * @Date 2021/9/24 16:30
 * @purpose
 * @Version 1.0
 **/

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kakarote.build.equipment.entity.BO.EquipmentAssetsBO;
import com.kakarote.build.gb28181.entity.PO.DeviceProjectRelation;
import com.kakarote.build.gb28181.mapper.DeviceProjectRelationMapper;
import com.kakarote.build.gb28181.service.YsyDockingService;
import com.kakarote.build.home.service.VideoMonitorDataService;
import com.kakarote.build.equipment.service.IEquipmentAssetsService;
import com.kakarote.build.utils.HttpClientResult;
import com.kakarote.build.utils.HttpClientUtils;
import com.kakarote.build.utils.VideoOfflineUtil;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.*;

/**
 *@ClassName videoMonitorServiceImpl
 *@Author wnj58
 *@Date 2021/9/24 16:30
 *@purpose
 *@Version 1.0
 **/
@Service
@Slf4j
public class VideoMonitorDataServiceImpl implements VideoMonitorDataService {

    //设备资产台账
    @Autowired
    private IEquipmentAssetsService iEquipmentAssetsService;

    @Autowired
    private YsyDockingService ysyDockingService;
    @Autowired
    private DeviceProjectRelationMapper deviceProjectRelationMapper;
    @Autowired
    private AdminService adminService;

    @Value("${ysy.config.appKey}")
    private String appKey;

    @Value("${ysy.config.aecret}")
    private String aecret;

    @Value("${ysy.config.urlPrefix}")
    private String ysyUrlPrefix;

    @Override
    public List<Map<String, Object>> videoEquiByPro( Map<String,Object> map) {
//        Map<String,Object> map=new HashMap<>();
//        map.put("online","1"); map.put("typeId","30");
        List<Map<String,Object>> list= iEquipmentAssetsService.videoEquiByPro(map);

        for(Map<String,Object> map2:list){
//            List<String> listStr=new ArrayList<>();
            //判断设备总数大于0才去查询设备信息
            if(Integer.valueOf(String.valueOf(map2.get("totalNum")))>0){
                //查询项目下的设备列表
                EquipmentAssetsBO eABO=new EquipmentAssetsBO();
                eABO.setProjectId(Integer.valueOf(String.valueOf(map2.get("projectId"))));
                List<Map<String,Object>> equimentList= iEquipmentAssetsService.groupList2(eABO);
                map2.put("equiList",equimentList);
                //循环查询设备播放地址
//                for(Map<String,Object> map3:equimentList){
//                    Map<String,Object> param=new HashMap<>();
//                    param.put("deviceSerial","33011081992397348766:33011036991117755118");//直播源，均采用英文符号，限制50个
//                    param.put("protocol",1);//流播放协议，1-ezopen、2-hls、3-rtmp、4-flv，默认为1
//                    param.put("quality",2);//视频清晰度，1-高清（主码流）、2-流畅（子码流）
//                    param.put("type","1");//1-预览，2-本地录像回放，3-云存储录像回放，非必选，默认为1
//                    param.put("gbchannel",String.valueOf(map3.get("deviceChannelId")));//通道id
//
//                    Result result= ysyDockingService.playback(param);
//                    if(result.getCode()==0){
//                       Map<String,Object> ss=(Map)result.getData();
//                        listStr.add(String.valueOf(ss.get("url")));
//                    }else{
//                        listStr.add("");
//                    }
//                    listStr.add(String.valueOf(map3.get("url")));
//                }
//                map2.put("playUrl",listStr);
//                map2.put("onLineNum",listStr.size());
            }else{
                map2.put("equiList",new ArrayList<>());
//                map2.put("playUrl",listStr);
//                map2.put("onLineNum",listStr.size());
            }
        }

        return list;
    }

    @Override
    public List<Map<String, Object>> videoEquiByProByCheckHox( Map<String,Object> map) {
//        Map<String,Object> map=new HashMap<>();
//        map.put("online","1"); map.put("typeId","30");
        List<Map<String,Object>> list= iEquipmentAssetsService.videoEquiByProByCheckHox(map);

        for(Map<String,Object> map2:list){
            List<String> listStr=new ArrayList<>();
            //判断设备总数大于0才去查询设备信息
            if(Integer.valueOf(String.valueOf(map2.get("totalNum")))>0){
                //查询项目下的设备列表
                List<Map<String,Object>> equimentList= iEquipmentAssetsService.equimentListByCheckHox(map2);
                //循环查询设备播放地址
                for(Map<String,Object> map3:equimentList){
//                    Map<String,Object> param=new HashMap<>();
//                    param.put("deviceSerial","33011081992397348766:33011036991117755118");//直播源，均采用英文符号，限制50个
//                    param.put("protocol",1);//流播放协议，1-ezopen、2-hls、3-rtmp、4-flv，默认为1
//                    param.put("quality",2);//视频清晰度，1-高清（主码流）、2-流畅（子码流）
//                    param.put("type","1");//1-预览，2-本地录像回放，3-云存储录像回放，非必选，默认为1
//                    param.put("gbchannel",String.valueOf(map3.get("deviceChannelId")));//通道id
//
//                    Result result= ysyDockingService.playback(param);
//                    if(result.getCode()==0){
//                       Map<String,Object> ss=(Map)result.getData();
//                        listStr.add(String.valueOf(ss.get("url")));
//                    }else{
//                        listStr.add("");
//                    }
                    listStr.add(String.valueOf(map3.get("url")));
                }
                map2.put("playUrl",listStr);
//                map2.put("onLineNum",listStr.size());
            }else{
                map2.put("playUrl",listStr);
//                map2.put("onLineNum",listStr.size());
            }
        }

        return list;
    }


    @Override
    public  List<Map<String, Object>> videoEquiByLocation(Map<String, Object> map) {
        return iEquipmentAssetsService.videoEquiByLocation(map);
    }

    @Override
    public List<Map<String, Object>> videoStreamList(Map<String, Object> map) {
        map.put("status","1");//设备在线状态
        map.put("projectState","2");//项目在建状态
        map.put("limit",4);//取前四个在线设备的播放url
        return iEquipmentAssetsService.videoStreamList(map);
    }

    @Override
    public Map<String, Object> selectVideoNumber() {
        Map<String, Object> map = iEquipmentAssetsService.selectVideoNumber();

        // 计算
        // 总数
        Integer count = Integer.valueOf(String.valueOf(map.get("totalNumber")));
        // 未完成数
        Integer onlineNumber = Integer.valueOf(String.valueOf(map.get("onlineNumber")));

        // 计算百分比
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(0);
        if (onlineNumber == 0) { // 除数为0，百分比直接为100%
            map.put("percent", "0%");
        } else {
            double retio = onlineNumber * 1.0 / count;
            String percent = format.format(retio);
            map.put("percent", percent);
        }

        return map;
    }

    @Override
    public List<Map<String, Object>> selectVideoList(Map<String, Object> params) {
        List<Map<String, Object>> list = iEquipmentAssetsService.selectVideoList(params);
        return list;
    }

    @Override
    public BasePage<Map<String,Object>> selectVideoOnlineList(Map<String, Object> param) {
        BasePage<Map<String,Object>> list = iEquipmentAssetsService.selectVideoOnlineList(param);
        return list;
    }

    @Override
    public Map<String, Object> selectVideoNumberByPro() {
        Map<String, Object> map = iEquipmentAssetsService.selectVideoNumberByPro();

        // 计算
        // 总数
        Integer count = Integer.valueOf(String.valueOf(map.get("totalNumber")));
        // 未完成数
        Integer onlineNumber = Integer.valueOf(String.valueOf(map.get("onlineNumber")));

        // 计算百分比
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(0);
        if (onlineNumber == 0) { // 除数为0，百分比直接为100%
            map.put("percent", "0%");
        } else {
            double retio = onlineNumber * 1.0 / count;
            String percent = format.format(retio);
            map.put("percent", percent);
        }

        return map;
    }

    @Override
    public Map selectVideoById(Integer id) {
        return iEquipmentAssetsService.selectVideoById(id);
    }

    @Override
    public boolean videoOnline(Map<String, Object> map) {
        boolean online = VideoOfflineUtil.openMedia((String)map.get("wvpOpenUrl"));

        HttpClientResult result;
        //拼装请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        //拼装请求参数
        Map params = new HashMap();
        String deviceSerial = (String) map.get("deviceSerial");

        AdminConfig adminConfig = null;
        //获取accessToken
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
            } else {
                throw new CrmException(500, "adminConfig不存在");
            }
        } else {
            throw new CrmException(500, "获取adminConfig接口调用失败");
        }

        params.put("deviceSerial", deviceSerial);
        //开始请求 NVR 设备在线状态
        try {
            result = HttpClientUtils.doPost2(ysyUrlPrefix + "/api/lapp/device/info", headers, params);
            if (result.getCode() == 200) {
                JSONObject jsonObject = result.getContent();
                if ("200".equals(String.valueOf(jsonObject.get("code")))) {
                    JSONObject ysyResult = (JSONObject) jsonObject.get("data");
                    String status = String.valueOf(ysyResult.get("status"));//status=1 在线
                    //仅设备在线 查询设备其他信息
                    if ("1".equals(status)) {
                        DeviceProjectRelation device = new DeviceProjectRelation();
                        if ("0".equals(map.get("status")) && online) {
                            device.setStatus("1");
                            device.setBusinessId(String.valueOf(map.get("assetId")));
                            deviceProjectRelationMapper.updateBybusinessId(device);
                        } else if ("1".equals(map.get("status")) && !online) {
                            device.setStatus("0");
                            device.setBusinessId(String.valueOf(map.get("assetId")));
                            deviceProjectRelationMapper.updateBybusinessId(device);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("监控设备状态更新失败:" + e);
            e.printStackTrace();
        }
        return online;
    }
    //select count(*)as totalNum, count(*) as onLineNum,b.project_name from b_equipment_assets a left join b_project_info b on a.project_id=b.id where a.type_id='50'  group by a.project_id
}

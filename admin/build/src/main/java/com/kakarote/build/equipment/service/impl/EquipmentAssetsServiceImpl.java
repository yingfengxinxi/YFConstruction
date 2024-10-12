package com.kakarote.build.equipment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.equipment.common.EqWarnManager;
import com.kakarote.build.equipment.constant.CpFlagEnum;
import com.kakarote.build.equipment.entity.BO.EquipmentAssetsBO;
import com.kakarote.build.equipment.entity.PO.*;
import com.kakarote.build.equipment.mapper.EquipmentAssetsMapper;
import com.kakarote.build.equipment.service.*;
import com.kakarote.build.gb28181.service.YsyDockingService;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.build.safety.service.ISafetyInspectionDetailService;
import com.kakarote.build.utils.DateUtil;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备资产 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
@Service
public class EquipmentAssetsServiceImpl extends BaseServiceImpl<EquipmentAssetsMapper, EquipmentAssets> implements IEquipmentAssetsService {

    @Autowired
    private IMonitorPointAlertService monitorPointAlertService;

    @Autowired
    private ISafetyInspectionDetailService inspectionDetailService;

    @Autowired
    private IEquipmentHelmetService equipmentHelmetService;

    @Autowired
    public IProjectInfoService projectInfoService;

    @Override
    public BasePage<EquipmentAssets> selectByPage(EquipmentAssetsBO equipmentAssetsBO) {
        equipmentAssetsBO.setOrdersBd("a.status",false);
        equipmentAssetsBO.setOrdersBd("a.sort",false);
        equipmentAssetsBO.setOrdersBd("a.create_time",false);
        BasePage<EquipmentAssets> basePage = getBaseMapper().selectByPage(equipmentAssetsBO.parse(), equipmentAssetsBO);
        return basePage;
    }

    @Override
    public List<EquipmentAssets> selectPlaneByType(Integer typeId) {
        List<EquipmentAssets> list = getBaseMapper().selectPlaneByType(typeId);
        return list;
    }

    @Override
    public List<Map<String, Object>> listByLocationId(Integer locationId) {
        return getBaseMapper().listByLocationId(locationId);
    }

    @Override
    public List<EquipmentAssets> selectEquList(EquipmentAssetsBO equipmentAssetsBO) {
        List<EquipmentAssets> list = getBaseMapper().selectEquList(equipmentAssetsBO);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectSprayList() {
        // 设置设备类型为喷淋设备
        EquipmentAssetsBO equipmentAssetsBO = new EquipmentAssetsBO();
        equipmentAssetsBO.setMonitorType("98");
        // 查询数据
        List<Map<String, Object>> list = getBaseMapper().selectSprayList(equipmentAssetsBO);
        return list;
    }

    @Override
    public List<EquipmentAssets> selectTowerCrane() {
        // 设置塔吊设备类型id
        Integer typeId = 10;

        // 查询数据
        LambdaQueryWrapper<EquipmentAssets> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EquipmentAssets::getTypeId, typeId);
        List<EquipmentAssets> list = getBaseMapper().selectList(queryWrapper);

        return list;
    }

    @Override
    public List<Map> selectAllOverview() {
        return getBaseMapper().selectAllOverview();
    }

    @Override
    public List<Map> selectTowerCraneOverview() {
        return getBaseMapper().selectTowerCraneOverview();
    }

    @Override
    public Map selectTowerCraneAlertNew() {
        Map map = new HashMap();
        map.put("jt",getBaseMapper().selectTowerCraneAlertNew());
        map.put("zt",getBaseMapper().selectTowerCraneAlertYday());
        map.put("sst",getBaseMapper().selectTowerCraneAlert30day());
        return map;
    }

    @Override
    public Map selectTowerCraneById(String id) {
        Map map = getBaseMapper().selectTowerCraneById(id);
        if (map == null) {
            map = new HashMap();
            map.put("assetName", "#塔机");
            map.put("rotation", 0);
            map.put("windspeed", 0);
            map.put("amplitude", 0);
            map.put("weight", 0);
            map.put("amplitude", 0);
            map.put("dipxZero", 0);
            map.put("value", 0);
        }
        return map;
    }

    @Override
    public List<Map> selectElevatorOverview() {
        return getBaseMapper().selectElevatorOverview();
    }

    @Override
    public List<Map> selectPitOverview() {
        return getBaseMapper().selectPitOverview();
    }

    @Override
    public List<Map> selectFormworkOverview() {
        return getBaseMapper().selectFormworkOverview();
    }

    @Override
    public List<Map> selectHookOverview() {
        return getBaseMapper().selectHookOverview();
    }

    @Override
    public Map selectAllAlertNew() {
        Map map = new HashMap();
        map.put("jt",getBaseMapper().selectMonitorAlertNew(""));
        map.put("zt",getBaseMapper().selectMonitorAlertYday(""));
        map.put("sst",getBaseMapper().selectMonitorAlert30day(""));
        return map;
    }

    @Override
    public Map selectPitAlertNew() {
        Map map = new HashMap();
        map.put("jt",getBaseMapper().selectMonitorAlertNew("1"));
        map.put("zt",getBaseMapper().selectMonitorAlertYday("1"));
        map.put("sst",getBaseMapper().selectMonitorAlert30day("1"));
        return map;
    }

    @Override
    public Map selectFormworkAlertNew() {
        Map map = new HashMap();
        map.put("jt",getBaseMapper().selectMonitorAlertNew("11"));
        map.put("zt",getBaseMapper().selectMonitorAlertYday("11"));
        map.put("sst",getBaseMapper().selectMonitorAlert30day("11"));
        return map;
    }

    @Override
    public Map selectElevatorAlertNew() {
        Map map = new HashMap();
        map.put("jt",getBaseMapper().selectElevatorAlertNew());
        map.put("zt",getBaseMapper().selectElevatorAlertYday());
        map.put("sst",getBaseMapper().selectElevatorAlert30day());
        return map;
    }

    @Override
    public Map selectElevatorById(String id) {
        Map<String, Object> obj = getBaseMapper().selectElevatorById(id);
        if (obj == null) {
            obj = new HashMap<>();
            obj.put("assetName", "#升降机");
        }
        return obj;
    }


    @Override
    public Map<String, Object> selectVideoMonitor(Map<String, Object> map) {
        return getBaseMapper().selectVideoMonitor(map);
    }

    @Override
    public BasePage<EquipmentAssets> selectByPageGroup(EquipmentAssetsBO equipmentAssetsBO) {
        equipmentAssetsBO.setOrdersBd("a.status",false);
        equipmentAssetsBO.setOrdersBd("a.sort",true);
        equipmentAssetsBO.setOrdersBd("a.create_time",false);
        BasePage<EquipmentAssets> basePage = getBaseMapper().selectByPageGroup(equipmentAssetsBO.parse(), equipmentAssetsBO);
        return basePage;
    }

    @Override
    public List<Map<String, Object>> EquipmentNum(Map<String, Object> param) {
        return getBaseMapper().EquipmentNum(param);
    }

    @Override
    public List<Map<String, Object>> videoEquiByPro(Map<String, Object> map) {
        return getBaseMapper().videoEquiByPro(map);
    }

    @Override
    public List<Map<String, Object>> videoEquiByProByCheckHox(Map<String, Object> map) {
        return getBaseMapper().videoEquiByProByCheckHox(map);
    }

    @Override
    public List<EquipmentAssets> getEqListByMoType(String moType) {
        return getBaseMapper().getEqListByMoType(moType);
    }

    @Autowired
    private IEquipmentMonitorPointService monitorPointService;

    @Autowired
    private IEquipmentMonitorDataService monitorDataService;

    @Autowired
    private IEquipmentMonitorStatusService monitorStatusService;

    @Autowired
    private IEquipmentMonitorDataAtService dataAtService;

    /**
     * 设备监控中心：接收、处理数据
     *
     * @param jsonObject
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result monitorSaveEqData(JSONObject jsonObject) {
        //校验设备
        String mn = jsonObject.getString("mn");
        LambdaQueryWrapper<EquipmentAssets> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EquipmentAssets::getAssetCode, mn);
        queryWrapper.eq(EquipmentAssets::getStatus, "1");//正常
        queryWrapper.isNotNull(EquipmentAssets::getTenantId);//跳过：非法租户
        queryWrapper.isNotNull(EquipmentAssets::getProjectId);
        List<EquipmentAssets> equipmentAssets = getBaseMapper().selectList(queryWrapper);
        if (equipmentAssets.size() == 0) {
            return Result.error(-1, "无效的设备编码（MN）或设备已报废");
        }
        EquipmentAssets assets = equipmentAssets.get(0);
        //获取点位集合
        LambdaQueryWrapper<EquipmentMonitorPoint> pointLambdaQueryWrapper = new LambdaQueryWrapper<>();
        pointLambdaQueryWrapper.eq(EquipmentMonitorPoint::getStatus, "1");
        pointLambdaQueryWrapper.eq(EquipmentMonitorPoint::getEquipmentId, assets.getAssetId());
        pointLambdaQueryWrapper.eq(EquipmentMonitorPoint::getTenantId, assets.getTenantId());//跳过：非法租户
        pointLambdaQueryWrapper.eq(EquipmentMonitorPoint::getProjectId, assets.getProjectId());
        pointLambdaQueryWrapper.eq(EquipmentMonitorPoint::getMonitorType, "9");
        List<EquipmentMonitorPoint> monitorPoints = monitorPointService.getBaseMapper().selectList(pointLambdaQueryWrapper);
        if (monitorPoints.size() == 0) {
            return Result.error(-1, "该设备下未配置监测点位");
        }
        //预处理点位编码、ID关系
        Map<String, Integer> rMap = new HashMap<>();
        Map<String, EquipmentMonitorPoint> pointMap = new HashMap<>();
        for (EquipmentMonitorPoint point : monitorPoints) {
            rMap.put(point.getPointCode(), point.getId());
            pointMap.put(point.getPointCode(), point);
        }
        //预处理点位、状态数据
        JSONArray jsonArray = jsonObject.getJSONArray("cp");
        List<EquipmentMonitorData> dataList = new ArrayList<>();
        List<EquipmentMonitorDataAt> dataAtList = new ArrayList<>();
        List<EquipmentMonitorStatus> statusList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject one = jsonArray.getJSONObject(i);
            String pointCode = one.getString("pointCode");
            //核对是否存在对应点位
            Integer pointId = rMap.get(pointCode);
            if (pointId == null) {
                log.error("[" + pointCode + "]未检索到对应点位");
                continue;
            }
            //数据
            EquipmentMonitorData data = new EquipmentMonitorData();
            EquipmentMonitorDataAt dataAt = new EquipmentMonitorDataAt();
            data.setPointId(pointId);
            data.setValue(one.getString("value"));
            data.setMonitorTime(DateUtil.parseDate(one.getString("monitorTime"), DateUtil.YMDHMS));
            data.setProjectId(assets.getProjectId());
            data.setTenantId(assets.getTenantId());
            dataList.add(data);
            //复制实时
            BeanUtil.copyProperties(data, dataAt);
            dataAtList.add(dataAt);
            //报警、预警
            monitorData(pointMap.get(pointCode), one.getString("value"));
            //状态
            EquipmentMonitorStatus status = new EquipmentMonitorStatus();
            status.setId(i);
            status.setPointId(pointId);
            String flag = one.getString("flag");
            status.setStatus(flag);
            status.setDes(CpFlagEnum.parse(flag).getDescribe());
            status.setMonitorTime(DateUtil.parseDate(one.getString("monitorTime"), DateUtil.YMDHMS));
            status.setVsn(CpFlagEnum.VERSION.getDescribe());
            status.setProjectId(assets.getProjectId());
            status.setTenantId(assets.getTenantId());
            statusList.add(status);
        }
        //储存点位、及状态数据
        monitorDataService.saveBatch(dataList);
        monitorStatusService.saveBatch(statusList);
        //todo 更新或插入实时点位
        saveOrUpdateDataAt(dataAtList);
        return Result.ok();
    }

    private void saveOrUpdateDataAt(List<EquipmentMonitorDataAt> dataAtList) {
        for (EquipmentMonitorDataAt dataAt : dataAtList) {
            EquipmentMonitorDataAt one = dataAtService.getById(dataAt.getPointId());
            if (one == null) {
                dataAtService.save(dataAt);
            } else {
                dataAtService.updateById(dataAt);
            }
        }
    }

    @Autowired
    private EqWarnManager eqWarnManager;

    /**
     * 预警、报警处理
     *
     * @param point 点位信息
     * @param value 值
     */
    private void monitorData(EquipmentMonitorPoint point, String value) {
        //异步预警
        new Thread(new Runnable() {
            @Override
            public void run() {
                eqWarnManager.warn(point, new BigDecimal(value));
            }
        }).start();
    }

    @Override
    public Result receptEqData(JSONObject jsonObject) {
        //设备数据
        JSONObject assetJson = jsonObject.getJSONObject("asset");
        EquipmentAssets assets = new EquipmentAssets();
        BeanUtil.copyProperties(assetJson, assets);
        //获取点位集合
        LambdaQueryWrapper<EquipmentMonitorPoint> pointLambdaQueryWrapper = new LambdaQueryWrapper<>();
        pointLambdaQueryWrapper.eq(EquipmentMonitorPoint::getStatus, "1");
        pointLambdaQueryWrapper.eq(EquipmentMonitorPoint::getEquipmentId, assets.getAssetId());
        pointLambdaQueryWrapper.eq(EquipmentMonitorPoint::getTenantId, assets.getTenantId());//跳过：非法租户
        pointLambdaQueryWrapper.eq(EquipmentMonitorPoint::getProjectId, assets.getProjectId());
        List<EquipmentMonitorPoint> monitorPoints = monitorPointService.getBaseMapper().selectList(pointLambdaQueryWrapper);
        if (monitorPoints.size() == 0) {
            return Result.error(-1, "该设备下未配置监测点位");
        }
        //预处理点位编码、ID关系
        Map<String, Integer> rMap = new HashMap<>();
        Map<String, EquipmentMonitorPoint> pointMap = new HashMap<>();
        for (EquipmentMonitorPoint point : monitorPoints) {
            rMap.put(point.getPointCode(), point.getId());
            pointMap.put(point.getPointCode(), point);
        }
        //预处理点位、状态数据
        //获取点位数据JSONObject
        JSONArray jsonArray = jsonObject.getJSONArray("dataList");
        List<EquipmentMonitorData> dataList = new ArrayList<>();
        List<EquipmentMonitorDataAt> dataAtList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject one = jsonArray.getJSONObject(i);
            String pointCode = one.getString("pointCode");
            //核对是否存在对应点位
            Integer pointId = rMap.get(pointCode);
            if (pointId == null) {
                log.error("[" + pointCode + "]未检索到对应点位");
                continue;
            }
            //数据
            EquipmentMonitorData data = new EquipmentMonitorData();
            EquipmentMonitorDataAt dataAt = new EquipmentMonitorDataAt();
            data.setPointId(pointId);
            data.setValue(one.getString("value"));
            data.setMonitorTime(DateUtil.parseDate(one.getString("monitorTime"), DateUtil.YMDHMS));
            data.setProjectId(assets.getProjectId());
            data.setTenantId(assets.getTenantId());
            dataList.add(data);
            //复制实时
            BeanUtil.copyProperties(data, dataAt);
            dataAtList.add(dataAt);
            //报警、预警
            monitorData(pointMap.get(pointCode), one.getString("value"));
        }
        //储存点位、及状态数据
        monitorDataService.saveBatch(dataList, 100);
        //
        saveOrUpdateDataAt(dataAtList);
        return Result.ok();
    }

    @Override
    public List<Map<String, Object>> equimentList(Map<String, Object> map) {
        return getBaseMapper().equimentList(map);
    }

    @Override
    public List<Map<String, Object>> equimentListByCheckHox(Map<String, Object> map) {
        return getBaseMapper().equimentListByCheckHox(map);
    }
    @Autowired
    private YsyDockingService ysyDockingService;
    @Autowired
    private AdminService adminService;
    @Override
    public List<Map<String, Object>> groupList2(EquipmentAssetsBO equipmentAssetsBO) {
        List<Map<String, Object>> equimentList= getBaseMapper().groupList2(equipmentAssetsBO);
//        //拼装请求参数
//        Map<String,Object> param=new HashMap<>();
//        param.put("deviceSerial","33011081992397348766:33011036991117755118");//直播源，均采用英文符号，限制50个
//        param.put("protocol",1);//流播放协议，1-ezopen、2-hls、3-rtmp、4-flv，默认为1
//        param.put("quality",2);//视频清晰度，1-高清（主码流）、2-流畅（子码流）
//        param.put("type","1");//1-预览，2-本地录像回放，3-云存储录像回放，非必选，默认为1
//        Result<List<AdminConfig>> Feignresult1 = adminService.queryConfigByNameAndTenantId("yinshiToken");
//        if (Feignresult1.hasSuccess()) {
//            List<AdminConfig> adminConfigList = Feignresult1.getData();
//            if (adminConfigList != null && adminConfigList.size() != 0) {
//                AdminConfig adminConfig = adminConfigList.get(0);
//                if (adminConfig.getValue() != null) {
//                    param.put("accessToken", adminConfig.getValue());
//                }
//            }
//        }
//        //循环查询设备播放地址
//        for(Map<String,Object> map3:equimentList){
//            param.put("gbchannel",String.valueOf(map3.get("deviceChannelId")));//通道id
//            Result result= ysyDockingService.ysyPostInit2(param,"/api/lapp/v2/live/address/get");
//            if(result.getCode()==0){
//                Map<String,Object> ss=(Map)result.getData();
//                map3.put("url",String.valueOf(ss.get("url")));
//            }else{
//                map3.put("url","");
//                map3.put("msg",result.getMsg());
//            }
//        }
        return equimentList;
    }

    @Override
    public List<Map<String, Object>> videoEquiByLocation(Map<String, Object> map) {
        return getBaseMapper().videoEquiByLocation(map);
    }

    @Override
    public List<Map<String, Object>> videoStreamList(Map<String, Object> map) {
        return getBaseMapper().videoStreamList(map);
    }

    @Override
    public List<Map<String, Object>> getLine(Map<String, Object> params) {
        return getBaseMapper().getLine(params);
    }

    @Override
    public Map<String, Object> getDeviceId(String assectId) {
        return getBaseMapper().getDeviceId(assectId);
    }

    @Override
    public BasePage<EquipmentAssets> equiStateList(EquipmentAssetsBO equipmentAssetsBO) {
        return getBaseMapper().equiStateList(equipmentAssetsBO.parse(), equipmentAssetsBO);
    }

    @Override
    public Map<String, Object> videoAccessNum(Map<String, Object> map) {
        return getBaseMapper().videoAccessNum(map);
    }

    @Override
    public Map<String,Object> selectProjectAccessNum(Map<String, Object> map) {
        return getBaseMapper().selectProjectAccessNum(map);
    }

    @Override
    public List<Map<String,Object>> selectEquiAccessNum(Map<String, Object> map) {
        return getBaseMapper().selectEquiAccessNum(map);
    }

    @Override
    public List<Map<String,Object>> selectEquiAlarmNum(Map<String, Object> map) {
        return getBaseMapper().selectEquiAlarmNum(map);
    }

    @Override
    public List<Map<String, Object>> selectVideoEquiList(Map<String, Object> map) {
        return getBaseMapper().selectVideoEquiList(map);
    }

    @Override
    public Map<String, Object> selectVideoNumber() {
        return getBaseMapper().selectVideoNumber();
    }

    @Override
    public List<Map<String, Object>> selectVideoList(Map<String, Object> params) {
        return getBaseMapper().selectVideoList(params);
    }

    /**
     * type 1：预警 2：报警 3：环境 4：塔机 5：升降机 6：基坑 7：高支模 8：人员 9：监控 10：喷淋 11：隐患
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> selectThreeDimensional(Map<String, Object> params) {

        if (params.get("type") == null || params.get("type").equals("")) {
            throw new CrmException(500, "参数type不能为空！");
        }

        String type = (String) params.get("type");
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();

        // 查询预警或报警相关设备 位置信息
        if (type.equals("1") || type.equals("2")) {
            // 查询预警或报警相关设备 位置信息
            result = monitorPointAlertService.selectAlertEquiInfo(type);
        } else if (type.equals("3") ) {
            // 查询环境相关设备 位置信息
            result = getBaseMapper().selectEnvEquiInfo(type);
        } else if (type.equals("4") ) {
            map.put("iconType", type);
            map.put("monitorType", "2"); // 塔机
            result = getBaseMapper().selectAssetInfoByMonitorType(map);
            if (result.size() <= 0) { // 无数据 需返回默认数据
                Map<String, Object> equObj = new HashMap<>();
                equObj.put("assetId", 0);
                equObj.put("assetCode", "0");
                result.add(equObj);
            }
        } else if (type.equals("5") ) {
            map.put("iconType", type);
            map.put("monitorType", "3"); // 升降机
            result = getBaseMapper().selectAssetInfoByMonitorType(map);
            if (result.size() <= 0) { // 无数据 需返回默认数据
                Map<String, Object> equObj = new HashMap<>();
                equObj.put("assetId", 0);
                equObj.put("assetCode", "0");
                result.add(equObj);
            }
        } else if (type.equals("6") ) {
            map.put("iconType", type);
            map.put("monitorType", "1"); // 基坑
            result = getBaseMapper().selectAssetInfoByMonitorType(map);
            if (result.size() <= 0) { // 无数据 需返回默认数据
                Map<String, Object> equObj = new HashMap<>();
                equObj.put("assetId", 0);
                result.add(equObj);
            }
        } else if (type.equals("7") ) {
            map.put("iconType", type);
            map.put("monitorType", "11"); // 高支模
            result = getBaseMapper().selectAssetInfoByMonitorType(map);
            if (result.size() <= 0) { // 无数据 需返回默认数据
                Map<String, Object> equObj = new HashMap<>();
                equObj.put("assetId", 0);
                result.add(equObj);
            }
        } else if (type.equals("8") ) {
            // 查询人员最新的三维位置信息
            result = equipmentHelmetService.selectLatestLocationList();
        } else if (type.equals("9") ) {
            // 查询监控设备相关 位置信息
            result = getBaseMapper().selectVideoEquiInfo(type);
        } else if (type.equals("10") ) {
            // 查询喷淋设备相关 位置信息
            result = getBaseMapper().selectSprayEquiInfo(type);
        } else if (type.equals("11") ) {
            // 查询隐患相关 位置信息
            result = inspectionDetailService.selectHiddenDangerInfo(type);
        } else {
            throw new CrmException(500, "无当前type值的对应结果！");
        }


        return result;
    }

    @Override
    public BasePage<Map<String,Object>> selectVideoOnlineList(Map<String, Object> param) {
        PageEntity page=new PageEntity();
        page.setPage(Integer.valueOf(String.valueOf(param.get("page"))));
        page.setLimit(Integer.valueOf(String.valueOf(param.get("limit"))));
        BasePage<Map<String,Object>> list=getBaseMapper().selectVideoOnlineList(page.parse(),param);
        return list;
    }


    // 2022-04-18  查询数据监测的数据列表
    @Override
    public Map<String, Object> selectEquipmentList(Map<String, Object> param) {
        Map <String, Object> map=new HashMap<String, Object>();
        int page =0;
        int limit =0;
        // monitorType 对应监测项类型
        if (param.get("monitorType") == null || param.get("monitorType").equals("")) {
            throw new CrmException(500, "参数monitorType不能为空");
        }
        if (param.get("page") == null || param.get("page").equals("")) {
            page=1;
        }else{
            Object conversion= param.get("page");
            page=Integer.valueOf(String.valueOf(conversion));

        }
        if (param.get("limit") == null || param.get("limit").equals("")) {
            limit =10;
        }else{
            Object conversion= param.get("limit");
            limit=Integer.valueOf(String.valueOf(conversion));
        }
        param.put("page",(page-1)*limit);
        param.put("limit",limit);
        List<Map<String, Object>> result = this.getBaseMapper().selectEquipmentList(param);// 列表数据
        Map<String, Object>  zs = this.getBaseMapper().selectEquipmentListCount(param);// 分页用总数
        map.put("list",result);
        map.put("totalRow",zs.get("zs"));
        return map;
    }

    @Override
    public Map<String, Object> machineEquNumber() {
        return getBaseMapper().machineEquNumber();
    }

    @Override
    public BasePage<Map<String, Object>> selectEquiListByType(EquipmentAssetsBO assetsBO) {
        return getBaseMapper().selectEquiListByType(assetsBO.parse(), assetsBO);
    }

    @Override
    public Map<String, Object> selectVideoNumberByPro() {
        return getBaseMapper().selectVideoNumberByPro();
    }

    @Override
    public List<Map<String, Object>> selectByPageGroupCount(Map<String, Object> params) {
        return getBaseMapper().selectByPageGroupCount(params);
    }

    @Override
    public List<Map<String, Object>> getLineByThreeDimensional(Map<String, Object> params) {
        return getBaseMapper().getLineByThreeDimensional(params);
    }

    @Override
    public BasePage<Map<String, Object>> selectEquiListByThree(EquipmentAssetsBO assetsBO) {
        return getBaseMapper().selectEquiListByThree(assetsBO.parse(), assetsBO);
    }

    @Override
    public List<Map> selectAllTower() {
        List<Map<String, Object>>  towers =  getBaseMapper().selectAllTower();
        List<Integer> projects = towers.stream().map(e->(Integer)e.get("projectId")).collect(Collectors.toList());

        // 项目id去重
        HashSet set = new HashSet(projects);
        projects.clear();
        projects.addAll(set);

        QueryWrapper<ProjectInfo> queryWrapper= new QueryWrapper<>();
        queryWrapper.in("id",projects);
        List<ProjectInfo> projectInfos =  projectInfoService.list(queryWrapper);
        List<Map> res = new ArrayList<>();
        for(ProjectInfo projectInfo:projectInfos){

            Map project = new HashMap();
            List<Map> resTower = new ArrayList<>();
            for(Map tower:towers){
                if((projectInfo.getId().compareTo(Integer.valueOf (tower.get("projectId")+""))==0) ){
                    resTower.add(tower);
                }
            }
            if(resTower.size()>0){
                project.put("id",projectInfo.getId());
                project.put("name", projectInfo.getProjectAbbreviation() != null ? projectInfo.getProjectAbbreviation():projectInfo.getProjectName());
                project.put("children",resTower);
                res.add(project);
            }
        }
        return res;
    }

    @Override
    public List<Map> selectAllElevator() {
        List<Map<String, Object>>  towers =  getBaseMapper().selectAllElevator();
        List<Integer> projects = towers.stream().map(e->(Integer)e.get("projectId")).collect(Collectors.toList());

        // 项目id去重
        HashSet set = new HashSet(projects);
        projects.clear();
        projects.addAll(set);

        QueryWrapper<ProjectInfo> queryWrapper= new QueryWrapper<>();
        queryWrapper.in("id",projects);
        List<ProjectInfo> projectInfos =  projectInfoService.list(queryWrapper);
        List<Map> res = new ArrayList<>();

        for(ProjectInfo projectInfo:projectInfos){
            Map project = new HashMap();
            List<Map> resTower = new ArrayList<>();
            for(Map tower:towers){
                if((projectInfo.getId().compareTo(Integer.valueOf (tower.get("projectId")+""))==0) ){
                    resTower.add(tower);
                }
            }
            if(resTower.size()>0){
                project.put("id",projectInfo.getId());
                project.put("name", projectInfo.getProjectAbbreviation() != null ? projectInfo.getProjectAbbreviation():projectInfo.getProjectName());
                project.put("children",resTower);
                res.add(project);
            }
        }
        return res;
    }

    @Override
    public Map selectVideoById(Integer id) {
        return getBaseMapper().selectVideoById(id);
    }

    @Override
    public BasePage<Map<String, Object>> selectProjectByEquipment(EquipmentAssetsBO equipmentAssetsBO) {
        return getBaseMapper().selectProjectByEquipment(equipmentAssetsBO.parse(), equipmentAssetsBO);
    }

    @Override
    public BasePage<EquipmentAssets> selectListByProject(EquipmentAssetsBO equipmentAssetsBO) {
        return getBaseMapper().selectListByProject(equipmentAssetsBO.parse(), equipmentAssetsBO);
    }

    @Override
    public List<EquipmentAssets> selectSprayEquiList() {
        EquipmentAssetsBO equipmentAssetsBO = new EquipmentAssetsBO();
        equipmentAssetsBO.setMonitorType("98");
        return getBaseMapper().selectSprayEquiList(equipmentAssetsBO);
    }

    @Override
    public BasePage<EquipmentAssets> equiStateListNew(EquipmentAssetsBO equipmentAssetsBO) {
        return getBaseMapper().equiStateListNew(equipmentAssetsBO.parse(), equipmentAssetsBO);
    }
}

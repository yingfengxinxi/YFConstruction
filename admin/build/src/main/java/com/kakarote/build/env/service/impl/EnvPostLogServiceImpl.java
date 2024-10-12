package com.kakarote.build.env.service.impl;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.build.ai.entity.PO.AiBareSoilData;
import com.kakarote.build.ai.entity.PO.BAiDregCarCleaning;
import com.kakarote.build.ai.service.impl.AiBareSoilDataServiceImpl;
import com.kakarote.build.ai.service.impl.AiDregCarServiceImpl;
import com.kakarote.build.api.common.EnvMonitorCodeEnum;
import com.kakarote.build.env.entity.PO.EnvPostData;
import com.kakarote.build.env.entity.PO.EnvPostLog;
import com.kakarote.build.env.mapper.EnvPostLogMapper;
import com.kakarote.build.env.service.IEnvPostLogService;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.core.common.Result;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 扬尘在线监测设备数据传输日志 服务实现类
 * </p>
 *
 * @author shz
 * @since 2021-08-23
 */
@Service
public class EnvPostLogServiceImpl extends BaseServiceImpl<EnvPostLogMapper, EnvPostLog> implements IEnvPostLogService {

    @Autowired
    private IProjectInfoService projectInfoService;

    @Autowired
    private EnvPostDataServiceImpl envPostDataService;

    @Autowired
    private AiDregCarServiceImpl aiDregCarService;

    @Autowired
    private AiBareSoilDataServiceImpl bareSoilDataService;

    @Override
    public Result envMonitorDataAsync(Map<String, Object> map) {
        try {
            LambdaQueryWrapper<ProjectInfo> projectQueryWrapper = new LambdaQueryWrapper<>();
            projectQueryWrapper.eq(ProjectInfo::getAppid, map.get("appid"));
            projectQueryWrapper.ge(ProjectInfo::getTenantId, "0");
            projectQueryWrapper.ge(ProjectInfo::getProjectId, "0");
            List<ProjectInfo> proList = projectInfoService.list(projectQueryWrapper);
            ProjectInfo projectInfo = proList.get(0);
            // 保存数据
            this.saveLogAndData(map, projectInfo.getTenantId(), projectInfo.getId());

            return Result.ok();
        } catch (CrmException e) {
            e.printStackTrace();
            log.error("打印错误：" + e);
            // 保存
            EnvPostLog postLog = new EnvPostLog();
            postLog.setAppid(null);
            postLog.setTimestamp(null);
            postLog.setSign(null);
            postLog.setTenantId(null);
            postLog.setProjectId(null);
            postLog.setData(map.toString());
            postLog.setCode(e.getCode());
            postLog.setMsg(e.getMsg());
            this.getBaseMapper().saveLog(postLog);

            throw e;
        }

    }

    @Override
    public Result selectResult(Map<String, Object> map) {

        if (!map.containsKey("appid") || "".equals(map.get("appid"))) {
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_MISSING_PARAM);
        }
        if (!map.containsKey("timestamp") || "".equals(map.get("timestamp"))) {
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_MISSING_PARAM);
        }
        if (!map.containsKey("sign") || "".equals(map.get("sign"))) {
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_MISSING_PARAM);
        }
        if (!map.containsKey("data") || "".equals(map.get("data"))) {
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_MISSING_PARAM);
        }

        // 查询是否保存成功
        LambdaQueryWrapper<EnvPostLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnvPostLog::getAppid, map.get("appid"));
        queryWrapper.eq(EnvPostLog::getTimestamp, map.get("data"));
        queryWrapper.eq(EnvPostLog::getSign, map.get("sign"));
        queryWrapper.ge(EnvPostLog::getTenantId, "0");
        queryWrapper.ge(EnvPostLog::getProjectId, "0");

        List<EnvPostLog> list = this.list(queryWrapper);
        if (list == null || list.size() <= 0) {
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_FAIL_SAVE);
        }
        return Result.ok();
    }

    @Override
    public Result vehicleCleaningData(List<Map<String, Object>> list) {
        try {
            // 保存数据
            this.saveVehicleCleaningData(list);

            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("打印错误：" + e);
            throw e;
        }
    }

    @Override
    public Result baseSoilAlarmData(List<Map<String, Object>> list) {
        try {
            // 保存数据
            this.saveBaseSoilAlarmData(list);

            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("打印错误：" + e);
            throw e;
        }
    }

    private void saveBaseSoilAlarmData(List<Map<String, Object>> list) {
        try {

            List<AiBareSoilData> bareSoilDataList = new ArrayList<>();
            for (Map<String, Object> dataMap: list) {

                if (dataMap.get("projectId") == null || "".equals(dataMap.get("projectId"))) {
                    throw new CrmException(500, "项目id不能为空");
                }

                // 判断项目id是否存在

                Integer projectId = Integer.valueOf((String) dataMap.get("projectId"));

                LambdaQueryWrapper<ProjectInfo> projectQueryWrapper = new LambdaQueryWrapper<>();
                projectQueryWrapper.eq(ProjectInfo::getId, projectId);
                projectQueryWrapper.ge(ProjectInfo::getTenantId, "0");
                List<ProjectInfo> proList = projectInfoService.list(projectQueryWrapper);
                if (proList == null || proList.size() <= 0) {
                    throw new CrmException(500, "项目id：" + projectId + "不存在,请项目施工单位先在住建委云平台申请后再进行数据推送");
                }

                ProjectInfo projectInfo = proList.get(0);

                if (dataMap.get("alarmLocation") == null || "".equals(dataMap.get("alarmLocation"))) {
                    throw new CrmException(500, "alarmLocation不能为空");
                }
                if (dataMap.get("alarmType") == null || "".equals(dataMap.get("alarmType"))) {
                    throw new CrmException(500, "alarmType不能为空");
                }
                if (dataMap.get("alarmContent") == null || "".equals(dataMap.get("alarmContent"))) {
                    throw new CrmException(500, "alarmContent不能为空");
                }
                if (dataMap.get("alarmTime") == null || "".equals(dataMap.get("alarmTime"))) {
                    throw new CrmException(500, "alarmTime不能为空");
                }

                AiBareSoilData bareSoilData = new AiBareSoilData();
                String id = UuidUtils.generateUuid();
                bareSoilData.setId(id);
                bareSoilData.setTenantId(projectInfo.getTenantId());
                bareSoilData.setProjectId(projectId);
                bareSoilData.setAlarmLocation((String) dataMap.get("alarmLocation"));
                bareSoilData.setAlarmType((String) dataMap.get("alarmType"));
                bareSoilData.setAlarmContent((String) dataMap.get("alarmContent"));
                bareSoilData.setAlarmTime(DateUtil.parse(String.valueOf(dataMap.get("alarmTime"))));
                bareSoilData.setAlarmImg((String) dataMap.get("alarmImg"));
                bareSoilData.setAlarmVideo((String) dataMap.get("alarmVideo"));
                bareSoilData.setOnlyNumber((String) dataMap.get("onlyNumber"));
                bareSoilData.setCreateTime(new Date());

                bareSoilDataList.add(bareSoilData);
            }

            // 批量保存
            bareSoilDataService.getBaseMapper().batchSaveData(bareSoilDataList);

        } catch (CrmException e) {
            e.printStackTrace();
            log.error("裸土分析报警数据传输错误：" + e);
            throw e;
        } catch (DateException e){
            e.printStackTrace();
            log.error("裸土分析报警数据传输错误：" + e);
            throw new CrmException(500, "日期时间格式不符合规范!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("裸土分析报警数据传输错误：" + e);
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_SYSTEM_ERROR);
        }
    }


    public Result saveLogAndData(Map<String, Object> map, Integer tenantId, Integer projectId) {

        try {
            List<Map<String, Object>> data = (List<Map<String, Object>>) map.get("data");
            String jsonData = JSONObject.toJSONString(data);
            map.put("data", jsonData);

            JSONObject json = (JSONObject) JSONObject.toJSON(map);
            EnvPostLog postLog = JSONObject.parseObject(String.valueOf(json), EnvPostLog.class);
            postLog.setCode(0);
            postLog.setMsg("保存成功");
            postLog.setTenantId(tenantId);
            postLog.setProjectId(projectId);

            // 保存log
            this.getBaseMapper().saveLog(postLog);

            // 保存data
            List<EnvPostData> postDataList = new ArrayList<>();
            for (Map<String, Object> dataMap:data) {
                JSONObject json2 = (JSONObject) JSONObject.toJSON(dataMap);
                EnvPostData postData = JSONObject.parseObject(String.valueOf(json2), EnvPostData.class);
                postData.setTenantId(tenantId);
                postData.setProjectId(projectId);
                postData.setLogId(postLog.getId());

                postDataList.add(postData);
            }

            // 批量保存
            envPostDataService.getBaseMapper().batchSaveData(postDataList);

            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("系统异常：" + e);
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_SYSTEM_ERROR);
        }
    }

    private void saveVehicleCleaningData(List<Map<String, Object>> list) {
        try {

            List<BAiDregCarCleaning> carCleaningList = new ArrayList<>();
            for (Map<String, Object> dataMap: list) {

                if (dataMap.get("projectId") == null || "".equals(dataMap.get("projectId"))) {
                    throw new CrmException(500, "项目id不能为空");
                }

                // 判断项目id是否存在
                if (dataMap.get("projectId") == null || dataMap.get("projectId").equals("")) {
                    throw new CrmException(500, "项目id不能为空");
                }
                Integer projectId = Integer.valueOf(String.valueOf(dataMap.get("projectId")));



                LambdaQueryWrapper<ProjectInfo> projectQueryWrapper = new LambdaQueryWrapper<>();
                projectQueryWrapper.eq(ProjectInfo::getId, projectId);
                projectQueryWrapper.ge(ProjectInfo::getTenantId, "0");
                List<ProjectInfo> proList = projectInfoService.list(projectQueryWrapper);
                if (proList == null || proList.size() <= 0) {
                    throw new CrmException(500, "项目id：" + projectId + "不存在,请项目施工单位先在住建委云平台申请后再进行数据推送");
                }

                ProjectInfo projectInfo = proList.get(0);

                if (dataMap.get("catchTime") == null || "".equals(dataMap.get("catchTime"))) {
                    throw new CrmException(500, "catchTime不能为空");
                }
                if (dataMap.get("catchNo") == null || "".equals(dataMap.get("catchNo"))) {
                    throw new CrmException(500, "catchNo不能为空");
                }
                if (dataMap.get("alarmType") == null || "".equals(dataMap.get("alarmType"))) {
                    throw new CrmException(500, "alarmType不能为空");
                }
                if (dataMap.get("licensePlateNumber") == null || "".equals(dataMap.get("licensePlateNumber"))) {
                    throw new CrmException(500, "licensePlateNumber不能为空");
                }

                BAiDregCarCleaning carCleaning = new BAiDregCarCleaning();
                String id = UuidUtils.generateUuid();
                carCleaning.setId(id);
                carCleaning.setTenantId(projectInfo.getTenantId());
                carCleaning.setProjectId(projectId);
                carCleaning.setCleaningTime(DateUtil.parse(String.valueOf(dataMap.get("catchTime"))));
                carCleaning.setCatchNo((String) dataMap.get("catchNo"));
                carCleaning.setAlarmType((String) dataMap.get("alarmType"));
                if (dataMap.get("appearanceTime") != null) {
                    carCleaning.setAppearanceTime(DateUtil.parse(String.valueOf(dataMap.get("appearanceTime"))));
                }
                if (dataMap.get("appearanceImg") != null) {
                    carCleaning.setAppearanceImg((String) dataMap.get("appearanceImg"));
                }
                carCleaning.setLicensePlateNumber((String) dataMap.get("licensePlateNumber"));
                carCleaning.setLicensePlateColor((String) dataMap.get("licensePlateColor"));
                carCleaning.setCleaningImg((String) dataMap.get("photoUrl"));
                carCleaning.setVideoUrl((String) dataMap.get("videoUrl"));
                carCleaning.setCreateTime(new Date());

                carCleaningList.add(carCleaning);
            }

            // 批量保存
            aiDregCarService.getBaseMapper().batchSaveData(carCleaningList);

        } catch (CrmException e) {
            e.printStackTrace();
            log.error("清洗车辆记录传输接口错误：" + e);
            throw e;
        } catch (DateException e){
            e.printStackTrace();
            log.error("清洗车辆记录传输接口错误：" + e);
            throw new CrmException(500, "日期时间格式不符合规范!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("清洗车辆记录传输接口错误：" + e);
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_SYSTEM_ERROR);
        }
    }

}

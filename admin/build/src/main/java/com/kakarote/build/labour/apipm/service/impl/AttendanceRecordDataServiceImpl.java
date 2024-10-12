package com.kakarote.build.labour.apipm.service.impl;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.api.common.EnvMonitorCodeEnum;
import com.kakarote.build.labour.apipm.service.IAttendanceRecordDataService;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceRecordBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceConfiguration;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceRecord;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.labour.entity.PO.LabourSubcontractor;
import com.kakarote.build.labour.mapper.BLabourAttendanceRecordMapper;
import com.kakarote.build.labour.service.IBLabourAttendanceConfigurationService;
import com.kakarote.build.labour.service.IBLabourAttendanceRecordService;
import com.kakarote.build.labour.service.ILabourInfocollectionService;
import com.kakarote.build.labour.service.ILabourSubcontractorService;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.build.utils.ApiUtils;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 考勤记录管理 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-04-14
 */
@Service
public class AttendanceRecordDataServiceImpl extends BaseServiceImpl<BLabourAttendanceRecordMapper, BLabourAttendanceRecord> implements IAttendanceRecordDataService {


    @Autowired
    private ApiUtils apiUtils;

    @Autowired
    private ILabourInfocollectionService labourInfocollectionService;



    @Override
    public Result attendanceRecordDataAdd(Map<String, Object> parpam){
        try {
            BLabourAttendanceRecord bLabourAttendanceRecord= new BLabourAttendanceRecord();
            Map<String, Object> data = new HashMap<String, Object>();
            LabourInfocollection labourInfocollectiondata= new LabourInfocollection ();
            ProjectInfo projectInfo= apiUtils.dataValidation(parpam);
            bLabourAttendanceRecord.setCreateBy((String)parpam.get("createBy"));
            if (parpam.get("time") != null && !"".equals(parpam.get("time"))) {
                String time=(String)parpam.get("time");
                DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                bLabourAttendanceRecord.setCreateTime(dft.parse(time));
            }
            bLabourAttendanceRecord.setProjectId(projectInfo.getId());
            bLabourAttendanceRecord.setTenantId(projectInfo.getTenantId());
            labourInfocollectiondata.setProjectId(projectInfo.getId());
            labourInfocollectiondata.setTenantId(projectInfo.getTenantId());
            if(parpam.get("data") != null && !"".equals(parpam.get("data"))){
                data = (Map<String, Object>) parpam.get("data");
                if (data.get("infocollectionName") == null || "".equals(data.get("infocollectionName"))) {
                    throw new CrmException(500, "考勤人员姓名不能为空");
                }
                if (data.get("infocollectionCard") == null || "".equals(data.get("infocollectionCard"))) {
                    throw new CrmException(500, "考勤人员身份证号不能为空");
                }
                labourInfocollectiondata.setName((String)data.get("infocollectionName").toString().trim());
                labourInfocollectiondata.setIdCard((String)data.get("infocollectionCard").toString().trim());
                labourInfocollectiondata.setAttendanceNumber(String.valueOf((String)data.get("attendanceNumber").toString().trim()));
                LabourInfocollection labourInfocollection=  labourInfocollectionService.selectInfoRecord(labourInfocollectiondata);
                if (labourInfocollection == null) {
                    throw new CrmException(500, "考勤人员：" + data.get("infocollectionName") + "不存在,请先维护该人员基本信息后在进行上传");
                }
                bLabourAttendanceRecord.setInfocollectionId(String.valueOf(labourInfocollection.getId()));
                bLabourAttendanceRecord.setInfocollectionName((String)data.get("infocollectionName"));
                bLabourAttendanceRecord.setInfocollectionCard((String)data.get("infocollectionCard"));
                bLabourAttendanceRecord.setAttendanceRecordTeamName(String.valueOf(labourInfocollection.getTeamName()));

                if (data.get("deviceSn") == null || "".equals(data.get("deviceSn"))) {
                    throw new CrmException(500, "设备序列号不能为空");
                }

                if (data.get("channel") == null || "".equals(data.get("channel"))) {
                    throw new CrmException(500, "通道名称不能为空");
                }
                bLabourAttendanceRecord.setChannel((String)data.get("channel"));
                if (data.get("clockTime") != null && !"".equals(data.get("clockTime"))) {
                    String clockTime=(String)data.get("clockTime");
                    DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    bLabourAttendanceRecord.setClockTime(dft.parse(clockTime));
                }else{
                    throw new CrmException(500, "打卡时间不能为空");
                }
                if (data.get("image") != null && !"".equals(data.get("image"))) {
                    bLabourAttendanceRecord.setImage((String)data.get("image"));
                }
                if (data.get("clockDirection") == null || "".equals(data.get("clockDirection"))) {
                    throw new CrmException(500, "打卡方向不能为空");
                }
                bLabourAttendanceRecord.setClockDirection((String)data.get("clockDirection"));
                if (data.get("attendType") == null || "".equals(data.get("attendType"))) {
                    throw new CrmException(500, "通行方式不能为空");
                }
                bLabourAttendanceRecord.setAttendType((String)data.get("attendType"));
                bLabourAttendanceRecord.setAttendanceNumber((String)data.get("attendanceNumber"));
                bLabourAttendanceRecord.setBodyTemperature((String)data.get("bodyTemperature"));
                bLabourAttendanceRecord.setAlcoholicity((String)data.get("alcoholicity"));
                bLabourAttendanceRecord.setBloodPressure((String)data.get("bloodPressure"));
                bLabourAttendanceRecord.setBloodOxygen((String)data.get("bloodOxygen"));
                if(data.get("lng") != null && !"".equals(data.get("lng"))){
                    bLabourAttendanceRecord.setLng(new BigDecimal((String)data.get("lng")));
                }
                if(data.get("lat") != null && !"".equals(data.get("lat"))){
                    bLabourAttendanceRecord.setLat(new BigDecimal((String)data.get("lat")));
                }
                save(bLabourAttendanceRecord);
            }else{
                throw new CrmException(500, "您未传输数据主体！");
            }
            return Result.ok();
        } catch (CrmException e) {
            e.printStackTrace();
            log.error("考勤数据上传接口错误：" + e);
            throw e;
        } catch (DateException e){
            e.printStackTrace();
            log.error("考勤数据上传接口错误：" + e);
            throw new CrmException(500, "日期时间格式不符合规范!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("考勤数据上传接口错误：" + e);
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_SYSTEM_ERROR);
        }
    }

}

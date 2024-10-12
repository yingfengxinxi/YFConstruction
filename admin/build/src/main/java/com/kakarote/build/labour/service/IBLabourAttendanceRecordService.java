package com.kakarote.build.labour.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceDetailBO;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceRecordBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceDetail;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceRecord;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
/**
 * <p>
 * 考勤记录管理 服务类
 * </p>
 *
 * @author author
 * @since 2021-04-14
 */
public interface IBLabourAttendanceRecordService extends BaseService<BLabourAttendanceRecord> {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BLabourAttendanceRecord> selectList(BLabourAttendanceRecordBO bLabourAttendanceRecordBO);

    public List<BLabourAttendanceRecord> selectDetailList(BLabourAttendanceRecord bLabourAttendanceRecord);

    public List<BLabourAttendanceRecord> selectByMonth(BLabourAttendanceRecord bLabourAttendanceRecord);

    public List<BLabourAttendanceRecord> selectDetailListUpdate(BLabourAttendanceRecord bLabourAttendanceRecord);

    public List<BLabourAttendanceRecord> selectValidation(BLabourAttendanceRecord bLabourAttendanceRecord);

    /**
     * excel导入员工
     * @param file file
     */
    public JSONObject excelImport(MultipartFile file);

    BasePage<Map<String, Object>> getAttendanceDetailList(BLabourAttendanceRecordBO bLabourAttendanceRecordBO);

    Map<String, Object> selectEntryAndExit();

    Map<String, Object> selectPositionAttendance();

    List<BLabourAttendanceRecord> selectDynamic();

    List<BLabourAttendanceRecord> AttendanceRate();

    public void  demoDataAdd();

    public List<Map<String,Object>> projectAttendanceRate();

    public List<Map<String,Object>> projectAttendanceRateGo();

    public Map<String,Object> peopleNumber();

    public Map<String,Object> workersConstitute();

    public Map<String,Object> workerTypeConstitute();

    public Map<String,Object> wageConstitute();

    public Map<String,Object> defaultWageConstitute();

    List<Map<String, Object>> selectAttendanceNumber();

    BasePage<BLabourAttendanceRecord> selectDynamicNew(Map<String,Object> param);

    BasePage<Map<String, Object>> peopleInfoDetails(Map<String,Object> params);

    Map selectByUserId(Integer id);

    void addClock(BLabourAttendanceRecord record);
}

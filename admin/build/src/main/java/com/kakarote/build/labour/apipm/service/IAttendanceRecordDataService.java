package com.kakarote.build.labour.apipm.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceRecordBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceRecord;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 考勤记录管理 服务类
 * </p>
 *
 * @author author
 * @since 2021-04-14
 */
public interface IAttendanceRecordDataService extends BaseService<BLabourAttendanceRecord> {

    public Result attendanceRecordDataAdd(Map<String, Object> parpam);

}

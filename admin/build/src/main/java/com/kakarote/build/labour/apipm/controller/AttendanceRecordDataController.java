package com.kakarote.build.labour.apipm.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.labour.apipm.service.IAttendanceRecordDataService;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceRecordBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceRecord;
import com.kakarote.build.labour.service.IBLabourAttendanceRecordService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 考勤记录管理 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-04-14
 */
@RestController
@RequestMapping("/apiLabour/attendanceRecordData")
public class AttendanceRecordDataController {


    @Autowired
    private IAttendanceRecordDataService iAttendanceRecordDataService;

    /**
     * 传输新增考勤数据
     * @param parpam
     * @return
     */
    @PostMapping("/post")
    @ApiOperation("考勤数据新增")
    public Result attendanceRecordDataAdd(@RequestBody Map<String, Object> parpam) {
        return iAttendanceRecordDataService.attendanceRecordDataAdd(parpam);
    }


}


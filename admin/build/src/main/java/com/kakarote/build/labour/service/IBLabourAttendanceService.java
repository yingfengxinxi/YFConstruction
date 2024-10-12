package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.BO.BLabourAttendanceBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendance;
import com.kakarote.build.safety.entity.BO.BSafetySafetyTrainingBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyTraining;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 考勤管理 服务类
 * </p>
 *
 * @author author
 * @since 2021-04-14
 */
public interface IBLabourAttendanceService extends BaseService<BLabourAttendance> {


    /**
     * 查询数据列表
     *
     */
    public BasePage<BLabourAttendanceBO> selectList(BLabourAttendanceBO bLabourAttendanceBO);

    public String add(BLabourAttendance bLabourAttendance);

    public String edit(BLabourAttendance bLabourAttendance);

    public String validation(BLabourAttendance bLabourAttendance);

}

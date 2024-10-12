package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.BO.BLabourAttendanceBO;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceConfigurationBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendance;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceConfiguration;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 考勤日期设定 服务类
 * </p>
 *
 * @author author
 * @since 2021-12-17
 */
public interface IBLabourAttendanceConfigurationService extends BaseService<BLabourAttendanceConfiguration> {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BLabourAttendanceConfiguration> selectList(BLabourAttendanceConfigurationBO bLabourAttendanceConfigurationBO);

    public String add(BLabourAttendanceConfiguration bLabourAttendanceConfiguration);

    public String edit(BLabourAttendanceConfiguration bLabourAttendanceConfiguration);

    public String validation(BLabourAttendanceConfiguration bLabourAttendanceConfiguration);

    public List<BLabourAttendanceConfiguration> dataList(BLabourAttendanceConfiguration bLabourAttendanceConfiguration);

}

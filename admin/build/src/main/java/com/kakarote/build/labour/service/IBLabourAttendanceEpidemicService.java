package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.BO.BLabour5gvideoBO;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceEpidemicBO;
import com.kakarote.build.labour.entity.PO.BLabour5gvideo;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceEpidemic;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * 疫情进出场统计 服务类
 * </p>
 *
 * @author zhang
 * @since 2022-11-29
 */
public interface IBLabourAttendanceEpidemicService extends BaseService<BLabourAttendanceEpidemic> {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BLabourAttendanceEpidemicBO> selectList(BLabourAttendanceEpidemicBO bLabourAttendanceEpidemicBO);

    public String add(BLabourAttendanceEpidemic bLabourAttendanceEpidemic);

    public String edit(BLabourAttendanceEpidemic bLabourAttendanceEpidemic);

    public Map<String, Object> attendanceEpidemicStatistical();
}

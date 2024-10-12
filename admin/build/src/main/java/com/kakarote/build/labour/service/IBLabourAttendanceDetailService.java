package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.BO.BLabourAttendanceBO;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceDetailBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendance;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceDetail;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceDetailMonth;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 考勤明细管理 服务类
 * </p>
 *
 * @author author
 * @since 2021-04-14
 */
public interface IBLabourAttendanceDetailService extends BaseService<BLabourAttendanceDetail> {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BLabourAttendanceDetail> selectList(BLabourAttendanceDetailBO bLabourAttendanceDetailBO);

    /**
     * 查询月份数据列表
     *
     */
    public List<BLabourAttendanceDetailMonth> selectMonth(BLabourAttendanceDetailMonth bLabourAttendanceDetailMonth);

    public Long countByattendance(String id) ;
    public BLabourAttendanceDetail selectUpdate(BLabourAttendanceDetail bLabourAttendanceDetail);

    public String  deletePid(String pid);


    public String  selectWageDetail(BLabourAttendanceDetail bLabourAttendanceDetail);
}

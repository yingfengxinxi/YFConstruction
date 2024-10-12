package com.kakarote.build.labour.mapper;

import com.kakarote.build.labour.entity.BO.BLabourAttendanceBO;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceDetailBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendance;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceDetail;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceDetailMonth;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 考勤明细管理 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-04-14
 */
public interface BLabourAttendanceDetailMapper extends BaseMapper<BLabourAttendanceDetail> {

    BasePage<BLabourAttendanceDetail> selectDataList(BasePage<AdminMessage> parse, @Param("data") BLabourAttendanceDetailBO bLabourAttendanceDetailBO);

    public List<BLabourAttendanceDetailMonth> selectMonth(BLabourAttendanceDetailMonth bLabourAttendanceDetailMonth);

    public Long countByattendance(String id);

    public BLabourAttendanceDetail selectUpdate(BLabourAttendanceDetail bLabourAttendanceDetail);

    public Integer  deletePid(String pid);

    public String  selectWageDetail(BLabourAttendanceDetail bLabourAttendanceDetail);

}

package com.kakarote.build.labour.mapper;

import com.kakarote.build.labour.entity.BO.BLabourAttendanceBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendance;
import com.kakarote.build.safety.entity.BO.BSafetySafetyTrainingBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyTraining;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 考勤管理 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-04-14
 */
public interface BLabourAttendanceMapper extends BaseMapper<BLabourAttendance> {

    BasePage<BLabourAttendanceBO> selectDataList(BasePage<AdminMessage> parse, @Param("data") BLabourAttendanceBO bLabourAttendanceBO);
    List<BLabourAttendance> selectValidationt(BLabourAttendance bLabourAttendance);

}

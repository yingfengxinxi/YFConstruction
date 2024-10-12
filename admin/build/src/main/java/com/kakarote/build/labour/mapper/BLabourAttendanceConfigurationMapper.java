package com.kakarote.build.labour.mapper;

import com.kakarote.build.labour.entity.BO.BLabourAttendanceBO;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceConfigurationBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendance;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceConfiguration;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 考勤日期设定 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-12-17
 */
public interface BLabourAttendanceConfigurationMapper extends BaseMapper<BLabourAttendanceConfiguration> {

    BasePage<BLabourAttendanceConfiguration> selectDataList(BasePage<AdminMessage> parse, @Param("data") BLabourAttendanceConfigurationBO bLabourAttendanceConfigurationBO);
    List<BLabourAttendanceConfiguration> selectValidationt(BLabourAttendanceConfiguration bLabourAttendanceConfiguration);

    List<BLabourAttendanceConfiguration> dataList(BLabourAttendanceConfiguration bLabourAttendanceConfiguration);

}

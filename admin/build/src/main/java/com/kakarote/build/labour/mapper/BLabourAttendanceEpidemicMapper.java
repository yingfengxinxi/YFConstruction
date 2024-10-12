package com.kakarote.build.labour.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.labour.entity.BO.BLabour5gvideoBO;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceEpidemicBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceEpidemic;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 疫情进出场统计 Mapper 接口
 * </p>
 *
 * @author zhang
 * @since 2022-11-29
 */
public interface BLabourAttendanceEpidemicMapper extends BaseMapper<BLabourAttendanceEpidemic> {
    BasePage<BLabourAttendanceEpidemicBO> selectDataList(BasePage<AdminMessage> parse, @Param("data") BLabourAttendanceEpidemicBO bLabourAttendanceEpidemicBO);
    @SqlParser(filter = true)
    List<Map<String, Object>> attendanceEpidemicStatistical(@Param("data") Map<String, Object> params);

    @SqlParser(filter = true)
    List<Map<String, Object>> attendanceEpidemicStatisticalList(@Param("data") Map<String, Object> params);

}

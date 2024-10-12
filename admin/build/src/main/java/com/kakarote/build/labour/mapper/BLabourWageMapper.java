package com.kakarote.build.labour.mapper;

import com.kakarote.build.labour.entity.BO.BLabourAttendanceBO;
import com.kakarote.build.labour.entity.BO.BLabourWageBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendance;
import com.kakarote.build.labour.entity.PO.BLabourWage;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工资管理 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-04-26
 */
public interface BLabourWageMapper extends BaseMapper<BLabourWage> {

    BasePage<BLabourWage> selectDataList(BasePage<AdminMessage> parse, @Param("data") BLabourWageBO bLabourWageBO);
    List<BLabourWage> selectValidationt(BLabourWage bLabourWage);
    Map<String,Object> selectById(@Param("data") String id);
}

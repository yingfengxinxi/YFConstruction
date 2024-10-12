package com.kakarote.build.labour.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.labour.entity.BO.BLabourDroneBO;
import com.kakarote.build.labour.entity.PO.BLabourDrone;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 无人机现场巡检 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2022-05-27
 */
public interface BLabourDroneMapper extends BaseMapper<BLabourDrone> {

    BasePage<BLabourDroneBO> selectDataList(BasePage<AdminMessage> parse, @Param("data") BLabourDroneBO bLabourDroneBO);

    @SqlParser(filter = true)
    Map<String, Object> getBatchIdFile(@Param("data") Map<String, Object> batchIdData);

    @SqlParser(filter = true)
    List<Map<String, Object>>  selectFileList(@Param("data") Map<String, Object> batchIdData);
}

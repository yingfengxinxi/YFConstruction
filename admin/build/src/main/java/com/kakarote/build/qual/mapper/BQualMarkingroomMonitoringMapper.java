package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.BQualMarkingroomMonitoringBO;
import com.kakarote.build.qual.entity.PO.BQualMarkingroomMonitoring;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 标养室监测信息 Mapper 接口
 * </p>
 *
 * @author zhang
 * @since 2022-07-14
 */
public interface BQualMarkingroomMonitoringMapper extends BaseMapper<BQualMarkingroomMonitoring> {

    BasePage<BQualMarkingroomMonitoring> selectDataList(BasePage<AdminMessage> parse, @Param("data") BQualMarkingroomMonitoringBO BQualMarkingroomMonitoringBO);
}

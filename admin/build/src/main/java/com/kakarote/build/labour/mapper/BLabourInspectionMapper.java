package com.kakarote.build.labour.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.labour.entity.BO.BLabour5gvideoBO;
import com.kakarote.build.labour.entity.BO.BLabourInspectionBO;
import com.kakarote.build.labour.entity.PO.BLabourInspection;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 企业报验 Mapper 接口
 * </p>
 *
 * @author zhang
 * @since 2022-11-30
 */
public interface BLabourInspectionMapper extends BaseMapper<BLabourInspection> {

    BasePage<BLabourInspectionBO> selectDataList(BasePage<AdminMessage> parse, @Param("data") BLabourInspectionBO bLabourInspectionBO);

    @SqlParser(filter = true)
    Map<String, Object> inspectionStatistical(@Param("data") Map<String, Object> params);

    @SqlParser(filter = true)
    List<Map<String, Object>> inspectionStatisticalHistogram(@Param("data") Map<String, Object> params);

    BasePage<Map> selectApplyList(BasePage<Object> parse, @Param("data") BLabourInspectionBO bLabourInspectionBO,
                                  @Param("userId") Long userId);

    Map<String, Object> selectApplyAndProjectInfo(@Param("id") String id);

    BasePage<Map> selectAllDataList(BasePage<Object> parse, @Param("data") BLabourInspectionBO bLabourInspectionBO);
}

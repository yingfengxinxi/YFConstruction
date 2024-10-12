package com.kakarote.build.labour.mapper;

import com.kakarote.build.labour.entity.PO.BLabourInspectionCheck;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 企业报验检查表 Mapper 接口
 * </p>
 *
 * @author zhang
 * @since 2022-11-30
 */
public interface BLabourInspectionCheckMapper extends BaseMapper<BLabourInspectionCheck> {

    BLabourInspectionCheck selectByInspectionId(@Param("inspectionId") String inspectionId);
}

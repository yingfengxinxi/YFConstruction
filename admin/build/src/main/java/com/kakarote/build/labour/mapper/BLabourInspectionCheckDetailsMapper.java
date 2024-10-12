package com.kakarote.build.labour.mapper;

import com.kakarote.build.labour.entity.PO.BLabourInspectionCheckDetails;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 企业报验检查表详细 Mapper 接口
 * </p>
 *
 * @author zhang
 * @since 2022-11-30
 */
public interface BLabourInspectionCheckDetailsMapper extends BaseMapper<BLabourInspectionCheckDetails> {

    List<BLabourInspectionCheckDetails> selectItemList(@Param("checkId") String checkId);
}

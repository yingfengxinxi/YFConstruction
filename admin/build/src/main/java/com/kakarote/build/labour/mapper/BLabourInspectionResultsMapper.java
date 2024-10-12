package com.kakarote.build.labour.mapper;

import com.kakarote.build.labour.entity.PO.BLabourInspectionResults;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 企业报验结果表 Mapper 接口
 * </p>
 *
 * @author zhang
 * @since 2022-11-30
 */
public interface BLabourInspectionResultsMapper extends BaseMapper<BLabourInspectionResults> {

    public BLabourInspectionResults getPid(@Param("pid")String pid);
}

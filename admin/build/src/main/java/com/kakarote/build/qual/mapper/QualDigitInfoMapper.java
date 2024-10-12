package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.QualDigitInfoBO;
import com.kakarote.build.qual.entity.BO.QualPlanBO;
import com.kakarote.build.qual.entity.PO.QualDigitInfo;
import com.kakarote.build.qual.entity.PO.QualPlan;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 数字质量资料 Mapper 接口
 * </p>
 *
 * @author pz
 * @since 2021-03-22
 */
public interface QualDigitInfoMapper extends BaseMapper<QualDigitInfo> {
    BasePage<QualDigitInfo> selectByPage(BasePage<QualDigitInfoBO> parse, @Param("data") QualDigitInfoBO planBO);
}

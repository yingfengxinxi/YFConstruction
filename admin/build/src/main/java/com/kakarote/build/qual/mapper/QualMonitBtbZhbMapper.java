package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.QualMonitSTbZhbBO;
import com.kakarote.build.qual.entity.PO.QualMonitBtbZhb;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 标养室监测--监测数据填报--主表 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-03-25
 */
public interface QualMonitBtbZhbMapper extends BaseMapper<QualMonitBtbZhb> {
    BasePage<QualMonitBtbZhb> selectList(BasePage<QualMonitBtbZhb> page, @Param("qualMonitSTbZhbBO") QualMonitSTbZhbBO qualMonitSTbZhbBO);

}

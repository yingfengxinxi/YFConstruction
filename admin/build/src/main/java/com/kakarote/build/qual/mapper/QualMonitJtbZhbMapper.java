package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.QualMonitSTbZhbBO;
import com.kakarote.build.qual.entity.PO.QualMonitJtbZhb;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 检测检验--监测数据填报--主表 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-03-25
 */
public interface QualMonitJtbZhbMapper extends BaseMapper<QualMonitJtbZhb> {

    BasePage<QualMonitJtbZhb> selectList(BasePage<QualMonitJtbZhb> page, @Param("qualMonitSTbZhbBO") QualMonitSTbZhbBO qualMonitSTbZhbBO);


}

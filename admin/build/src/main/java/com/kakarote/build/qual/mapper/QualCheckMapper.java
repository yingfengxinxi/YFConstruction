package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.QualCheckBO;
import com.kakarote.build.qual.entity.PO.QualCheck;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 施工质量--质量检查管理 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-03-23
 */
public interface QualCheckMapper extends BaseMapper<QualCheck> {

    BasePage<QualCheck> selectByPage(BasePage<QualCheck> parse, @Param("data") QualCheckBO qualCheckBO);

    Integer saveInfo(QualCheck qualCheck);

    Map<String, Object> getPrintData(@Param("id") Integer id);
}

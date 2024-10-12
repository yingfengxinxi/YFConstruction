package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.BO.QualCheckPersonnelBO;
import com.kakarote.build.qual.entity.PO.QualCheckPersonnel;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 验收人员管理 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-07-08
 */
public interface QualCheckPersonnelMapper extends BaseMapper<QualCheckPersonnel> {

    BasePage<QualCheckPersonnel> selectByPage(BasePage<Object> parse,@Param("data") QualCheckPersonnelBO qualCheckPersonnelBO);

    List<QualCheckPersonnel>  selectWxList(@Param("data") QualCheckPersonnelBO qualCheckPersonnelBO);
}

package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.PO.QualBuildMange;
import com.kakarote.build.qual.entity.PO.QualQuestions;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 施工质量--分户验收--楼栋管理 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-06-22
 */
public interface QualBuildMangeMapper extends BaseMapper<QualBuildMange> {

    List<QualBuildMange> queryByParentId(Integer parentId);

    int Save(@Param("data") QualBuildMange qualBuildMange);

    List<QualBuildMange> wXqueryByParentId(@Param("data") QualBuildMange qualBuildMange);
}

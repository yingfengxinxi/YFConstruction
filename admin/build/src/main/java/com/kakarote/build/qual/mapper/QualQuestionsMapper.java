package com.kakarote.build.qual.mapper;

import com.kakarote.build.qual.entity.PO.QualQuestions;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 质量问题库 Mapper 接口
 * </p>
 *
 * @author pz
 * @since 2021-03-22
 */
public interface QualQuestionsMapper extends BaseMapper<QualQuestions> {
    List<QualQuestions> queryByParentId(@Param("parentId") Integer parentId);

    List<Map<String, Object>> queryTextList();

    List<Map<String, Object>> selectQualHiddenDanger(@Param("data")Map<String,Object>map);

    List<Map<String, Object>> selectHiddenDangerNumber(@Param("data") Map<String, Object> map);
}

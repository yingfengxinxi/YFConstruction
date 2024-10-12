package com.kakarote.admin.mapper;

import com.kakarote.admin.entity.BO.AdminDictMathBO;
import com.kakarote.admin.entity.PO.AdminDictMatch;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-11-30
 */
public interface AdminDictMatchMapper extends BaseMapper<AdminDictMatch> {

    BasePage<AdminDictMatch> selectByPage(BasePage<Object> parse, @Param("data") AdminDictMathBO adminDictMathBO);

    List<Map<String, Object>> selectSuggestList(@Param("data") Map<String, Object> params);

    Map selectDictMatchByType(@Param("matchDict") String matchDict, @Param("value") String value, @Param("tenantId") Integer tenantId);

    List<AdminDictMatch> selectAll();
}

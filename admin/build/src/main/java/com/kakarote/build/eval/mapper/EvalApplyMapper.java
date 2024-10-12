package com.kakarote.build.eval.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.eval.entity.BO.EvalApplyBO;
import com.kakarote.build.eval.entity.PO.EvalApply;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 智慧工地评价-评估打分申请表 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2022-02-23
 */
public interface EvalApplyMapper extends BaseMapper<EvalApply> {

    BasePage<EvalApply> selectByPage(BasePage<Object> parse, @Param("data") EvalApplyBO evalApplyBO);

    BasePage<EvalApply> selectRankingList(BasePage<Object> parse, @Param("data") EvalApplyBO evalApplyBO);

    List<Map<String,Object>> selectClassTypeById(@Param("id") String id);

    EvalApply selectInfoById(@Param("id") String id);

    List<Map<String, Object>> selectBasicsItemTotalInfo(@Param("id") String id);

    double selectOtherScore(@Param("id") String id);

    @SqlParser(filter = true)
    List<Map<String, Object>> selectItemListByClassType(@Param("applyId") String applyId, @Param("classType") String classType,
                                                        @Param("tenantId") Long tenantId);

    List<Map<String, Object>> getItemList(@Param("data") Map<String, Object> params);
}

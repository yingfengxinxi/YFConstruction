package com.kakarote.build.eval.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.eval.entity.PO.EvalApplyItems;
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
 * @since 2022-02-23
 */
public interface EvalApplyItemsMapper extends BaseMapper<EvalApplyItems> {

    List<Map<String,Object>> selectClassTypeItemList(@Param("applyId") String applyId);

    List<EvalApplyItems> selectItemList(@Param("applyId") String applyId, @Param("tenantId") Long tenantId);

    int updateItemList(@Param("list") List<EvalApplyItems> params);
}

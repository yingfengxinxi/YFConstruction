package com.kakarote.build.eval.mapper;

import com.kakarote.build.eval.entity.BO.EvalItemsBO;
import com.kakarote.build.eval.entity.PO.EvalItems;
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
 * @since 2022-02-22
 */
public interface EvalItemsMapper extends BaseMapper<EvalItems> {

    BasePage<EvalItems> selectByPage(BasePage<Object> parse, @Param("data") EvalItemsBO evalItemsBO);

    List<Map<String, Object>> selectItemList(@Param("data") EvalItemsBO evalItemsBO);

    List<EvalItems> selectListByClassType(@Param("data") List<String> classTypeList);

    List<EvalItems> selectListByItemName(@Param("data") List<String> itemNameList);
}

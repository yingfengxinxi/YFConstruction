package com.kakarote.build.eval.mapper;

import com.kakarote.build.eval.entity.BO.EvalLevelBO;
import com.kakarote.build.eval.entity.PO.EvalLevel;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2022-02-22
 */
public interface EvalLevelMapper extends BaseMapper<EvalLevel> {

    BasePage<EvalLevel> selectByPage(BasePage<Object> parse, @Param("data") EvalLevelBO evalLevelBO);
}

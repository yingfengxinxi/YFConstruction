package com.kakarote.build.eval.service;

import com.kakarote.build.eval.entity.BO.EvalLevelBO;
import com.kakarote.build.eval.entity.PO.EvalLevel;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzy
 * @since 2022-02-22
 */
public interface IEvalLevelService extends BaseService<EvalLevel> {

    BasePage<EvalLevel> selectByPage(EvalLevelBO evalLevelBO);
}

package com.kakarote.build.eval.service.impl;

import com.kakarote.build.eval.entity.BO.EvalLevelBO;
import com.kakarote.build.eval.entity.PO.EvalLevel;
import com.kakarote.build.eval.mapper.EvalLevelMapper;
import com.kakarote.build.eval.service.IEvalLevelService;
import com.kakarote.build.safety.entity.PO.SafetyInspectionPlan;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lzy
 * @since 2022-02-22
 */
@Service
public class EvalLevelServiceImpl extends BaseServiceImpl<EvalLevelMapper, EvalLevel> implements IEvalLevelService {

    @Override
    public BasePage<EvalLevel> selectByPage(EvalLevelBO evalLevelBO) {
        BasePage<EvalLevel> list = this.getBaseMapper().selectByPage(evalLevelBO.parse(), evalLevelBO);
        return list;
    }
}

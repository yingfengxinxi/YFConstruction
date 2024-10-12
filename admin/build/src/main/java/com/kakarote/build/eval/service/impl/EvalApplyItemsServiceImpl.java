package com.kakarote.build.eval.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kakarote.build.eval.entity.PO.EvalApplyItems;
import com.kakarote.build.eval.mapper.EvalApplyItemsMapper;
import com.kakarote.build.eval.service.IEvalApplyItemsService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lzy
 * @since 2022-02-23
 */
@Service
public class EvalApplyItemsServiceImpl extends BaseServiceImpl<EvalApplyItemsMapper, EvalApplyItems> implements IEvalApplyItemsService {

    @Override
    public List<Map<String, Object>> selectClassTypeItemList(String applyId) {
        return getBaseMapper().selectClassTypeItemList(applyId);
    }

    @Override
    public List<EvalApplyItems> selectItemList(String applyId) {
        Long tenantId = UserUtil.getUser().getTenantId();
        return getBaseMapper().selectItemList(applyId,tenantId);
    }

    @Override
    public int updateItemList(List<EvalApplyItems> params) {
        int num = getBaseMapper().updateItemList(params);
        return num;
    }
}

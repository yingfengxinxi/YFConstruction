package com.kakarote.build.eval.service;

import com.kakarote.build.eval.entity.PO.EvalApplyItems;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzy
 * @since 2022-02-23
 */
public interface IEvalApplyItemsService extends BaseService<EvalApplyItems> {

    List<Map<String, Object>> selectClassTypeItemList(String applyId);

    List<EvalApplyItems> selectItemList(String applyId);

    int updateItemList(List<EvalApplyItems> params);
}

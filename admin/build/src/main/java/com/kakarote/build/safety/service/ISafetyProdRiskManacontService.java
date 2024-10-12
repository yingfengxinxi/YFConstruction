package com.kakarote.build.safety.service;

import com.kakarote.build.safety.entity.PO.BSafetyDangerSource;
import com.kakarote.build.safety.entity.PO.SafetyProdRiskManacont;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 安全生产风险管控 服务类
 * </p>
 *
 * @author zy
 * @since 2021-04-02
 */
public interface ISafetyProdRiskManacontService extends BaseService<SafetyProdRiskManacont> {

    List<SafetyProdRiskManacont> queryByParentId(Integer parentId);

    boolean removeListById(Integer id);

}

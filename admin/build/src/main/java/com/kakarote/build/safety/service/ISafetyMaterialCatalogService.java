package com.kakarote.build.safety.service;

import com.kakarote.build.safety.entity.PO.SafetyMaterialCatalog;
import com.kakarote.core.common.Result;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 安全资料目录 服务类
 * </p>
 *
 * @author wudw
 * @since 2021-04-07
 */
public interface ISafetyMaterialCatalogService extends BaseService<SafetyMaterialCatalog> {

    public List<Map> selectTree(Integer id);

    void deleteOneselfAndSublevel(Integer id);
}

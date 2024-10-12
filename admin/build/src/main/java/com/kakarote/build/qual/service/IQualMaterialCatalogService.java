package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.PO.QualMaterialCatalog;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 质量资料目录 服务类
 * </p>
 *
 * @author wudw
 * @since 2021-04-13
 */
public interface IQualMaterialCatalogService extends BaseService<QualMaterialCatalog> {

    public List<Map> selectTree(Integer id);

}

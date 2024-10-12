package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.QualMaterialBO;
import com.kakarote.build.qual.entity.PO.QualMaterial;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 质量资料 服务类
 * </p>
 *
 * @author wudw
 * @since 2021-04-13
 */
public interface IQualMaterialService extends BaseService<QualMaterial> {

    BasePage<QualMaterial> selectPageAllByPid(QualMaterialBO qualMaterialBO);

    int collect(Integer id,Long userId);

    List<Integer> selectCollect(Long userId);

    List<QualMaterial> selectByCollect(Integer catalogId);

    int deleteCollect(Integer materialId);

}

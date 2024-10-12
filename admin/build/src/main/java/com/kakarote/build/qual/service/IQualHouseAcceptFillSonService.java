package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.PO.QualHouseAcceptFillSon;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 施工质量--分户验收--填报--子表 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-06-29
 */
public interface IQualHouseAcceptFillSonService extends BaseService<QualHouseAcceptFillSon> {

    List<QualHouseAcceptFillSon> wxSelectByPid(Integer id);

}

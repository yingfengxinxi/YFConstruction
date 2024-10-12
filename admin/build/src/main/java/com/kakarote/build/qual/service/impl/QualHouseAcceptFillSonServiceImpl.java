package com.kakarote.build.qual.service.impl;

import com.kakarote.build.qual.entity.PO.QualHouseAcceptFillSon;
import com.kakarote.build.qual.mapper.QualHouseAcceptFillSonMapper;
import com.kakarote.build.qual.service.IQualHouseAcceptFillSonService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 施工质量--分户验收--填报--子表 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-06-29
 */
@Service
public class QualHouseAcceptFillSonServiceImpl extends BaseServiceImpl<QualHouseAcceptFillSonMapper, QualHouseAcceptFillSon> implements IQualHouseAcceptFillSonService {

    @Override
    public List<QualHouseAcceptFillSon> wxSelectByPid(Integer id) {
        return getBaseMapper().wxSelectByPid(id);
    }
}

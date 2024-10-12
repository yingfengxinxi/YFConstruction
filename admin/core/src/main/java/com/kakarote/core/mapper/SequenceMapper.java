package com.kakarote.core.mapper;

import com.kakarote.core.entity.PO.Sequence;
import com.kakarote.core.servlet.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pz
 * @since 2021-04-02
 */
public interface SequenceMapper extends BaseMapper<Sequence> {

    int insertTenantId(Sequence sequenceNew);
}

package com.kakarote.core.service;

import com.kakarote.core.entity.PO.Sequence;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pz
 * @since 2021-04-02
 */
public interface ISequenceService extends BaseService<Sequence> {

    String getDateSeq(String type);

    String getDateSeqNew(String type);

    String getDateSeq(String type, Integer peojectId);

    String getDateSeq(String type, Integer tenantId, Integer peojectId);
}

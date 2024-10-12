package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.PO.BLabourInspectionCheckDetails;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 企业报验检查表详细 服务类
 * </p>
 *
 * @author zhang
 * @since 2022-11-30
 */
public interface IBLabourInspectionCheckDetailsService extends BaseService<BLabourInspectionCheckDetails> {

    List<BLabourInspectionCheckDetails> selectItemList(String checkId);
}

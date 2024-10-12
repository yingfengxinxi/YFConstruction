package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.PO.BLabourInspectionCheck;
import com.kakarote.core.servlet.BaseService;

import java.util.Map;

/**
 * <p>
 * 企业报验检查表 服务类
 * </p>
 *
 * @author zhang
 * @since 2022-11-30
 */
public interface IBLabourInspectionCheckService extends BaseService<BLabourInspectionCheck> {

    Map<String, Object> selectByInspectionId(String inspectionId);

    boolean saveCheckData(Map<String, Object> params);
}

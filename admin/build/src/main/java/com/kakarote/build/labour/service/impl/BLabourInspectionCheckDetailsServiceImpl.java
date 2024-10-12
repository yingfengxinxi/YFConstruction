package com.kakarote.build.labour.service.impl;

import com.kakarote.build.labour.entity.PO.BLabourInspectionCheckDetails;
import com.kakarote.build.labour.mapper.BLabourInspectionCheckDetailsMapper;
import com.kakarote.build.labour.service.IBLabourInspectionCheckDetailsService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 企业报验检查表详细 服务实现类
 * </p>
 *
 * @author zhang
 * @since 2022-11-30
 */
@Service
public class BLabourInspectionCheckDetailsServiceImpl extends BaseServiceImpl<BLabourInspectionCheckDetailsMapper, BLabourInspectionCheckDetails> implements IBLabourInspectionCheckDetailsService {

    @Override
    public List<BLabourInspectionCheckDetails> selectItemList(String checkId) {
        return getBaseMapper().selectItemList(checkId);
    }
}

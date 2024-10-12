package com.kakarote.build.labour.service.impl;

import com.kakarote.build.labour.entity.BO.LabourConstructionUnitBO;
import com.kakarote.build.labour.entity.PO.LabourConstructionUnit;
import com.kakarote.build.labour.mapper.LabourConstructionUnitMapper;
import com.kakarote.build.labour.service.ILabourConstructionUnitService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 建设单位管理 服务实现类
 * </p>
 *
 * @author shz
 * @since 2022-04-20
 */
@Service
public class LabourConstructionUnitServiceImpl extends BaseServiceImpl<LabourConstructionUnitMapper, LabourConstructionUnit> implements ILabourConstructionUnitService {

    @Override
    public BasePage<Map<String, Object>> selectPageList(LabourConstructionUnitBO constructionUnitBO) {
        return getBaseMapper().selectPageList(constructionUnitBO.parse(),constructionUnitBO);
    }

}

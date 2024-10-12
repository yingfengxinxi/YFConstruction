package com.kakarote.build.ai.service.impl;

import com.kakarote.build.ai.entity.BO.AiDregCarBO;
import com.kakarote.build.ai.entity.BO.BAiDregCarCleaningBO;
import com.kakarote.build.ai.entity.PO.AiDregCar;
import com.kakarote.build.ai.entity.PO.BAiDregCarCleaning;
import com.kakarote.build.ai.mapper.BAiDregCarCleaningMapper;
import com.kakarote.build.ai.service.IBAiDregCarCleaningService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车辆清洗记录 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-09-22
 */
@Service
public class BAiDregCarCleaningServiceImpl extends BaseServiceImpl<BAiDregCarCleaningMapper, BAiDregCarCleaning> implements IBAiDregCarCleaningService {

    @Override
    public BasePage<BAiDregCarCleaning> selectByPage(BAiDregCarCleaningBO bAiDregCarCleaningBO) {
        bAiDregCarCleaningBO.setOrdersBd("c.cleaning_time",false);
        BasePage<BAiDregCarCleaning> list = getBaseMapper().selectByPage(bAiDregCarCleaningBO.parse(), bAiDregCarCleaningBO);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectReportData(Map<String, Object> params) {
        List<Map<String, Object>> list = getBaseMapper().selectReportData(params);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectTypeProportion() {
        return getBaseMapper().selectTypeProportion();
    }

    @Override
    public List<Map<String, Object>> selectTop10List() {
        return getBaseMapper().selectTop10List();
    }
}

package com.kakarote.build.safety.service.impl;

import com.kakarote.build.safety.entity.BO.BSafetySafetyDataBO;
import com.kakarote.build.safety.entity.BO.BSafetySafetyTrainingBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyData;
import com.kakarote.build.safety.entity.PO.BSafetySafetyTraining;
import com.kakarote.build.safety.mapper.BSafetySafetyTrainingMapper;
import com.kakarote.build.safety.service.IBSafetySafetyTrainingService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 安全教育培训管理 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-04-08
 */
@Service
public class BSafetySafetyTrainingServiceImpl extends BaseServiceImpl<BSafetySafetyTrainingMapper, BSafetySafetyTraining> implements IBSafetySafetyTrainingService {

    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BSafetySafetyTraining> selectList(BSafetySafetyTrainingBO bSafetySafetyTrainingBO)
    {
        bSafetySafetyTrainingBO.setOrdersBd("create_time",false);
        BasePage<BSafetySafetyTraining> adminMessageBasePage = getBaseMapper().selectDataList(bSafetySafetyTrainingBO.parse(),bSafetySafetyTrainingBO);
        return adminMessageBasePage;
    }
}

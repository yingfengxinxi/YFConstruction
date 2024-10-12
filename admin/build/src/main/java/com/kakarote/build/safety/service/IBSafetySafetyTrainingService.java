package com.kakarote.build.safety.service;

import com.kakarote.build.safety.entity.BO.BSafetySafetyTrainingBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyData;
import com.kakarote.build.safety.entity.PO.BSafetySafetyTraining;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 安全教育培训管理 服务类
 * </p>
 *
 * @author author
 * @since 2021-04-08
 */
public interface IBSafetySafetyTrainingService extends BaseService<BSafetySafetyTraining> {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BSafetySafetyTraining> selectList(BSafetySafetyTrainingBO bSafetySafetyTrainingBO);

}

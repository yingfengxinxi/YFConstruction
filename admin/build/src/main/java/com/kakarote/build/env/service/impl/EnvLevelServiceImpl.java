package com.kakarote.build.env.service.impl;

import com.kakarote.build.env.entity.BO.EnvLevelBO;
import com.kakarote.build.env.entity.PO.EnvLevel;
import com.kakarote.build.env.mapper.EnvLevelMapper;
import com.kakarote.build.env.service.IEnvLevelService;
import com.kakarote.build.qual.entity.PO.QualCheck;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 环境数据标准设置表 服务实现类
 * </p>
 *
 * @author pz
 * @since 2021-09-17
 */
@Service
public class EnvLevelServiceImpl extends BaseServiceImpl<EnvLevelMapper, EnvLevel> implements IEnvLevelService {

    @Override
    public EnvLevel getEnvLevel(double data) {
       return this.getBaseMapper().getEnvLevel(data);
    }

    @Override
    public BasePage<EnvLevel> selectByPage(EnvLevelBO envLevelBO) {
        return this.getBaseMapper().selectByPage(envLevelBO.parse(), envLevelBO);
    }

}

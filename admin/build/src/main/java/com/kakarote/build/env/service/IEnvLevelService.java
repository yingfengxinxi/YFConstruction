package com.kakarote.build.env.service;

import com.kakarote.build.env.entity.BO.EnvLevelBO;
import com.kakarote.build.env.entity.PO.EnvLevel;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 环境数据标准设置表 服务类
 * </p>
 *
 * @author pz
 * @since 2021-09-17
 */
public interface IEnvLevelService extends BaseService<EnvLevel> {

    /**
    * 根据PM2.5值获取空气质量等级：levelName
    */
    EnvLevel getEnvLevel(double data);

    BasePage<EnvLevel> selectByPage(EnvLevelBO envLevelBO);
}

package com.kakarote.build.env.mapper;

import com.kakarote.build.env.entity.BO.EnvLevelBO;
import com.kakarote.build.env.entity.PO.EnvLevel;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 环境数据标准设置表 Mapper 接口
 * </p>
 *
 * @author pz
 * @since 2021-09-17
 */
public interface EnvLevelMapper extends BaseMapper<EnvLevel> {
    EnvLevel getEnvLevel(@Param("data") double data);
    BasePage<EnvLevel> selectByPage(BasePage<EnvLevel> parse, @Param("data") EnvLevelBO envLevelBO);

}

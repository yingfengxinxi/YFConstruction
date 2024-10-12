package com.kakarote.build.app.mapper;

import com.kakarote.build.app.entity.PO.AppVersion;
import com.kakarote.core.servlet.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2021-07-01
 */
public interface AppVersionMapper extends BaseMapper<AppVersion> {

    AppVersion selectNewVersion();
}

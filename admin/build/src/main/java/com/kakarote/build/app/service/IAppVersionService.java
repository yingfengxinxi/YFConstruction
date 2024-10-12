package com.kakarote.build.app.service;

import com.kakarote.build.app.entity.PO.AppVersion;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shz
 * @since 2021-07-01
 */
public interface IAppVersionService extends BaseService<AppVersion> {

    public AppVersion selectNewVersion();

}

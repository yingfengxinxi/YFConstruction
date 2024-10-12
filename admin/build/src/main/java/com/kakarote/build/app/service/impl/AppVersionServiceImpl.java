package com.kakarote.build.app.service.impl;

import com.kakarote.build.app.entity.PO.AppVersion;
import com.kakarote.build.app.mapper.AppVersionMapper;
import com.kakarote.build.app.service.IAppVersionService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shz
 * @since 2021-07-01
 */
@Service
public class AppVersionServiceImpl extends BaseServiceImpl<AppVersionMapper, AppVersion> implements IAppVersionService {

    @Override
    public AppVersion selectNewVersion() {
        return getBaseMapper().selectNewVersion();
    }
}

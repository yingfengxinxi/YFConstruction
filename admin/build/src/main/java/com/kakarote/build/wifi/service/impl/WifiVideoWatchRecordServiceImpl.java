package com.kakarote.build.wifi.service.impl;

import com.kakarote.build.wifi.entity.BO.WifiVideoBO;
import com.kakarote.build.wifi.entity.PO.WifiVideoWatchRecord;
import com.kakarote.build.wifi.mapper.WifiVideoWatchRecordMapper;
import com.kakarote.build.wifi.service.IWifiVideoWatchRecordService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 教育视频观看记录 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-06-01
 */
@Service
public class WifiVideoWatchRecordServiceImpl extends BaseServiceImpl<WifiVideoWatchRecordMapper, WifiVideoWatchRecord> implements IWifiVideoWatchRecordService {

    @Override
    public BasePage<Map<String, Object>> selectList(WifiVideoBO wifiVideoBO) {
                wifiVideoBO.setOrdersBd("a.create_time",false);
                wifiVideoBO.setProjectId(UserUtil.getUser().getProjectId());
                wifiVideoBO.setTenantId(UserUtil.getUser().getTenantId());
        return getBaseMapper().selectList(wifiVideoBO.parse(),wifiVideoBO);
    }

    @Override
    public Map getCreateTimeLast(Map map) {
        return getBaseMapper().getCreateTimeLast(map);
    }
}

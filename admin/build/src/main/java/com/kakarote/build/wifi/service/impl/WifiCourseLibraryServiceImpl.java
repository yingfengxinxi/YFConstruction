package com.kakarote.build.wifi.service.impl;


import com.kakarote.build.wifi.entity.BO.WifiCourseLibraryBO;
import com.kakarote.build.wifi.entity.PO.WifiCourseLibrary;
import com.kakarote.build.wifi.mapper.WifiCourseLibraryMapper;
import com.kakarote.build.wifi.service.IWifiCourseLibraryService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * wifi教育-课程库 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-08-19
 */
@Service
public class WifiCourseLibraryServiceImpl extends BaseServiceImpl<WifiCourseLibraryMapper, WifiCourseLibrary> implements IWifiCourseLibraryService {

    @Override
    public BasePage<WifiCourseLibrary> getLibraryList(WifiCourseLibraryBO wifiCourseLibraryBO) {
        wifiCourseLibraryBO.setOrdersBd("create_time",false);
        BasePage<WifiCourseLibrary> sss=getBaseMapper().getLibraryList(wifiCourseLibraryBO.parse(),wifiCourseLibraryBO);
        return sss;
    }
}

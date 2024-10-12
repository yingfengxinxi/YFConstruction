package com.kakarote.build.wifi.service;

import com.kakarote.build.wifi.entity.BO.WifiCourseLibraryBO;
import com.kakarote.build.wifi.entity.PO.WifiCourseLibrary;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * wifi教育-课程库 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-08-19
 */
public interface IWifiCourseLibraryService extends BaseService<WifiCourseLibrary> {

    BasePage<WifiCourseLibrary> getLibraryList(WifiCourseLibraryBO wifiCourseLibraryBO);
}

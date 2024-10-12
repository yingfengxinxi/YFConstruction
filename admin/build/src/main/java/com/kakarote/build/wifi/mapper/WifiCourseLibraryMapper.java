package com.kakarote.build.wifi.mapper;

import com.kakarote.build.wifi.entity.BO.WifiCourseLibraryBO;
import com.kakarote.build.wifi.entity.PO.WifiCourseLibrary;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * wifi教育-课程库 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-08-19
 */
public interface WifiCourseLibraryMapper extends BaseMapper<WifiCourseLibrary> {

    BasePage<WifiCourseLibrary> getLibraryList(BasePage<Object> parse,@Param("data") WifiCourseLibraryBO wifiCourseLibraryBO);
}

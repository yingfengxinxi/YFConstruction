package com.kakarote.build.wifi.service;

import com.kakarote.build.wifi.entity.BO.WifiTestBO;
import com.kakarote.build.wifi.entity.PO.WifiTest;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 生成试卷 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-05-25
 */
public interface IWifiTestService extends BaseService<WifiTest> {

    Map<String,Object> createTestPaper(WifiTestBO wifiTestBO) throws  Exception;

    Map<String, Object>  SubmitTestPaper(Map<String,Object> map);

    BasePage<Map<String,Object>> getAllTestPaper(WifiTestBO wifiTestBO);

    Map<String, Object> getPaperDetail(String testId);

    Map<String, Object> getMapByWorkTypeIdToEcharts();
}

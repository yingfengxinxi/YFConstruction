package com.kakarote.build.wifi.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.labour.entity.BO.LabourSpeworktypeBO;
import com.kakarote.build.labour.entity.PO.LabourSpeworktype;
import com.kakarote.build.wifi.entity.BO.WifiQuestionsBO;
import com.kakarote.build.wifi.entity.PO.WifiQuestions;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * wifi教育答题--问题表 服务类
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
public interface IWifiQuestionsService extends BaseService<WifiQuestions> {

    public BasePage<Map<String,Object>> selectList(WifiQuestionsBO wifiQuestionsBO);

    List<Map<String,Object>> TestPaperQue(Map<String,Object> map);

    int TestPaperQueCount(Map<String,Object> map);

    JSONObject excelImport(MultipartFile file,String typeId) throws Exception;

    JSONObject excelImport2(MultipartFile file,String typeId) throws Exception;

    JSONObject excelImport3(MultipartFile file,String typeId) throws Exception;
    void deleteQuestionById(String id);
}
